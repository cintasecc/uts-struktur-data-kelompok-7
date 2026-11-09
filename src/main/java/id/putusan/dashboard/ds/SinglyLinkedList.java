package id.putusan.dashboard.ds;

import java.util.Objects;

/** Implementasi singly linked list tanpa Java Collection Framework. */
public class SinglyLinkedList<T> implements StrukturData<T> {
    private Node<T> head, tail; private int size;
    public void addFirst(T value){Node<T> n=new Node<>(value); n.next=head; head=n; if(tail==null)tail=n; size++;}
    @Override public void addLast(T value){Node<T> n=new Node<>(value); if(tail==null)head=tail=n; else {tail.next=n;tail=n;} size++;}
    public void insert(int index,T value){if(index<0||index>size)throw new IndexOutOfBoundsException(index); if(index==0){addFirst(value);return;} if(index==size){addLast(value);return;} Node<T> p=nodeAt(index-1); Node<T> n=new Node<>(value); n.next=p.next;p.next=n;size++;}
    @Override public T get(int index){return nodeAt(index).data;}
    @Override public T remove(int index){if(index<0||index>=size)throw new IndexOutOfBoundsException(index); Node<T> removed; if(index==0){removed=head;head=head.next;if(head==null)tail=null;}else{Node<T> p=nodeAt(index-1);removed=p.next;p.next=removed.next;if(removed==tail)tail=p;}size--;return removed.data;}
    public T set(int index,T value){Node<T> n=nodeAt(index);T old=n.data;n.data=value;return old;}
    public int search(T value){int i=0;for(Node<T> n=head;n!=null;n=n.next,i++)if(Objects.equals(n.data,value))return i;return -1;}
    @Override public int size(){return size;} @Override public boolean isEmpty(){return size==0;}
    public boolean contains(T value){return search(value)>=0;}
    @Override public void clear(){head=tail=null;size=0;}
    private Node<T> nodeAt(int index){if(index<0||index>=size)throw new IndexOutOfBoundsException(index);Node<T> n=head;for(int i=0;i<index;i++)n=n.next;return n;}
    public boolean validateInvariant(){int count=0;Node<T> last=null;for(Node<T> n=head;n!=null;n=n.next){count++;last=n;if(count>size)return false;}return count==size&&last==tail&&(size==0?head==null&&tail==null:head!=null&&tail.next==null);}
    public void sort(java.util.Comparator<? super T> comparator){if(size<2)return; for(int i=0;i<size-1;i++)for(int j=0;j<size-i-1;j++){Node<T>a=nodeAt(j),b=a.next;if(comparator.compare(a.data,b.data)>0){T t=a.data;a.data=b.data;b.data=t;}}}
}
