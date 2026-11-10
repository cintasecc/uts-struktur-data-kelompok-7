package id.putusan.dashboard.ds;
import org.junit.jupiter.api.Test;import static org.junit.jupiter.api.Assertions.*;
class SinglyLinkedListTest {
 @Test void operationsAndInvariant(){SinglyLinkedList<Integer> l=new SinglyLinkedList<>();assertTrue(l.isEmpty());l.addLast(2);l.addFirst(1);l.insert(2,3);assertEquals(3,l.size());assertEquals(2,l.get(1));assertEquals(2,l.remove(1));assertTrue(l.validateInvariant());l.clear();assertTrue(l.validateInvariant());}
}
