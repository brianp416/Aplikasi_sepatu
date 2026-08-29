package com.praetorian.sepatuapp.gui;

import com.praetorian.sepatuapp.dao.SepatuDAO;
import com.praetorian.sepatuapp.dao.StrukDAO;
import com.praetorian.sepatuapp.model.Sepatu;
import com.praetorian.sepatuapp.model.Struk;
import com.praetorian.sepatuapp.util.StrukFilePrinter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PanelTransaksi extends JPanel {

    private final SepatuDAO sepatuDAO = new SepatuDAO();
    private final StrukDAO strukDAO = new StrukDAO();

    private JTable table;
    private DefaultTableModel tableModel;

    private Sepatu sepatuTerpilih;

    // Form disabled (data produk)
    private JTextField txtKode;
    private JTextField txtModel;
    private JTextField txtMerk;
    private JTextField txtWarna;
    private JTextField txtHarga;

    // Form enabled (input transaksi)
    private JTextField txtKuantitas;
    private JTextField txtUangBayar;

    private JLabel lblTotalHarga;

    private JButton btnCetakStruk;
    private JButton btnRefresh;

    public PanelTransaksi() {
        setLayout(new BorderLayout(12, 12));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);

        add(buildTablePanel(), BorderLayout.CENTER);
        add(buildFormPanel(), BorderLayout.SOUTH);

        muatDaftarSepatu();
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
                pilihSepatu();
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                "Pilih Produk Sepatu (klik baris untuk memilih)", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, new Font("SansSerif", Font.BOLD, 13)));
        return scrollPane;
    }

    private JComponent buildFormPanel() {
        JPanel wrapper = new JPanel(new BorderLayout(10, 10));
        wrapper.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                "Form Transaksi", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("SansSerif", Font.BOLD, 13)));

        JPanel form = new JPanel(new GridLayout(1, 2, 20, 0));
        form.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Kolom kiri: data produk (disabled)
        JPanel kiri = new JPanel(new GridLayout(5, 2, 8, 8));
        txtKode = new JTextField();
        txtModel = new JTextField();
        txtMerk = new JTextField();
        txtWarna = new JTextField();
        txtHarga = new JTextField();
        for (JTextField tf : new JTextField[]{txtKode, txtModel, txtMerk, txtWarna, txtHarga}) {
            tf.setEditable(false);
            tf.setBackground(new Color(240, 240, 240));
        }
        kiri.add(new JLabel("Kode Sepatu:"));
        kiri.add(txtKode);
        kiri.add(new JLabel("Model Sepatu:"));
        kiri.add(txtModel);
        kiri.add(new JLabel("Merk Sepatu:"));
        kiri.add(txtMerk);
        kiri.add(new JLabel("Warna Sepatu:"));
        kiri.add(txtWarna);
        kiri.add(new JLabel("Harga Sepatu:"));
        kiri.add(txtHarga);

        // Kolom kanan: input transaksi (enabled)
        JPanel kanan = new JPanel(new GridLayout(4, 2, 8, 8));
        txtKuantitas = new JTextField();
        txtUangBayar = new JTextField();
        lblTotalHarga = new JLabel("Rp 0");
        lblTotalHarga.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTotalHarga.setForeground(new Color(46, 139, 87));

        DocumentListener hitungListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                hitungTotal();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                hitungTotal();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                hitungTotal();
            }
        };
        txtKuantitas.getDocument().addDocumentListener(hitungListener);

        kanan.add(new JLabel("Kuantitas Sepatu:"));
        kanan.add(txtKuantitas);
        kanan.add(new JLabel("Uang Dibayarkan:"));
        kanan.add(txtUangBayar);
        kanan.add(new JLabel("Total Harga:"));
        kanan.add(lblTotalHarga);

        form.add(kiri);
        form.add(kanan);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnCetakStruk = new JButton("Cetak Struk");
        btnRefresh = new JButton("Refresh Daftar Produk");

        btnCetakStruk.setBackground(new Color(46, 139, 87));
        btnCetakStruk.setForeground(Color.WHITE);
        btnCetakStruk.setFocusPainted(false);
        btnCetakStruk.setPreferredSize(new Dimension(160, 34));

        btnRefresh.setBackground(new Color(70, 130, 180));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setPreferredSize(new Dimension(200, 34));

        btnCetakStruk.addActionListener(e -> prosesTransaksi());
        btnRefresh.addActionListener(e -> muatDaftarSepatu());

        buttonPanel.add(btnCetakStruk);
        buttonPanel.add(btnRefresh);

        wrapper.add(form, BorderLayout.CENTER);
        wrapper.add(buttonPanel, BorderLayout.SOUTH);
        return wrapper;
    }

    public void muatDaftarSepatu() {
        try {
            tableModel.setRowCount(0);
            List<Sepatu> daftar = sepatuDAO.getAllSepatu();
            for (Sepatu s : daftar) {
                tableModel.addRow(new Object[]{
                        s.getKodeSepatu(), s.getModelSepatu(), s.getMerkSepatu(),
                        s.getWarnaSepatu(), s.getHargaSepatu()
                });
            }
            bersihkanForm();
        } catch (SQLException e) {
            tampilkanError("Gagal memuat daftar sepatu", e);
        }
    }

    private void pilihSepatu() {
        int row = table.getSelectedRow();
        String kode = String.valueOf(tableModel.getValueAt(row, 0));
        try {
            sepatuTerpilih = sepatuDAO.getByKode(kode);
            if (sepatuTerpilih == null) return;
            txtKode.setText(sepatuTerpilih.getKodeSepatu());
            txtModel.setText(sepatuTerpilih.getModelSepatu());
            txtMerk.setText(sepatuTerpilih.getMerkSepatu());
            txtWarna.setText(sepatuTerpilih.getWarnaSepatu());
            txtHarga.setText(String.valueOf(sepatuTerpilih.getHargaSepatu()));
            txtKuantitas.setText("");
            txtUangBayar.setText("");
            hitungTotal();
        } catch (SQLException e) {
            tampilkanError("Gagal mengambil detail sepatu", e);
        }
    }

    private void hitungTotal() {
        if (sepatuTerpilih == null) {
            lblTotalHarga.setText("Rp 0");
            return;
        }
        try {
            int kuantitas = txtKuantitas.getText().isBlank() ? 0 : Integer.parseInt(txtKuantitas.getText().trim());
            int total = kuantitas * sepatuTerpilih.getHargaSepatu();
            lblTotalHarga.setText("Rp " + formatRupiah(total));
        } catch (NumberFormatException e) {
            lblTotalHarga.setText("Rp 0");
        }
    }

    private void prosesTransaksi() {
        if (sepatuTerpilih == null) {
            JOptionPane.showMessageDialog(this, "Pilih dulu produk sepatu dari tabel.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int kuantitas;
        int uangBayar;
        try {
            kuantitas = Integer.parseInt(txtKuantitas.getText().trim());
            if (kuantitas <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Kuantitas harus berupa angka lebih besar dari 0.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            uangBayar = Integer.parseInt(txtUangBayar.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Uang dibayarkan harus berupa angka.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int total = kuantitas * sepatuTerpilih.getHargaSepatu();
        if (uangBayar < total) {
            JOptionPane.showMessageDialog(this,
                    "Uang yang dibayarkan (Rp " + formatRupiah(uangBayar) + ") kurang dari total harga (Rp "
                            + formatRupiah(total) + ").",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Struk struk = new Struk();
        struk.setKodeSepatu(sepatuTerpilih.getKodeSepatu());
        struk.setModelSepatu(sepatuTerpilih.getModelSepatu());
        struk.setMerkSepatu(sepatuTerpilih.getMerkSepatu());
        struk.setWarnaSepatu(sepatuTerpilih.getWarnaSepatu());
        struk.setHargaSepatu(sepatuTerpilih.getHargaSepatu());
        struk.setKuantitasSepatu(kuantitas);
        struk.setTotalHarga(total);
        struk.setUangPembayaran(uangBayar);
        struk.setKembalian(uangBayar - total);

        try {
            Struk tersimpan = strukDAO.insertStruk(struk);
            String pathFile = StrukFilePrinter.cetak(tersimpan);

            JOptionPane.showMessageDialog(this,
                    "Transaksi berhasil disimpan!\n" +
                            "No. Struk : " + tersimpan.getStrukId() + "\n" +
                            "Total     : Rp " + formatRupiah(tersimpan.getTotalHarga()) + "\n" +
                            "Kembalian : Rp " + formatRupiah(tersimpan.getKembalian()) + "\n\n" +
                            "Struk disimpan di:\n" + pathFile,
                    "Transaksi Berhasil", JOptionPane.INFORMATION_MESSAGE);

            bersihkanForm();
        } catch (SQLException e) {
            tampilkanError("Gagal menyimpan transaksi ke database", e);
        } catch (IOException e) {
            tampilkanError("Transaksi tersimpan di database, tetapi gagal mencetak file struk", e);
        }
    }

    private void bersihkanForm() {
        sepatuTerpilih = null;
        txtKode.setText("");
        txtModel.setText("");
        txtMerk.setText("");
        txtWarna.setText("");
        txtHarga.setText("");
        txtKuantitas.setText("");
        txtUangBayar.setText("");
        lblTotalHarga.setText("Rp 0");
        table.clearSelection();
    }

    private String formatRupiah(int value) {
        return String.format("%,d", value).replace(',', '.');
    }

    private void tampilkanError(String pesan, Exception e) {
        JOptionPane.showMessageDialog(this, pesan + ":\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
