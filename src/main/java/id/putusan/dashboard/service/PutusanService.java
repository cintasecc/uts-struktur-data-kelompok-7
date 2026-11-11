package id.putusan.dashboard.service;

import id.putusan.dashboard.model.Putusan;
import id.putusan.dashboard.repository.PutusanRepository;
import id.putusan.dashboard.util.ValidationUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Aturan bisnis CRUD putusan. */
public class PutusanService {
    private final PutusanRepository repo;
    private final AuditService audit;

    public PutusanService(PutusanRepository repository, AuditService auditService) {
        repo = repository;
        audit = auditService;
    }

    public void add(Putusan putusan, String user) {
        validate(putusan);
        repo.add(putusan);
        audit.record(user, "ADD", putusan.getCaseId());
    }

    public void update(int index, Putusan putusan, String user) {
        validate(putusan);
        String oldId = repo.all().get(index).getCaseId();
        repo.update(index, putusan);
        audit.record(user, "UPDATE", oldId + " -> " + putusan.getCaseId());
    }

    public void delete(int index, String user) {
        String id = repo.all().get(index).getCaseId();
        repo.remove(index);
        audit.record(user, "DELETE", id);
    }

    private void validate(Putusan putusan) {
        String error = ValidationUtil.validate(putusan);
        if (error != null) throw new IllegalArgumentException(error);
    }

    public List<Putusan> search(String query) {
        List<Putusan> result = new ArrayList<>();
        String keyword = query == null ? "" : query.toLowerCase();
        for (int i = 0; i < repo.all().size(); i++) {
            Putusan p = repo.all().get(i);
            if ((p.getCaseId() != null && p.getCaseId().toLowerCase().contains(keyword))
                    || (p.getNomorPerkara() != null && p.getNomorPerkara().toLowerCase().contains(keyword))) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Putusan> filter(String jenis, Double min, Double max, String pasal,
                                Integer vonisMin, Integer vonisMax) {
        List<Putusan> result = new ArrayList<>();
        for (int i = 0; i < repo.all().size(); i++) {
            Putusan p = repo.all().get(i);
            Double weight = p.getBeratBarangBuktiGram();
            Integer sentence = p.getVonisBulan();
            if (jenis != null && !jenis.isBlank()
                    && (p.getJenisNarkotika() == null || !p.getJenisNarkotika().equalsIgnoreCase(jenis))) continue;
            if ((min != null && (weight == null || weight < min))
                    || (max != null && (weight == null || weight > max))) continue;
            if (pasal != null && !pasal.isBlank()
                    && (p.getPasalTerbukti() == null || !p.getPasalTerbukti().toLowerCase().contains(pasal.toLowerCase()))) continue;
            if ((vonisMin != null && (sentence == null || sentence < vonisMin))
                    || (vonisMax != null && (sentence == null || sentence > vonisMax))) continue;
            result.add(p);
        }
        return result;
    }

    public void sortByVonis() {
        repo.all().sort(Comparator.comparing(Putusan::getVonisBulan,
                Comparator.nullsLast(Integer::compareTo)));
    }
}
