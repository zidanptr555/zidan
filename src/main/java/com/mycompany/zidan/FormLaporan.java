/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.zidan;

import javax.swing.*;
import java.awt.*;

public class FormLaporan extends JFrame {

    private final LaporanDao laporanDao = new LaporanDao();

    public FormLaporan() {
        setTitle("Menu Cetak Laporan");
        setSize(420, 260);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 10, 10));

        JLabel lblJudul = new JLabel("CETAK LAPORAN SISTEM", SwingConstants.CENTER);
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 16));

        JButton btnPreviewAkun = new JButton("Preview Laporan Akun");
        JButton btnPdfAkun = new JButton("Simpan PDF Laporan Akun");
        JButton btnPreviewLaptop = new JButton("Preview Laporan Laptop");
        JButton btnPdfLaptop = new JButton("Simpan PDF Laporan Laptop");

        btnPreviewAkun.addActionListener(e -> laporanDao.cetakLaporanAkun());
        btnPdfAkun.addActionListener(e -> laporanDao.simpanPdfAkun());
        btnPreviewLaptop.addActionListener(e -> laporanDao.cetakLaporanLaptop());
        btnPdfLaptop.addActionListener(e -> laporanDao.simpanPdfLaptop());

        add(lblJudul);
        add(btnPreviewAkun);
        add(btnPdfAkun);
        add(btnPreviewLaptop);
        add(btnPdfLaptop);

        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FormLaporan().setVisible(true);
        });
    }
}

