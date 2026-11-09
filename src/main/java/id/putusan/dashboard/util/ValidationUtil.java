package id.putusan.dashboard.util;
import id.putusan.dashboard.model.Putusan;
/** Validasi dasar sebelum data disimpan. */
public final class ValidationUtil {
 private ValidationUtil(){}
 public static String validate(Putusan p){if(p==null)return "Data putusan kosong";if(p.getCaseId()==null||p.getCaseId().isBlank())return "caseId wajib diisi";if(p.getNomorPerkara()==null||p.getNomorPerkara().isBlank())return "Nomor perkara wajib diisi";if(p.getVonisBulan()!=null&&p.getVonisBulan()<0)return "Vonis tidak boleh negatif";if(p.getBeratBarangBuktiGram()!=null&&p.getBeratBarangBuktiGram()<0)return "Berat tidak boleh negatif";return null;}
}
