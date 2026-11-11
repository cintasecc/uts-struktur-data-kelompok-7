
package id.putusan.dashboard.util;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.putusan.dashboard.model.Putusan;

public class PutusanPdfParser {

    public Putusan parse(
            PdfExtractor.PageExtraction extraction,
            String caseId,
            String terdakwaKode) {

        if (extraction == null) {
            throw new IllegalArgumentException(
                    "Hasil ekstraksi tidak boleh null");
        }

        String fullText = extraction.fullText();

        String page2 = getPage(extraction, 2);
        String page33 = getPage(extraction, 33);
        String page34 = getPage(extraction, 34);

        String nomorPerkara = find(fullText,
                "(?i)([0-9]+/Pid\\.Sus/[0-9]{4}/PN\\s+[A-Za-z]+)");

        String pengadilan = find(page34,
                "(?i)(Pengadilan Negeri\\s+[A-Za-z ]+?),\\s*pada hari");

        LocalDate tanggalPutusan = findDecisionDate(page34);

        String jenisNarkotika = find(fullText,
                "(?i)narkotika jenis\\s+(shabu|sabu|ganja|ekstasi)");

        Double berat = findDouble(fullText,
                "(?i)berat bersihnya\\s+([0-9]+(?:[,.][0-9]+)?)\\s*gram");

        String pasalDakwaan = findCharges(extraction);

        String pasalTerbukti = find(page33,
                "(?i)Memperhatikan\\s+(?:ketentuan\\s+)?"
                + "(Pasal\\s*112\\s*Ayat\\s*\\(2\\))");

        Integer tuntutanBulan = findYears(page2);

        Integer vonisBulan = findYears(
                after(page33, "M E N G A D I L I"));

        Long denda = findLong(
                after(page33, "M E N G A D I L I"),
                "(?i)denda\\s+sebesar\\s*Rp\\.?\\s*([0-9.]+)");

        return new Putusan(
                caseId,
                nomorPerkara,
                pengadilan,
                tanggalPutusan,
                terdakwaKode,
                jenisNarkotika,
                berat,
                pasalDakwaan,
                pasalTerbukti,
                tuntutanBulan,
                vonisBulan,
                denda,
                null,
                null,
                null,
                null
        );
    }

    private String getPage(
            PdfExtractor.PageExtraction extraction,
            int pageNumber) {

        return extraction.pageTexts().stream()
                .filter(page -> page.pageNumber() == pageNumber)
                .map(PdfExtractor.PageText::text)
                .findFirst()
                .orElse("");
    }

    private String find(String text, String regex) {
        Matcher matcher = Pattern.compile(
                regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL)
                .matcher(text);

        return matcher.find() ? matcher.group(1).trim() : null;
    }

    private String findCharges(
            PdfExtractor.PageExtraction extraction) {

        Set<String> charges = new LinkedHashSet<>();

        Pattern pattern = Pattern.compile(
                "(?i)Pasal\\s*(\\d+)\\s*Ayat\\s*\\((\\d+)\\)");

        for (PdfExtractor.PageText page : extraction.pageTexts()) {

            int number = page.pageNumber();

            // Bagian dakwaan pada dokumen contoh berada di halaman 3-8.
            if (number < 3 || number > 8) {
                continue;
            }

            Matcher matcher = pattern.matcher(page.text());

            while (matcher.find()) {
                charges.add(
                        "Pasal " + matcher.group(1)
                        + " ayat (" + matcher.group(2) + ")");
            }
        }

        return charges.isEmpty()
                ? null
                : String.join("; ", charges);
    }

    private Integer findYears(String text) {

        Matcher matcher = Pattern.compile(
                "(?i)pidana\\s+penjara\\s+selama\\s+"
                + "(\\d+)\\s*\\(([^)]+)\\)\\s*tahun")
                .matcher(text);

        if (!matcher.find()) {
            return null;
        }

        try {
            return Integer.parseInt(matcher.group(1)) * 12;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private LocalDate findDecisionDate(String text) {

    String normalized = text.replaceAll("\\s+", " ").trim();

    Pattern pattern = Pattern.compile(
            "(?i)Demikian\\s+diputuskan\\s+dalam\\s+sidang"
            + ".*?tanggal\\s+(\\d{1,2})\\s+"
            + "(Januari|Februari|Maret|April|Mei|Juni|"
            + "Juli|Agustus|September|Oktober|November|Desember)"
            + "\\s+(\\d{4})"
    );

    Matcher matcher = pattern.matcher(normalized);

    if (!matcher.find()) {
        return null;
    }

    Map<String, Integer> months = Map.ofEntries(
            Map.entry("januari", 1),
            Map.entry("februari", 2),
            Map.entry("maret", 3),
            Map.entry("april", 4),
            Map.entry("mei", 5),
            Map.entry("juni", 6),
            Map.entry("juli", 7),
            Map.entry("agustus", 8),
            Map.entry("september", 9),
            Map.entry("oktober", 10),
            Map.entry("november", 11),
            Map.entry("desember", 12)
    );

    try {
        int day = Integer.parseInt(matcher.group(1));
        int month = months.get(
                matcher.group(2).toLowerCase()
        );
        int year = Integer.parseInt(matcher.group(3));

        return LocalDate.of(year, month, day);

    } catch (RuntimeException e) {
        return null;
    }
}


    private String after(String text, String anchor) {
        int index = text.toLowerCase()
                .indexOf(anchor.toLowerCase());

        return index < 0 ? "" : text.substring(index + anchor.length());
    }

    private Double findDouble(String text, String regex) {
        String value = find(text, regex);

        if (value == null) {
            return null;
        }

        try {
            return Double.parseDouble(value.replace(",", "."));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Long findLong(String text, String regex) {
        String value = find(text, regex);

        if (value == null) {
            return null;
        }

        try {
            return Long.parseLong(value.replace(".", ""));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
