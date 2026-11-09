package id.putusan.dashboard.model;

import java.time.LocalDate;

/** Model satu putusan pengadilan. Nilai numerik nullable untuk data yang tidak tersedia. */
public class Putusan {
    private String caseId, nomorPerkara, pengadilan, terdakwaKode, jenisNarkotika;
    private LocalDate tanggalPutusan;
    private Double beratBarangBuktiGram;
    private String pasalDakwaan, pasalTerbukti;
    private Integer tuntutanBulan, vonisBulan;
    private Long dendaRupiah;
    private String barangBukti, keadaanMemberatkan, keadaanMeringankan, sourcePages;

    public Putusan(String caseId, String nomorPerkara, String pengadilan, LocalDate tanggalPutusan,
        String terdakwaKode, String jenisNarkotika, Double beratBarangBuktiGram, String pasalDakwaan,
        String pasalTerbukti, Integer tuntutanBulan, Integer vonisBulan, Long dendaRupiah,
        String barangBukti, String keadaanMemberatkan, String keadaanMeringankan, String sourcePages) {
        this.caseId=clean(caseId); this.nomorPerkara=clean(nomorPerkara); this.pengadilan=clean(pengadilan);
        this.tanggalPutusan=tanggalPutusan; this.terdakwaKode=clean(terdakwaKode); this.jenisNarkotika=clean(jenisNarkotika);
        this.beratBarangBuktiGram=beratBarangBuktiGram; this.pasalDakwaan=clean(pasalDakwaan);
        this.pasalTerbukti=clean(pasalTerbukti); this.tuntutanBulan=tuntutanBulan; this.vonisBulan=vonisBulan;
        this.dendaRupiah=dendaRupiah; this.barangBukti=clean(barangBukti); this.keadaanMemberatkan=clean(keadaanMemberatkan);
        this.keadaanMeringankan=clean(keadaanMeringankan); this.sourcePages=clean(sourcePages);
    }
    private static String clean(String s){ return s==null||s.isBlank()?null:s.trim(); }
    public String getCaseId(){return caseId;} public String getNomorPerkara(){return nomorPerkara;}
    public String getPengadilan(){return pengadilan;} public LocalDate getTanggalPutusan(){return tanggalPutusan;}
    public String getTerdakwaKode(){return terdakwaKode;} public String getJenisNarkotika(){return jenisNarkotika;}
    public Double getBeratBarangBuktiGram(){return beratBarangBuktiGram;} public String getPasalDakwaan(){return pasalDakwaan;}
    public String getPasalTerbukti(){return pasalTerbukti;} public Integer getTuntutanBulan(){return tuntutanBulan;}
    public Integer getVonisBulan(){return vonisBulan;} public Long getDendaRupiah(){return dendaRupiah;}
    public String getBarangBukti(){return barangBukti;} public String getKeadaanMemberatkan(){return keadaanMemberatkan;}
    public String getKeadaanMeringankan(){return keadaanMeringankan;} public String getSourcePages(){return sourcePages;}
    public void setNomorPerkara(String v){nomorPerkara=clean(v);} public void setPengadilan(String v){pengadilan=clean(v);}
    public void setTanggalPutusan(LocalDate v){tanggalPutusan=v;} public void setTerdakwaKode(String v){terdakwaKode=clean(v);}
    public void setJenisNarkotika(String v){jenisNarkotika=clean(v);} public void setBeratBarangBuktiGram(Double v){beratBarangBuktiGram=v;}
    public void setPasalDakwaan(String v){pasalDakwaan=clean(v);} public void setPasalTerbukti(String v){pasalTerbukti=clean(v);}
    public void setTuntutanBulan(Integer v){tuntutanBulan=v;} public void setVonisBulan(Integer v){vonisBulan=v;}
    public void setDendaRupiah(Long v){dendaRupiah=v;} public void setBarangBukti(String v){barangBukti=clean(v);}
    public void setKeadaanMemberatkan(String v){keadaanMemberatkan=clean(v);} public void setKeadaanMeringankan(String v){keadaanMeringankan=clean(v);}
    public void setSourcePages(String v){sourcePages=clean(v);}
}
