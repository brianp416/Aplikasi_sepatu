package com.praetorian.sepatuapp.dao;

import com.praetorian.sepatuapp.db.DatabaseConnection;
import com.praetorian.sepatuapp.model.Struk;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StrukDAO {

    public List<Struk> getAllStruk() throws SQLException {
        List<Struk> list = new ArrayList<>();
        String sql = "SELECT * FROM struk ORDER BY tanggal_transaksi DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public Struk insertStruk(Struk struk) throws SQLException {
        struk.setStrukId(generateStrukId());
        if (struk.getTanggalTransaksi() == null) {
            struk.setTanggalTransaksi(LocalDateTime.now());
        }

        String sql = "INSERT INTO struk (struk_id, kode_sepatu, model_sepatu, merk_sepatu, warna_sepatu, " +
                "harga_sepatu, kuantitas_sepatu, total_harga, uang_pembayaran, kembalian, tanggal_transaksi) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, struk.getStrukId());
            ps.setString(2, struk.getKodeSepatu());
            ps.setString(3, struk.getModelSepatu());
            ps.setString(4, struk.getMerkSepatu());
            ps.setString(5, struk.getWarnaSepatu());
            ps.setInt(6, struk.getHargaSepatu());
            ps.setInt(7, struk.getKuantitasSepatu());
            ps.setInt(8, struk.getTotalHarga());
            ps.setInt(9, struk.getUangPembayaran());
            ps.setInt(10, struk.getKembalian());
            ps.setTimestamp(11, Timestamp.valueOf(struk.getTanggalTransaksi()));
            ps.executeUpdate();
        }
        return struk;
    }

    private String generateStrukId() throws SQLException {
        String sql = "SELECT struk_id FROM struk ORDER BY struk_id DESC LIMIT 1";
        int nextNumber = 1;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String lastId = rs.getString("struk_id"); // format STR0001
                try {
                    nextNumber = Integer.parseInt(lastId.substring(3)) + 1;
                } catch (NumberFormatException e) {
                    nextNumber = 1;
                }
            }
        }
        return String.format("STR%04d", nextNumber);
    }

    private Struk mapRow(ResultSet rs) throws SQLException {
        Struk s = new Struk();
        s.setStrukId(rs.getString("struk_id"));
        s.setKodeSepatu(rs.getString("kode_sepatu"));
        s.setModelSepatu(rs.getString("model_sepatu"));
        s.setMerkSepatu(rs.getString("merk_sepatu"));
        s.setWarnaSepatu(rs.getString("warna_sepatu"));
        s.setHargaSepatu(rs.getInt("harga_sepatu"));
        s.setKuantitasSepatu(rs.getInt("kuantitas_sepatu"));
        s.setTotalHarga(rs.getInt("total_harga"));
        s.setUangPembayaran(rs.getInt("uang_pembayaran"));
        s.setKembalian(rs.getInt("kembalian"));
        Timestamp ts = rs.getTimestamp("tanggal_transaksi");
        if (ts != null) {
            s.setTanggalTransaksi(ts.toLocalDateTime());
        }
        return s;
    }
}
