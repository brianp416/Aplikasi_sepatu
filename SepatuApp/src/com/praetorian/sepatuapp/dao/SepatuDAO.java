package com.praetorian.sepatuapp.dao;

import com.praetorian.sepatuapp.db.DatabaseConnection;
import com.praetorian.sepatuapp.model.Sepatu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SepatuDAO {
    public List<Sepatu> getAllSepatu() throws SQLException {
        List<Sepatu> list = new ArrayList<>();
        String sql = "SELECT * FROM sepatu ORDER BY kode_sepatu";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public Sepatu getByKode(String kodeSepatu) throws SQLException {
        String sql = "SELECT * FROM sepatu WHERE kode_sepatu = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kodeSepatu);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public Sepatu insertSepatu(Sepatu sepatu) throws SQLException {
        String kodeBaru = generateKodeSepatu(sepatu.getMerkSepatu());
        sepatu.setKodeSepatu(kodeBaru);

        String sql = "INSERT INTO sepatu (kode_sepatu, model_sepatu, merk_sepatu, warna_sepatu, harga_sepatu) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sepatu.getKodeSepatu());
            ps.setString(2, sepatu.getModelSepatu());
            ps.setString(3, sepatu.getMerkSepatu());
            ps.setString(4, sepatu.getWarnaSepatu());
            ps.setInt(5, sepatu.getHargaSepatu());
            ps.executeUpdate();
        }
        return sepatu;
    }

    public void updateSepatu(Sepatu sepatu) throws SQLException {
        String sql = "UPDATE sepatu SET model_sepatu = ?, merk_sepatu = ?, warna_sepatu = ?, harga_sepatu = ? " +
                "WHERE kode_sepatu = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sepatu.getModelSepatu());
            ps.setString(2, sepatu.getMerkSepatu());
            ps.setString(3, sepatu.getWarnaSepatu());
            ps.setInt(4, sepatu.getHargaSepatu());
            ps.setString(5, sepatu.getKodeSepatu());
            ps.executeUpdate();
        }
    }

    public void deleteSepatu(String kodeSepatu) throws SQLException {
        String sql = "DELETE FROM sepatu WHERE kode_sepatu = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kodeSepatu);
            ps.executeUpdate();
        }
    }

    private String generateKodeSepatu(String merkSepatu) throws SQLException {
        if (merkSepatu == null || merkSepatu.isBlank()) {
            throw new IllegalArgumentException("Merk sepatu tidak boleh kosong.");
        }
        String prefix = merkSepatu.trim().substring(0, 1).toUpperCase();

        String sql = "SELECT kode_sepatu FROM sepatu WHERE kode_sepatu LIKE ? ORDER BY kode_sepatu DESC LIMIT 1";
        int nextNumber = 1;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, prefix + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String lastKode = rs.getString("kode_sepatu");
                    String numberPart = lastKode.substring(1);
                    try {
                        nextNumber = Integer.parseInt(numberPart) + 1;
                    } catch (NumberFormatException e) {
                        nextNumber = 1;
                    }
                }
            }
        }
        return String.format("%s%03d", prefix, nextNumber);
    }

    private Sepatu mapRow(ResultSet rs) throws SQLException {
        Sepatu s = new Sepatu();
        s.setKodeSepatu(rs.getString("kode_sepatu"));
        s.setModelSepatu(rs.getString("model_sepatu"));
        s.setMerkSepatu(rs.getString("merk_sepatu"));
        s.setWarnaSepatu(rs.getString("warna_sepatu"));
        s.setHargaSepatu(rs.getInt("harga_sepatu"));
        return s;
    }
}
