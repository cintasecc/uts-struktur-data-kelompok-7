package id.putusan.dashboard.util;
import org.junit.jupiter.api.Test;import static org.junit.jupiter.api.Assertions.*;import java.io.IOException;
class CsvReaderTest {@Test void parsesQuotedComma()throws Exception{assertEquals(java.util.List.of("a","b,c","d"),CsvReader.parse("a,\"b,c\",d"));}}
