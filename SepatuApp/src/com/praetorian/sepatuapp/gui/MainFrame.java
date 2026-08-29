package com.praetorian.sepatuapp.gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final PanelProdukSepatu panelProdukSepatu;
    private final PanelTransaksi panelTransaksi;
    private final PanelHistoryTransaksi panelHistoryTransaksi;

    public MainFrame() {
        setTitle("Aplikasi Kasir Toko Sepatu - Bootcamp Calon Praetorian 2026");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 650);
        setMinimumSize(new Dimension(850, 600));
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 13));

        panelProdukSepatu = new PanelProdukSepatu();
        panelTransaksi = new PanelTransaksi();
        panelHistoryTransaksi = new PanelHistoryTransaksi();

        tabbedPane.addTab("  Data Sepatu  ", panelProdukSepatu);
        tabbedPane.addTab("  Transaksi  ", panelTransaksi);
        tabbedPane.addTab("  History Transaksi  ", panelHistoryTransaksi);

        // Selalu refresh data setiap kali pindah tab, supaya data selalu sinkron
        tabbedPane.addChangeListener(e -> {
            int index = tabbedPane.getSelectedIndex();
            switch (index) {
                case 0 -> panelProdukSepatu.muatData();
                case 1 -> panelTransaksi.muatDaftarSepatu();
                case 2 -> panelHistoryTransaksi.muatData();
                default -> {
                }
            }
        });

        add(tabbedPane);
    }
}
