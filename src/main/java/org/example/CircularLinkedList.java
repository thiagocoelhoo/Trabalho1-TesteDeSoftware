package org.example;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CircularLinkedList<T> {
    class Node {
        T item;
        Node next;
        Node prev;
    }

    private Node head;
    private Node tail;
    private int size;

    public CircularLinkedList() {
        head = null;
        tail = null;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    /**
    * Add a new node to the end of the list (tail)
    * @param item is the new element's value
    */
    public void add(T item) {
        Node newNode = new Node();
        newNode.item = item;

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }

        tail.next = head;
        head.prev = tail;
        size++;
    }

    /**
     * Remove the object from the circular list
     * @param item
     */
    public void remove(T item) {
        Node node = head;

        for (int i = 0; i < size; i++) {
            if (node.item.equals(item)) {
                node.prev.next = node.next;
                node.next.prev = node.prev;
                size--;
                break;
            }
            node = node.next;
        }

        if (node == head) {
            head = node.next;
        }
    }

    /**
     * Remove the element at the specified index
     * @param index of the element to be removed
     */
    public void remove(int index) {
        Node node = head;

        for (int i = 0; i < index; i++) {
            node = node.next;
        }

        node.prev.next = node.next;
        node.next.prev = node.prev;

        if (node == head) {
            head = node.next;
        }

        size--;
    }

    public T get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();

        Node node = head;

        for (int i = 0; i < index; i++)
            node = node.next;

        return node.item;
    }

    /**
     * @return Retorna um objeto do tipo List<T> com todos os elementos da lista circular ordenados.
     */
    public List<T> toList() {
        List<T> result = new ArrayList<T>();

        if (!isEmpty()) {
            Node node = head;

            do {
                result.add(node.item);
                node = node.next;
            } while (node != head);
        }

        return result;
    }

    /**
     * @return Cria um iterador para percorrer os elementos da lista de forma circular
     */
    public Iterator<T> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<T> {
        private Node current = null;

        @Override
        public boolean hasNext() {
            return head != null;
        }

        @Override
        public T next() {
            if (!hasNext())
                throw new java.util.NoSuchElementException();

            if (current == null)
                current = head;
            else
                current = current.next;

            return current.item;
        }
    }
}

