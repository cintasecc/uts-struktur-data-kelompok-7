package id.putusan.dashboard.service;
import id.putusan.dashboard.model.Putusan;import java.util.List;
/** Facade filter data. */
public class FilterService {private final PutusanService service;public FilterService(PutusanService s){service=s;}public List<Putusan> apply(String jenis,Double min,Double max,String pasal,Integer vmin,Integer vmax){return service.filter(jenis,min,max,pasal,vmin,vmax);}}
