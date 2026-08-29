package com.praetorian.sepatuapp.gui;

import com.praetorian.sepatuapp.dao.StrukDAO;
import com.praetorian.sepatuapp.model.Struk;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PanelHistoryTransaksi extends JPanel {

    private final StrukDAO strukDAO = new StrukDAO();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private JTable table;
    private DefaultTableModel tableModel;

    public PanelHistoryTransaksi() {
        setLayout(new BorderLayout(12, 12));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);

        String[] kolom = {"No. Struk", "Kode Sepatu", "Model", "Merk", "Warna",
                "Harga Satuan", "Qty", "Total Harga", "Dibayar", "Kembalian", "Tanggal"};
        tableModel = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(26);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                "Riwayat Transaksi", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("SansSerif", Font.BOLD, 13)));

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(new Color(70, 130, 180));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.addActionListener(e -> muatData());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.add(btnRefresh);

        add(top, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        muatData();
    }

    public void muatData() {
        try {
            tableModel.setRowCount(0);
            List<Struk> daftar = strukDAO.getAllStruk();
            for (Struk s : daftar) {
                tableModel.addRow(new Object[]{
                        s.getStrukId(), s.getKodeSepatu(), s.getModelSepatu(), s.getMerkSepatu(),
                        s.getWarnaSepatu(), s.getHargaSepatu(), s.getKuantitasSepatu(),
                        s.getTotalHarga(), s.getUangPembayaran(), s.getKembalian(),
                        s.getTanggalTransaksi() != null ? s.getTanggalTransaksi().format(formatter) : ""
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat riwayat transaksi:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
