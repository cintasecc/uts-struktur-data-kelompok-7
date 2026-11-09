package id.putusan.dashboard.service;
import id.putusan.dashboard.model.Putusan;import id.putusan.dashboard.repository.PutusanRepository;import id.putusan.dashboard.util.ValidationUtil;import java.util.*;
/** Aturan bisnis CRUD putusan. */
public class PutusanService {
 private final PutusanRepository repo;private final AuditService audit;
 public PutusanService(PutusanRepository r,AuditService a){repo=r;audit=a;}
 public void add(Putusan p,String user){String e=ValidationUtil.validate(p);if(e!=null)throw new IllegalArgumentException(e);repo.add(p);audit.record(user,"ADD",p.getCaseId());}
 public void delete(int i,String user){String id=repo.all().get(i).getCaseId();repo.remove(i);audit.record(user,"DELETE",id);}
 public List<Putusan> search(String q){List<Putusan> r=new ArrayList<>();String x=q==null?"":q.toLowerCase();for(int i=0;i<repo.all().size();i++){Putusan p=repo.all().get(i);if((p.getCaseId()!=null&&p.getCaseId().toLowerCase().contains(x))||(p.getNomorPerkara()!=null&&p.getNomorPerkara().toLowerCase().contains(x)))r.add(p);}return r;}
 public List<Putusan> filter(String jenis,Double min,Double max,String pasal,Integer vonisMin,Integer vonisMax){List<Putusan> r=new ArrayList<>();for(int i=0;i<repo.all().size();i++){Putusan p=repo.all().get(i);Double b=p.getBeratBarangBuktiGram();Integer v=p.getVonisBulan();if(jenis!=null&&!jenis.isBlank()&&(p.getJenisNarkotika()==null||!p.getJenisNarkotika().equalsIgnoreCase(jenis)))continue;if(min!=null&&(b==null||b<min)||max!=null&&(b==null||b>max))continue;if(pasal!=null&&!pasal.isBlank()&&(p.getPasalTerbukti()==null||!p.getPasalTerbukti().toLowerCase().contains(pasal.toLowerCase())))continue;if(vonisMin!=null&&(v==null||v<vonisMin)||vonisMax!=null&&(v==null||v>vonisMax))continue;r.add(p);}return r;}
 public void sortByVonis(){repo.all().sort(Comparator.comparing(Putusan::getVonisBulan,Comparator.nullsLast(Integer::compareTo)));}
}
