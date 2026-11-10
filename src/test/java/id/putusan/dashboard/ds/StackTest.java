package id.putusan.dashboard.ds;
import org.junit.jupiter.api.Test;import static org.junit.jupiter.api.Assertions.*;
class StackTest {@Test void lifoAndPreserve(){Stack<String>s=new Stack<>();s.push("a");s.push("b");assertTrue(s.searchPreserve("a"));assertEquals("b",s.peek());assertEquals("b",s.pop());assertTrue(s.validateInvariant());}}
