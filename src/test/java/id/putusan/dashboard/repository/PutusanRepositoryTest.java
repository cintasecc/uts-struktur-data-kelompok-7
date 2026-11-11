package id.putusan.dashboard.repository;

import id.putusan.dashboard.model.Putusan;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PutusanRepositoryTest {
    @TempDir Path tempDir;

    @Test
    void updateKeepsListSizeAndRejectsDuplicateCaseNumber() {
        PutusanRepository repository = new PutusanRepository();
        repository.add(putusan("C-1", "1/Pid.Sus/2024/PN X"));
        repository.add(putusan("C-2", "2/Pid.Sus/2024/PN X"));

        repository.update(0, putusan("C-1-EDIT", "1/Pid.Sus/2024/PN X"));
        assertEquals("C-1-EDIT", repository.all().get(0).getCaseId());
        assertEquals(2, repository.all().size());
        assertTrue(repository.all().validateInvariant());
        assertThrows(IllegalArgumentException.class,
                () -> repository.update(0, putusan("C-2", "3/Pid.Sus/2024/PN X")));
    }

    @Test
    void saveAndLoadPreservesAllSixteenFields() throws Exception {
        PutusanRepository repository = new PutusanRepository();
        repository.add(new Putusan("C-1", "1/Pid.Sus/2024/PN X", "PN X", LocalDate.of(2024, 1, 2),
                "T-1", "Sabu", 1.25, "Pasal 112", "Pasal 112", 48, 36,
                100000000L, "1 paket", "Memberatkan", "Meringankan", "hal. 2-5"));
        Path csv = tempDir.resolve("roundtrip.csv");
        repository.save(csv);

        PutusanRepository restored = new PutusanRepository();
        restored.load(csv);
        Putusan actual = restored.all().get(0);
        assertEquals("C-1", actual.getCaseId());
        assertEquals("PN X", actual.getPengadilan());
        assertEquals(LocalDate.of(2024, 1, 2), actual.getTanggalPutusan());
        assertEquals(1.25, actual.getBeratBarangBuktiGram());
        assertEquals(100000000L, actual.getDendaRupiah());
        assertEquals("hal. 2-5", actual.getSourcePages());
        assertTrue(restored.all().validateInvariant());
    }

    private static Putusan putusan(String id, String number) {
        return new Putusan(id, number, "PN X", LocalDate.of(2024, 1, 2), "T-1", "Sabu",
                1.0, "Pasal 112", "Pasal 112", 48, 36, 100000000L,
                "barang bukti", "memberatkan", "meringankan", "hal. 1");
    }
}
