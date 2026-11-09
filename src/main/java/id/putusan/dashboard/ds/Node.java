package id.putusan.dashboard.ds;
/** Node generik untuk struktur linked list dan stack. */
public final class Node<T> {
    T data; Node<T> next;
    public Node(T data){this.data=data;}
    public T getData(){return data;} public Node<T> getNext(){return next;}
}
