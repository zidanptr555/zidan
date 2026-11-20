/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.zidan;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class LaporanDao {

    /**
     * Menampilkan jendela JasperViewer (Pratinjau Laporan sebelum dicetak/disimpan)
     */
    public void tampilkanLaporan(String namaFileJrxml, Map<String, Object> parameters) {
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
             InputStream reportStream = getClass().getResourceAsStream("/reports/" + namaFileJrxml)) {

            if (reportStream == null) {
                JOptionPane.showMessageDialog(null,
                        "File template laporan tidak ditemukan di: /reports/" + namaFileJrxml,
                        "Error File Laporan",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kompilasi file .jrxml
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Isi laporan dengan data dari koneksi database
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    (parameters != null) ? parameters : new HashMap<>(),
                    conn
            );

            // Tampilkan JasperViewer (false = menutup viewer tidak menutup aplikasi utama)
            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setTitle("Preview Laporan - " + namaFileJrxml);
            viewer.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Gagal mencetak laporan:\n" + e.getMessage(),
                    "Error JasperReports",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Langsung ekspor laporan ke file PDF menggunakan dialog pemilihan folder (JFileChooser)
     */
    public void exportKePdfDenganDialog(String namaFileJrxml, String defaultFileName) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Laporan PDF");
        fileChooser.setSelectedFile(new java.io.File(defaultFileName));

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String outputPath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!outputPath.toLowerCase().endsWith(".pdf")) {
                outputPath += ".pdf";
            }

            try (Connection conn = DatabaseConfig.getDataSource().getConnection();
                 InputStream reportStream = getClass().getResourceAsStream("/reports/" + namaFileJrxml)) {

                if (reportStream == null) {
                    throw new RuntimeException("File tidak ditemukan: /reports/" + namaFileJrxml);
                }

                JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), conn);

                JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);
                JOptionPane.showMessageDialog(null,
                        "Laporan PDF berhasil disimpan di:\n" + outputPath,
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Gagal menyimpan PDF:\n" + e.getMessage(),
                        "Error Export PDF",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Method siap pakai untuk tombol UI
    public void cetakLaporanAkun() {
        tampilkanLaporan("laporan_akun.jrxml", new HashMap<>());
    }

    public void cetakLaporanLaptop() {
        tampilkanLaporan("laporan_laptop.jrxml", new HashMap<>());
    }

    public void simpanPdfAkun() {
        exportKePdfDenganDialog("laporan_akun.jrxml", "Laporan_Akun.pdf");
    }

    public void simpanPdfLaptop() {
        exportKePdfDenganDialog("laporan_laptop.jrxml", "Laporan_Laptop.pdf");
    }
}

