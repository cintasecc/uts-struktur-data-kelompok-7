package id.putusan.dashboard.util;
import org.apache.pdfbox.Loader;import org.apache.pdfbox.pdmodel.PDDocument;import org.apache.pdfbox.text.PDFTextStripper;import java.io.IOException;import java.nio.file.Path;
/** Ekstraksi teks PDF. Tidak melakukan OCR dan tidak menebak atribut yang tidak ditemukan. */
public class PdfExtractor {
 public record Extraction(String sourceFile,String text,int pages,boolean hasText){}
 public Extraction extract(Path path)throws IOException{try(PDDocument doc=Loader.loadPDF(path.toFile())){PDFTextStripper s=new PDFTextStripper();String text=s.getText(doc);return new Extraction(path.getFileName().toString(),text,doc.getNumberOfPages(),text!=null&&!text.isBlank());}}
}
