package id.putusan.dashboard.service;
import id.putusan.dashboard.model.Putusan;import java.util.List;
/** Facade pencarian. */
public class SearchService {private final PutusanService service;public SearchService(PutusanService s){service=s;}public List<Putusan> search(String q){return service.search(q);}}
