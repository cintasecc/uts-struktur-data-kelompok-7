package id.putusan.dashboard.util;
import id.putusan.dashboard.model.Putusan;import java.io.*;import java.nio.charset.StandardCharsets;import java.nio.file.*;import java.util.*;
/** Penulis CSV dengan escaping tanda kutip. */
public class CsvWriter {
 public void write(Path path,List<Putusan> rows)throws IOException{try(BufferedWriter w=Files.newBufferedWriter(path,StandardCharsets.UTF_8)){w.write("caseId,nomorPerkara,pengadilan,tanggalPutusan,terdakwaKode,jenisNarkotika,beratBarangBuktiGram,pasalDakwaan,pasalTerbukti,tuntutanBulan,vonisBulan,dendaRupiah,barangBukti,keadaanMemberatkan,keadaanMeringankan,sourcePages");w.newLine();for(Putusan p:rows){String[] v={p.getCaseId(),p.getNomorPerkara(),p.getPengadilan(),str(p.getTanggalPutusan()),p.getTerdakwaKode(),p.getJenisNarkotika(),str(p.getBeratBarangBuktiGram()),p.getPasalDakwaan(),p.getPasalTerbukti(),str(p.getTuntutanBulan()),str(p.getVonisBulan()),str(p.getDendaRupiah()),p.getBarangBukti(),p.getKeadaanMemberatkan(),p.getKeadaanMeringankan(),p.getSourcePages()};for(int i=0;i<v.length;i++){if(i>0)w.write(',');w.write(quote(v[i]));}w.newLine();}}}
 private String str(Object o){return o==null?"":o.toString();} private String quote(String s){if(s==null)return "";return "\""+s.replace("\"","\"\"")+"\"";}
}
