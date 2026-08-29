package com.praetorian.sepatuapp.model;

public class Sepatu {

    private String kodeSepatu;
    private String modelSepatu;
    private String merkSepatu;
    private String warnaSepatu;
    private int hargaSepatu;

    public Sepatu() {
    }

    public Sepatu(String kodeSepatu, String modelSepatu, String merkSepatu,
                  String warnaSepatu, int hargaSepatu) {
        this.kodeSepatu = kodeSepatu;
        this.modelSepatu = modelSepatu;
        this.merkSepatu = merkSepatu;
        this.warnaSepatu = warnaSepatu;
        this.hargaSepatu = hargaSepatu;
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

    @Override
    public String toString() {
        return kodeSepatu + " - " + merkSepatu + " " + modelSepatu;
    }
}
