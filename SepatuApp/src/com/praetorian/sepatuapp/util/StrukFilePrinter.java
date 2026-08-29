package com.praetorian.sepatuapp.util;

import com.praetorian.sepatuapp.model.Struk;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

public class StrukFilePrinter {

    private static final String OUTPUT_FOLDER = "struk_output";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static String cetak(Struk struk) throws IOException {
        Path folder = Path.of(OUTPUT_FOLDER);
        if (!Files.exists(folder)) {
            Files.createDirectories(folder);
        }

        Path filePath = folder.resolve("struk_" + struk.getStrukId() + ".txt");

        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("            TOKO SEPATU PRAETORIAN       \n");
        sb.append("========================================\n");
        sb.append("No. Struk   : ").append(struk.getStrukId()).append("\n");
        sb.append("Tanggal     : ").append(struk.getTanggalTransaksi().format(FORMATTER)).append("\n");
        sb.append("----------------------------------------\n");
        sb.append("Kode Sepatu : ").append(struk.getKodeSepatu()).append("\n");
        sb.append("Model       : ").append(struk.getModelSepatu()).append("\n");
        sb.append("Merk        : ").append(struk.getMerkSepatu()).append("\n");
        sb.append("Warna       : ").append(struk.getWarnaSepatu()).append("\n");
        sb.append("Harga Satuan: Rp ").append(formatRupiah(struk.getHargaSepatu())).append("\n");
        sb.append("Kuantitas   : ").append(struk.getKuantitasSepatu()).append("\n");
        sb.append("----------------------------------------\n");
        sb.append("Total Harga : Rp ").append(formatRupiah(struk.getTotalHarga())).append("\n");
        sb.append("Dibayar     : Rp ").append(formatRupiah(struk.getUangPembayaran())).append("\n");
        sb.append("Kembalian   : Rp ").append(formatRupiah(struk.getKembalian())).append("\n");
        sb.append("========================================\n");
        sb.append("        Terima kasih telah berbelanja!   \n");
        sb.append("========================================\n");

        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            writer.write(sb.toString());
        }

        return filePath.toAbsolutePath().toString();
    }

    private static String formatRupiah(int value) {
        return String.format("%,d", value).replace(',', '.');
    }
}
