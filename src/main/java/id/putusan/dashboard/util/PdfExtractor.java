
package id.putusan.dashboard.util;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * Ekstraksi teks PDF per halaman.
 * Tidak melakukan OCR dan tidak menebak atribut yang tidak ditemukan.
 */
public class PdfExtractor {

    /**
     * Hasil ekstraksi satu halaman PDF.
     */
    public record PageText(
            int pageNumber,
            String text
    ) {}

    /**
     * Hasil ekstraksi seluruh dokumen.
     */
    public record Extraction(
            String sourceFile,
            String text,
            int pages,
            boolean hasText
    ) {}

    /**
     * Hasil ekstraksi lengkap beserta teks per halaman.
     */
    public record PageExtraction(
            String sourceFile,
            List<PageText> pageTexts,
            String fullText,
            int pages,
            boolean hasText
    ) {}

    /**
     * Method lama tetap dipertahankan
     * agar kode yang sudah menggunakan extract() tidak rusak.
     */
    public Extraction extract(Path path) throws IOException {

        PageExtraction result = extractWithPages(path);

        return new Extraction(
                result.sourceFile(),
                result.fullText(),
                result.pages(),
                result.hasText()
        );
    }

    /**
     * Mengekstrak teks PDF dan menyimpan nomor setiap halaman.
     */
    public PageExtraction extractWithPages(Path path)
            throws IOException {

        if (path == null) {
            throw new IllegalArgumentException(
                    "Path PDF tidak boleh kosong"
            );
        }

        if (!java.nio.file.Files.isRegularFile(path)) {
            throw new IOException(
                    "File PDF tidak ditemukan: " + path
            );
        }

        List<PageText> pageTexts = new ArrayList<>();
        StringBuilder fullText = new StringBuilder();

        try (PDDocument document =
                     Loader.loadPDF(path.toFile())) {

            PDFTextStripper stripper = new PDFTextStripper();

            int totalPages = document.getNumberOfPages();

            for (int page = 1; page <= totalPages; page++) {

                stripper.setStartPage(page);
                stripper.setEndPage(page);

                String pageContent = stripper.getText(document);

                if (pageContent == null) {
                    pageContent = "";
                }

                pageTexts.add(
                        new PageText(page, pageContent)
                );

                fullText.append(pageContent);

                if (page < totalPages) {
                    fullText.append("\n");
                }
            }

            String text = fullText.toString();

            boolean hasText = !text.isBlank();

            return new PageExtraction(
                    path.getFileName().toString(),
                    List.copyOf(pageTexts),
                    text,
                    totalPages,
                    hasText
            );
        }
    }
}
