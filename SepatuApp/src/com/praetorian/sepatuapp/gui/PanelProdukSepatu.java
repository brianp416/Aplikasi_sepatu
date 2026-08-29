package com.praetorian.sepatuapp.gui;

import com.praetorian.sepatuapp.dao.SepatuDAO;
import com.praetorian.sepatuapp.model.Sepatu;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PanelProdukSepatu extends JPanel {

    private final SepatuDAO sepatuDAO = new SepatuDAO();

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtKode;
    private JTextField txtModel;
    private JTextField txtMerk;
    private JTextField txtWarna;
    private JTextField txtHarga;

    private JButton btnTambah;
    private JButton btnUpdate;
    private JButton btnHapus;
    private JButton btnBersihkan;

    public PanelProdukSepatu() {
        setLayout(new BorderLayout(12, 12));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);

        add(buildTablePanel(), BorderLayout.CENTER);
        add(buildFormPanel(), BorderLayout.SOUTH);

        muatData();
    }

    private JComponent buildTablePanel() {
        String[] kolom = {"Kode Sepatu", "Model Sepatu", "Merk Sepatu", "Warna Sepatu", "Harga Sepatu"};
        tableModel = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(26);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                populateFormFromTable();
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                "Daftar Produk Sepatu", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("SansSerif", Font.BOLD, 13)));
        return scrollPane;
    }

    private JComponent buildFormPanel() {
        JPanel wrapper = new JPanel(new BorderLayout(10, 10));
        wrapper.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                "Form Produk Sepatu", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("SansSerif", Font.BOLD, 13)));

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBorder(new EmptyBorder(10, 10, 10, 10));

        txtKode = new JTextField();
        txtKode.setEditable(false);
        txtKode.setBackground(new Color(240, 240, 240));
        txtModel = new JTextField();
        txtMerk = new JTextField();
        txtWarna = new JTextField();
        txtHarga = new JTextField();

        form.add(new JLabel("Kode Sepatu (auto):"));
        form.add(txtKode);
        form.add(new JLabel("Model Sepatu:"));
        form.add(txtModel);
        form.add(new JLabel("Merk Sepatu:"));
        form.add(txtMerk);
        form.add(new JLabel("Warna Sepatu:"));
        form.add(txtWarna);
        form.add(new JLabel("Harga Sepatu:"));
        form.add(txtHarga);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnTambah = new JButton("Tambah");
        btnUpdate = new JButton("Update");
        btnHapus = new JButton("Hapus");
        btnBersihkan = new JButton("Bersihkan Form");

        styleButton(btnTambah, new Color(46, 139, 87));
        styleButton(btnUpdate, new Color(70, 130, 180));
        styleButton(btnHapus, new Color(178, 34, 34));
        styleButton(btnBersihkan, new Color(128, 128, 128));

        btnTambah.addActionListener(e -> tambahProduk());
        btnUpdate.addActionListener(e -> updateProduk());
        btnHapus.addActionListener(e -> hapusProduk());
        btnBersihkan.addActionListener(e -> bersihkanForm());

        buttonPanel.add(btnTambah);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnBersihkan);

        wrapper.add(form, BorderLayout.CENTER);
        wrapper.add(buttonPanel, BorderLayout.SOUTH);
        return wrapper;
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(140, 32));
    }

    public void muatData() {
        try {
            tableModel.setRowCount(0);
            List<Sepatu> daftar = sepatuDAO.getAllSepatu();
            for (Sepatu s : daftar) {
                tableModel.addRow(new Object[]{
                        s.getKodeSepatu(), s.getModelSepatu(), s.getMerkSepatu(),
                        s.getWarnaSepatu(), s.getHargaSepatu()
                });
            }
        } catch (SQLException e) {
            tampilkanError("Gagal memuat data sepatu", e);
        }
    }

    private void populateFormFromTable() {
        int row = table.getSelectedRow();
        txtKode.setText(String.valueOf(tableModel.getValueAt(row, 0)));
        txtModel.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        txtMerk.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        txtWarna.setText(String.valueOf(tableModel.getValueAt(row, 3)));
        txtHarga.setText(String.valueOf(tableModel.getValueAt(row, 4)));
    }

    private void tambahProduk() {
        if (!validasiForm(false)) return;
        try {
            Sepatu sepatu = new Sepatu();
            sepatu.setModelSepatu(txtModel.getText().trim());
            sepatu.setMerkSepatu(txtMerk.getText().trim());
            sepatu.setWarnaSepatu(txtWarna.getText().trim());
            sepatu.setHargaSepatu(Integer.parseInt(txtHarga.getText().trim()));

            Sepatu tersimpan = sepatuDAO.insertSepatu(sepatu);
            JOptionPane.showMessageDialog(this,
                    "Produk berhasil ditambahkan dengan kode: " + tersimpan.getKodeSepatu(),
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            muatData();
            bersihkanForm();
        } catch (SQLException e) {
            tampilkanError("Gagal menambahkan produk", e);
        }
    }

    private void updateProduk() {
        if (txtKode.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Pilih dulu produk yang ingin di-update dari tabel.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validasiForm(true)) return;
        try {
            Sepatu sepatu = new Sepatu();
            sepatu.setKodeSepatu(txtKode.getText().trim());
            sepatu.setModelSepatu(txtModel.getText().trim());
            sepatu.setMerkSepatu(txtMerk.getText().trim());
            sepatu.setWarnaSepatu(txtWarna.getText().trim());
            sepatu.setHargaSepatu(Integer.parseInt(txtHarga.getText().trim()));

            sepatuDAO.updateSepatu(sepatu);
            JOptionPane.showMessageDialog(this, "Produk berhasil diperbarui.",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            muatData();
            bersihkanForm();
        } catch (SQLException e) {
            tampilkanError("Gagal memperbarui produk", e);
        }
    }

    private void hapusProduk() {
        if (txtKode.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Pilih dulu produk yang ingin dihapus dari tabel.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Yakin ingin menghapus produk " + txtKode.getText() + "?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (konfirmasi != JOptionPane.YES_OPTION) return;

        try {
            sepatuDAO.deleteSepatu(txtKode.getText().trim());
            JOptionPane.showMessageDialog(this, "Produk berhasil dihapus.",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            muatData();
            bersihkanForm();
        } catch (SQLException e) {
            tampilkanError("Gagal menghapus produk (mungkin produk ini sudah punya riwayat transaksi)", e);
        }
    }

    private void bersihkanForm() {
        txtKode.setText("");
        txtModel.setText("");
        txtMerk.setText("");
        txtWarna.setText("");
        txtHarga.setText("");
        table.clearSelection();
    }

    private boolean validasiForm(boolean isUpdate) {
        if (txtModel.getText().isBlank() || txtMerk.getText().isBlank() || txtWarna.getText().isBlank()
                || txtHarga.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Semua field (model, merk, warna, harga) wajib diisi.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            int harga = Integer.parseInt(txtHarga.getText().trim());
            if (harga <= 0) {
                JOptionPane.showMessageDialog(this, "Harga sepatu harus lebih besar dari 0.",
                        "Peringatan", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga sepatu harus berupa angka.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void tampilkanError(String pesan, Exception e) {
        JOptionPane.showMessageDialog(this, pesan + ":\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
