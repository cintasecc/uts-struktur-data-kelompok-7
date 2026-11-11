
package id.putusan.dashboard.util;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.putusan.dashboard.model.Putusan;

public class PdfParserPreview {

    public static void main(String[] args) throws Exception {

        Path pdf = Path.of(
            "data/pdfs/Narkotika/"
            + "putusan_1003_pid.sus_2023_pn_pbr_20241106135730.pdf"
        );

        PdfExtractor extractor = new PdfExtractor();
        PutusanPdfParser parser = new PutusanPdfParser();

        var extraction = extractor.extractWithPages(pdf);

        Pattern pattern = Pattern.compile(
            "(?i)Pasal\\s*\\d+\\s*Ayat\\s*\\(\\d+\\)"
        );

        // Periksa halaman 2.
        System.out.println("=== TEKS HALAMAN 2 ===");

        extraction.pageTexts().stream()
            .filter(page -> page.pageNumber() == 2)
            .forEach(page -> System.out.println(page.text()));

        // Kandidat pasal halaman 3-8.
        System.out.println("\n=== KANDIDAT PASAL HALAMAN 3-8 ===");

        extraction.pageTexts().stream()
            .filter(page -> page.pageNumber() >= 3
                         && page.pageNumber() <= 8)
            .forEach(page -> {
                Matcher matcher = pattern.matcher(page.text());

                while (matcher.find()) {
                    System.out.println(
                        "Halaman " + page.pageNumber()
                        + " : " + matcher.group()
                    );
                }
            });

        // Hasil parsing.
        Putusan putusan = parser.parse(
            extraction,
            "CASE-001",
            "TERDAKWA-001"
        );

        System.out.println("\n=== HASIL PARSING PUTUSAN ===");
        System.out.println("Nomor Perkara       : " + putusan.getNomorPerkara());
        System.out.println("Pengadilan          : " + putusan.getPengadilan());
        System.out.println("Tanggal Putusan     : " + putusan.getTanggalPutusan());
        System.out.println("Jenis Narkotika     : " + putusan.getJenisNarkotika());
        System.out.println("Berat Barang Bukti  : " + putusan.getBeratBarangBuktiGram());
        System.out.println("Pasal Dakwaan       : " + putusan.getPasalDakwaan());
        System.out.println("Pasal Terbukti      : " + putusan.getPasalTerbukti());
        System.out.println("Tuntutan (bulan)    : " + putusan.getTuntutanBulan());
        System.out.println("Vonis (bulan)       : " + putusan.getVonisBulan());
        System.out.println("Denda               : " + putusan.getDendaRupiah());
    }
}
