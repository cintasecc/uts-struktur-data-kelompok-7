package id.putusan.dashboard.repository;
import id.putusan.dashboard.ds.SinglyLinkedList;import id.putusan.dashboard.model.Putusan;import id.putusan.dashboard.util.CsvReader;import id.putusan.dashboard.util.CsvWriter;
import java.io.IOException;import java.nio.file.Path;import java.util.*;
/** Repository yang menjadikan custom SinglyLinkedList sebagai penyimpanan utama runtime. */
public class PutusanRepository {
 private final SinglyLinkedList<Putusan> data=new SinglyLinkedList<>();
 public void load(Path path)throws IOException{data.clear();for(Putusan p:new CsvReader().read(path))data.addLast(p);}
 public void save(Path path)throws IOException{List<Putusan> rows=new ArrayList<>();for(int i=0;i<data.size();i++)rows.add(data.get(i));new CsvWriter().write(path,rows);}
 public SinglyLinkedList<Putusan> all(){return data;}
 public Putusan findCase(String id){for(int i=0;i<data.size();i++)if(Objects.equals(data.get(i).getCaseId(),id))return data.get(i);return null;}
 public Putusan findNumber(String n){for(int i=0;i<data.size();i++)if(Objects.equals(data.get(i).getNomorPerkara(),n))return data.get(i);return null;}
 public void add(Putusan p){if(findCase(p.getCaseId())!=null||findNumber(p.getNomorPerkara())!=null)throw new IllegalArgumentException("caseId atau nomor perkara sudah ada");data.addLast(p);}
 public void remove(int i){data.remove(i);}
}
