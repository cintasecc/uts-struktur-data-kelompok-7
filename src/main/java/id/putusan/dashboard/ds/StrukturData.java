package id.putusan.dashboard.ds;
/** Kontrak struktur data generik sederhana. */
public interface StrukturData<T> {
    void addLast(T value); T get(int index); T remove(int index); int size(); boolean isEmpty(); void clear();
}
