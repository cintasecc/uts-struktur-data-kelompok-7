
package id.putusan.dashboard.util;

import java.nio.file.Path;

public class PdfPreview {

    public static void main(String[] args) throws Exception {

        Path pdf = Path.of(
            "data/pdfs/Narkotika/putusan_1003_pid.sus_2023_pn_pbr_20241106135730.pdf"
        );

        PdfExtractor extractor = new PdfExtractor();

        PdfExtractor.PageExtraction result =
                extractor.extractWithPages(pdf);

        for (PdfExtractor.PageText page : result.pageTexts()) {

            int number = page.pageNumber();

if (number >= 2 && number <= 8) {

                System.out.println(
                    "\n========== HALAMAN " + number + " =========="
                );

                System.out.println(page.text());
            }
        }
    }
}
