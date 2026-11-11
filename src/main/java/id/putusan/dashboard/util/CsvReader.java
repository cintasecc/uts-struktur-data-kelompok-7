package id.putusan.dashboard.util;
import id.putusan.dashboard.model.Putusan;
import java.io.*;import java.nio.charset.StandardCharsets;import java.nio.file.*;import java.time.LocalDate;import java.util.*;
/** Pembaca CSV RFC-4180 sederhana yang mendukung kutip dan koma di dalam field. */
public class CsvReader {
 public List<Putusan> read(Path path) throws IOException {
  List<Putusan> out=new ArrayList<>();
  try(BufferedReader br=Files.newBufferedReader(path,StandardCharsets.UTF_8)){
   String line=br.readLine();if(line==null)return out;List<String> headers=parse(line);
   if(!headers.isEmpty() && headers.get(0).startsWith("\uFEFF")) headers.set(0,headers.get(0).substring(1));
   while((line=br.readLine())!=null){if(line.isBlank())continue;List<String> r=parse(line);if(r.size()!=headers.size())throw new IOException("Jumlah kolom tidak sesuai pada baris "+(out.size()+2));
    Map<String,String> m=new HashMap<>();for(int i=0;i<headers.size();i++)m.put(headers.get(i),blank(r.get(i)));
    out.add(new Putusan(m.get("caseId"),m.get("nomorPerkara"),m.get("pengadilan"),date(m.get("tanggalPutusan")),m.get("terdakwaKode"),m.get("jenisNarkotika"),dbl(m.get("beratBarangBuktiGram")),m.get("pasalDakwaan"),m.get("pasalTerbukti"),integer(m.get("tuntutanBulan")),integer(m.get("vonisBulan")),lng(m.get("dendaRupiah")),m.get("barangBukti"),m.get("keadaanMemberatkan"),m.get("keadaanMeringankan"),m.get("sourcePages")));
   }
  }return out;
 }
 private static String blank(String s){return s==null||s.isBlank()?null:s;}
 private static LocalDate date(String s){return s==null?null:LocalDate.parse(s);}
 private static Double dbl(String s){return s==null?null:Double.valueOf(s);}
 private static Integer integer(String s){return s==null?null:Integer.valueOf(s);}
 private static Long lng(String s){return s==null?null:Long.valueOf(s);}
 public static List<String> parse(String line)throws IOException{List<String> a=new ArrayList<>();StringBuilder b=new StringBuilder();boolean q=false;for(int i=0;i<line.length();i++){char c=line.charAt(i);if(c=='"'){if(q&&i+1<line.length()&&line.charAt(i+1)=='"'){b.append('"');i++;}else q=!q;}else if(c==','&&!q){a.add(b.toString());b.setLength(0);}else b.append(c);}if(q)throw new IOException("CSV memiliki kutip tidak tertutup");a.add(b.toString());return a;}
}
