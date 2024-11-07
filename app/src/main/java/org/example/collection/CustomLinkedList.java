package org.example.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class CustomLinkedList<T> {

    private class Node {
        T data;
        Node next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    public Iterator<T> iterator() {
        return new CustomIterator();
    }

    private class CustomIterator implements Iterator<T> {
        private Node current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T data = current.data;
            current = current.next;
            return data;
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            while (hasNext()) {
                action.accept(next());
            }
        }

    }

    private Node head;
    private int size;

    public CustomLinkedList() {
        head = null;
        size = 0;
    }

    public void add(T data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            head = head.next;
        } else {
            Node current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            current.next = current.next.next;
        }
        size--;
    }

    public boolean contains(T data) {
        Node current = head;
        while (current != null) {
            if (data == null) {
                if (current.data == null) {
                    return true;
                }
            } else {
                if (data.equals(current.data)) {
                    return true;
                }
            }
            current = current.next;
        }
        return false;
    }

    public void addAll(Collection<T> collection) {
        for (T data : collection) {
            add(data);
        }
    }

    public void addAll(CustomLinkedList<T> otherList) {
        Node current = otherList.head;
        while (current != null) {
            add(current.data);
            current = current.next;
        }
    }

    public T getFirst() {
        if (head == null) {
            throw new IllegalStateException();
        }
        return head.data;
    }

    public int size() {
        return size;
    }

}