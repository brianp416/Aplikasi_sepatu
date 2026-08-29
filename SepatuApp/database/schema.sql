CREATE DATABASE IF NOT EXISTS db_sepatu;
USE db_sepatu;

-- Tabel produk
CREATE TABLE IF NOT EXISTS sepatu (
    kode_sepatu   VARCHAR(10)  PRIMARY KEY,
    model_sepatu  VARCHAR(100) NOT NULL,
    merk_sepatu   VARCHAR(100) NOT NULL,
    warna_sepatu  VARCHAR(100) NOT NULL,
    harga_sepatu  INT          NOT NULL
);

-- riwayat transaksi
CREATE TABLE IF NOT EXISTS struk (
    struk_id           VARCHAR(10)  PRIMARY KEY,
    kode_sepatu        VARCHAR(10)  NOT NULL,
    model_sepatu       VARCHAR(100) NOT NULL,
    merk_sepatu        VARCHAR(100) NOT NULL,
    warna_sepatu       VARCHAR(100) NOT NULL,
    harga_sepatu       INT          NOT NULL,
    kuantitas_sepatu   INT          NOT NULL,
    total_harga        INT          NOT NULL,
    uang_pembayaran    INT          NOT NULL,
    kembalian          INT          NOT NULL,
    tanggal_transaksi  DATETIME     NOT NULL,
    CONSTRAINT fk_struk_sepatu FOREIGN KEY (kode_sepatu) REFERENCES sepatu(kode_sepatu)
);

INSERT INTO sepatu (kode_sepatu, model_sepatu, merk_sepatu, warna_sepatu, harga_sepatu) VALUES
('N001', 'Air Force 1', 'Nike', 'Putih', 1200000),
('A001', 'Ultraboost', 'Adidas', 'Hitam', 2100000),
('C001', 'Chuck Taylor', 'Converse', 'Merah', 750000);
