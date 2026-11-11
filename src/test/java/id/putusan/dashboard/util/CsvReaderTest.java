package id.putusan.dashboard.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CsvReaderTest {
    @TempDir Path tempDir;

    @Test
    void parsesQuotedComma() throws Exception {
        assertEquals(java.util.List.of("a", "b,c", "d"), CsvReader.parse("a,\"b,c\",d"));
    }

    @Test
    void readsCaseIdWhenCsvHasUtf8Bom() throws Exception {
        Path csv = tempDir.resolve("bom.csv");
        Files.writeString(csv, "\uFEFFcaseId,nomorPerkara\nC-01,01/Pid.Sus/2024/PN X\n");
        var rows = new CsvReader().read(csv);
        assertEquals(1, rows.size());
        assertEquals("C-01", rows.get(0).getCaseId());
    }
}
