package id.putusan.dashboard.service;
import id.putusan.dashboard.ds.SinglyLinkedList;import id.putusan.dashboard.model.Putusan;
/** Agregasi statistik dasar tanpa Stream API. */
public class StatistikService {
 public String summary(SinglyLinkedList<Putusan> d){int total=d.size(),known=0;double sum=0;int max=0;for(int i=0;i<total;i++){Integer v=d.get(i).getVonisBulan();if(v!=null){known++;sum+=v;if(v>max)max=v;}}return "Total putusan: "+total+"\nVonis tersedia: "+known+"\nRata-rata vonis: "+(known==0?"—":String.format(java.util.Locale.ROOT,"%.2f bulan",sum/known))+"\nVonis maksimum: "+(known==0?"—":max+" bulan");}
}
