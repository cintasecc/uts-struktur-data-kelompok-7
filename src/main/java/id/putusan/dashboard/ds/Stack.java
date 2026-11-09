package id.putusan.dashboard.ds;
/** Stack LIFO berbasis node buatan sendiri. */
public class Stack<T> {
 private Node<T> top; private int size;
 public void push(T value){Node<T> n=new Node<>(value);n.next=top;top=n;size++;}
 public T pop(){if(top==null)throw new IllegalStateException("Stack kosong");T v=top.data;top=top.next;size--;return v;}
 public T peek(){if(top==null)throw new IllegalStateException("Stack kosong");return top.data;}
 public int size(){return size;} public boolean isEmpty(){return size==0;}
 /** Pencarian tidak mengubah susunan stack. */
 public boolean searchPreserve(T value){for(Node<T> n=top;n!=null;n=n.next)if(java.util.Objects.equals(n.data,value))return true;return false;}
 /** Mengembalikan snapshot aktivitas terbaru-ke-terlama tanpa mengubah stack. */
 public String restore(){StringBuilder b=new StringBuilder();for(Node<T> n=top;n!=null;n=n.next)b.append(n.data).append(System.lineSeparator());return b.toString();}
 public boolean validateInvariant(){int c=0;for(Node<T> n=top;n!=null;n=n.next){if(++c>size)return false;}return c==size;}
}
