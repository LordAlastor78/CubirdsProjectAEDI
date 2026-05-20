package es.uvigo.esei.aed1.tads.stack;

import java.util.NoSuchElementException;

public class LinkedStack<T> implements Stack<T> {

    private static final class Node<T> {
        private final T value;
        private Node<T> next;

        private Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<T> top;
    private int size;

    public LinkedStack() {
        this.top = null;
        this.size = 0;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void push(T value) throws NullPointerException {
        if (value == null) {
            throw new NullPointerException();
        }
        this.top = new Node<>(value, this.top);
        this.size++;
    }

    @Override
    public T pop() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T value = this.top.value;
        this.top = this.top.next;
        this.size--;
        return value;
    }

    @Override
    public void clear() {
        this.top = null;
        this.size = 0;
    }

    @Override
    public boolean contains(char character) {
        // Method not implemented for this stack implementation
        throw new UnsupportedOperationException("Unimplemented method 'contains'");
    }
}