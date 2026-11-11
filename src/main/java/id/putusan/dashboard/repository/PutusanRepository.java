package id.putusan.dashboard.repository;

import id.putusan.dashboard.ds.SinglyLinkedList;
import id.putusan.dashboard.model.Putusan;
import id.putusan.dashboard.util.CsvReader;
import id.putusan.dashboard.util.CsvWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Repository dengan SinglyLinkedList sebagai penyimpanan utama runtime. */
public class PutusanRepository {
    private final SinglyLinkedList<Putusan> data = new SinglyLinkedList<>();

    public void load(Path path) throws IOException {
        data.clear();
        for (Putusan putusan : new CsvReader().read(path)) data.addLast(putusan);
    }

    public void save(Path path) throws IOException {
        List<Putusan> rows = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) rows.add(data.get(i));
        new CsvWriter().write(path, rows);
    }

    public SinglyLinkedList<Putusan> all() { return data; }

    public Putusan findCase(String id) {
        for (int i = 0; i < data.size(); i++)
            if (Objects.equals(data.get(i).getCaseId(), id)) return data.get(i);
        return null;
    }

    public Putusan findNumber(String number) {
        for (int i = 0; i < data.size(); i++)
            if (Objects.equals(data.get(i).getNomorPerkara(), number)) return data.get(i);
        return null;
    }

    public void add(Putusan putusan) {
        ensureUnique(putusan, -1);
        data.addLast(putusan);
    }

    public void update(int index, Putusan putusan) {
        if (index < 0 || index >= data.size()) throw new IndexOutOfBoundsException(index);
        ensureUnique(putusan, index);
        data.set(index, putusan);
    }

    private void ensureUnique(Putusan candidate, int ignoredIndex) {
        for (int i = 0; i < data.size(); i++) {
            if (i == ignoredIndex) continue;
            Putusan existing = data.get(i);
            if (Objects.equals(existing.getCaseId(), candidate.getCaseId())
                    || Objects.equals(existing.getNomorPerkara(), candidate.getNomorPerkara())) {
                throw new IllegalArgumentException("caseId atau nomor perkara sudah digunakan");
            }
        }
    }

    public void remove(int index) { data.remove(index); }
}
