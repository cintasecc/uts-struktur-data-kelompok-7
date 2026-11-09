package id.putusan.dashboard.ds;
import java.time.LocalDateTime;
/** Catatan aktivitas aplikasi. */
public record AuditEntry(LocalDateTime waktu,String pengguna,String aksi,String detail) {
 @Override public String toString(){return waktu+" | "+pengguna+" | "+aksi+" | "+detail;}
}
