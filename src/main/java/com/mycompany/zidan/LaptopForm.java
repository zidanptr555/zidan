package com.mycompany.zidan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class LaptopForm extends javax.swing.JFrame {

    private JTextField txtMerk;
    private JTextField txtTipe;
    private JTextField txtHarga;
    private JTextField txtSearch;
    private JTable tableLaptop;
    private DefaultTableModel tableModel;

    private JButton btnSimpan;
    private JButton btnUpdate;
    private JButton btnHapus;
    private JButton btnReset;
    private JButton btnExport;
    private JButton btnLogout;

    private final LaptopDao laptopDao = new LaptopDao();
    private int selectedLaptopId = -1; // Menyimpan ID baris yang diklik untuk fitur Edit
    private String currentUserRole = "ADMIN"; // Default role jika di-run langsung

    // Konstruktor default (untuk testing langsung)
    public LaptopForm() {
        this("ADMIN");
    }

    // Konstruktor dengan parameter Role dari LoginForm
    public LaptopForm(String role) {
        this.currentUserRole = (role != null) ? role.toUpperCase() : "USER";
        initComponents();
        setLocationRelativeTo(null);
        applyRoleAccess();
        loadDataLaptop();
    }

    private void initComponents() {
        setTitle("Sistem Manajemen Laptop - Role: " + currentUserRole);
        setSize(850, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- PANEL ATAS: FORM INPUT & PENCARIAN ---
        JPanel topContainer = new JPanel(new BorderLayout(5, 5));

        JPanel panelInput = new JPanel(new GridLayout(3, 2, 10, 10));
        panelInput.setBorder(BorderFactory.createTitledBorder("Input / Edit Data Laptop"));

        txtMerk = new JTextField();
        txtTipe = new JTextField();
        txtHarga = new JTextField();

        panelInput.add(new JLabel("Merk Laptop:"));
        panelInput.add(txtMerk);
        panelInput.add(new JLabel("Tipe Laptop:"));
        panelInput.add(txtTipe);
        panelInput.add(new JLabel("Harga (Rp):"));
        panelInput.add(txtHarga);

        // Panel Search Bar (FITUR 1)
        JPanel panelSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSearch.add(new JLabel("Cari Laptop (Merk/Tipe):"));
        txtSearch = new JTextField(20);
        panelSearch.add(txtSearch);

        topContainer.add(panelInput, BorderLayout.CENTER);
        topContainer.add(panelSearch, BorderLayout.SOUTH);

        // --- PANEL TENGAH: TABEL DATA ---
        tableModel = new DefaultTableModel(new String[]{"ID Laptop", "Merk", "Tipe", "Harga"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Mencegah edit langsung di sel tabel
            }
        };
        tableLaptop = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableLaptop);

        // --- PANEL BAWAH: TOMBOL AKSI ---
        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnSimpan = new JButton("Simpan Baru");
        btnUpdate = new JButton("Update Data");
        btnHapus = new JButton("Hapus");
        btnReset = new JButton("Reset");
        btnExport = new JButton("Export Excel (CSV)");
        btnLogout = new JButton("Logout");

        btnUpdate.setEnabled(false); // Nonaktif sampai user memilih baris tabel

        panelButton.add(btnSimpan);
        panelButton.add(btnUpdate);
        panelButton.add(btnHapus);
        panelButton.add(btnReset);
        panelButton.add(btnExport);
        panelButton.add(btnLogout);

        // --- EVENT LISTENERS ---
        // 1. Live Search saat mengetik
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                cariData(txtSearch.getText().trim());
            }
        });

        // 2. Klik Tabel untuk Edit (FITUR 2)
        tableLaptop.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableLaptop.getSelectedRow();
                if (row != -1) {
                    selectedLaptopId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    txtMerk.setText(tableModel.getValueAt(row, 1).toString());
                    txtTipe.setText(tableModel.getValueAt(row, 2).toString());
                    txtHarga.setText(tableModel.getValueAt(row, 3).toString());

                    btnSimpan.setEnabled(false);
                    if ("ADMIN".equals(currentUserRole)) {
                        btnUpdate.setEnabled(true);
                    }
                }
            }
        });

        btnSimpan.addActionListener(e -> simpanData());
        btnUpdate.addActionListener(e -> updateData());
        btnHapus.addActionListener(e -> hapusData());
        btnReset.addActionListener(e -> resetForm());
        btnExport.addActionListener(e -> exportToCSV());
        btnLogout.addActionListener(e -> logout());

        add(topContainer, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelButton, BorderLayout.SOUTH);
    }

    // FITUR 3: Pengaturan Hak Akses Role (RBAC)
    private void applyRoleAccess() {
        if (!"ADMIN".equals(currentUserRole)) {
            btnHapus.setEnabled(false);
            btnUpdate.setEnabled(false);
            btnHapus.setToolTipText("Hanya ADMIN yang dapat menghapus data");
            btnUpdate.setToolTipText("Hanya ADMIN yang dapat mengubah data");
        }
    }

    public void loadDataLaptop() {
        tableModel.setRowCount(0);
        List<Object[]> dataList = laptopDao.getAllLaptop();
        for (Object[] row : dataList) {
            tableModel.addRow(row);
        }
    }

    // Eksekusi Pencarian (FITUR 1)
    private void cariData(String keyword) {
        tableModel.setRowCount(0);
        List<Object[]> dataList = keyword.isEmpty() 
                ? laptopDao.getAllLaptop() 
                : laptopDao.searchLaptop(keyword);
        for (Object[] row : dataList) {
            tableModel.addRow(row);
        }
    }

    private void simpanData() {
        String merk = txtMerk.getText().trim();
        String tipe = txtTipe.getText().trim();
        String hargaText = txtHarga.getText().trim();

        if (merk.isEmpty() || tipe.isEmpty() || hargaText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field wajib diisi!");
            return;
        }

        try {
            double harga = Double.parseDouble(hargaText);
            if (laptopDao.insertLaptop(merk, tipe, harga)) {
                JOptionPane.showMessageDialog(this, "Data laptop berhasil disimpan!");
                resetForm();
                loadDataLaptop();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan data!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Format harga harus berupa angka!");
        }
    }

    // Eksekusi Update Data (FITUR 2)
    private void updateData() {
        if (selectedLaptopId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data laptop dari tabel terlebih dahulu!");
            return;
        }

        String merk = txtMerk.getText().trim();
        String tipe = txtTipe.getText().trim();
        String hargaText = txtHarga.getText().trim();

        if (merk.isEmpty() || tipe.isEmpty() || hargaText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field wajib diisi!");
            return;
        }

        try {
            double harga = Double.parseDouble(hargaText);
            if (laptopDao.updateLaptop(selectedLaptopId, merk, tipe, harga)) {
                JOptionPane.showMessageDialog(this, "Data laptop berhasil diperbarui!");
                resetForm();
                loadDataLaptop();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal memperbarui data!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Format harga harus berupa angka!");
        }
    }

    private void hapusData() {
        int selectedRow = tableLaptop.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris laptop pada tabel yang ingin dihapus!");
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(
            this, "Yakin ingin menghapus data ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {
            int idLaptop = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            if (laptopDao.deleteLaptop(idLaptop)) {
                JOptionPane.showMessageDialog(this, "Data laptop berhasil dihapus!");
                resetForm();
                loadDataLaptop();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data!");
            }
        }
    }

    // FITUR 5: Export Data Tabel ke File CSV (Bisa dibuka di Excel)
    private void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Laporan Data Laptop");
        fileChooser.setSelectedFile(new File("Laporan_Data_Laptop.csv"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".csv")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
            }

            try (java.io.PrintWriter writer = new java.io.PrintWriter(fileToSave, "UTF-8")) {
                
                // Tulis Header Kolom (Dipisah koma tanpa tanda kutip berlebih di ID)
                writer.print("ID Laptop,Merk,Tipe,Harga");
                writer.println();

                // Tulis Baris Data dari JTable
                for (int row = 0; row < tableModel.getRowCount(); row++) {
                    String id = tableModel.getValueAt(row, 0).toString();
                    String merk = tableModel.getValueAt(row, 1).toString();
                    String tipe = tableModel.getValueAt(row, 2).toString();
                    
                    // Format harga agar dibaca sebagai angka biasa oleh Excel (menghindari 1.4E7)
                    Object hargaObj = tableModel.getValueAt(row, 3);
                    double hargaVal = Double.parseDouble(hargaObj.toString());
                    String harga = String.format("%.0f", hargaVal); // Mengubah 1.4E7 jadi 14000000

                    // Gabungkan dengan koma, bungkus Tipe dengan tanda kutip mengantisipasi spasi/koma
                    writer.print(id + "," + merk + ",\"" + tipe + "\"," + harga);
                    writer.println();
                }

                JOptionPane.showMessageDialog(this, "Laporan berhasil diekspor:\n" + fileToSave.getAbsolutePath());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal mengekspor laporan: " + e.getMessage());
            }
        }
    }
    private void resetForm() {
        txtMerk.setText("");
        txtTipe.setText("");
        txtHarga.setText("");
        txtSearch.setText("");
        selectedLaptopId = -1;
        tableLaptop.clearSelection();
        btnSimpan.setEnabled(true);
        btnUpdate.setEnabled(false);
        loadDataLaptop();
    }

    private void logout() {
        new LoginForm().setVisible(true);
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LaptopForm("ADMIN").setVisible(true);
        });
    }
}