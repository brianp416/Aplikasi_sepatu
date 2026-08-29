package com.praetorian.sepatuapp.model;

import java.time.LocalDateTime;

public class Struk {

    private String strukId;
    private String kodeSepatu;
    private String modelSepatu;
    private String merkSepatu;
    private String warnaSepatu;
    private int hargaSepatu;
    private int kuantitasSepatu;
    private int totalHarga;
    private int uangPembayaran;
    private int kembalian;
    private LocalDateTime tanggalTransaksi;

    public Struk() {
    }

    public String getStrukId() {
        return strukId;
    }

    public void setStrukId(String strukId) {
        this.strukId = strukId;
    }

    public String getKodeSepatu() {
        return kodeSepatu;
    }

    public void setKodeSepatu(String kodeSepatu) {
        this.kodeSepatu = kodeSepatu;
    }

    public String getModelSepatu() {
        return modelSepatu;
    }

    public void setModelSepatu(String modelSepatu) {
        this.modelSepatu = modelSepatu;
    }

    public String getMerkSepatu() {
        return merkSepatu;
    }

    public void setMerkSepatu(String merkSepatu) {
        this.merkSepatu = merkSepatu;
    }

    public String getWarnaSepatu() {
        return warnaSepatu;
    }

    public void setWarnaSepatu(String warnaSepatu) {
        this.warnaSepatu = warnaSepatu;
    }

    public int getHargaSepatu() {
        return hargaSepatu;
    }

    public void setHargaSepatu(int hargaSepatu) {
        this.hargaSepatu = hargaSepatu;
    }

    public int getKuantitasSepatu() {
        return kuantitasSepatu;
    }

    public void setKuantitasSepatu(int kuantitasSepatu) {
        this.kuantitasSepatu = kuantitasSepatu;
    }

    public int getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(int totalHarga) {
        this.totalHarga = totalHarga;
    }

    public int getUangPembayaran() {
        return uangPembayaran;
    }

    public void setUangPembayaran(int uangPembayaran) {
        this.uangPembayaran = uangPembayaran;
    }

    public int getKembalian() {
        return kembalian;
    }

    public void setKembalian(int kembalian) {
        this.kembalian = kembalian;
    }

    public LocalDateTime getTanggalTransaksi() {
        return tanggalTransaksi;
    }

    public void setTanggalTransaksi(LocalDateTime tanggalTransaksi) {
        this.tanggalTransaksi = tanggalTransaksi;
    }
}
