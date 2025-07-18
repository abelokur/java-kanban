package ru.yandex.javacourse.model;

public class Node<E> {
    public Task data;
    public Node<E> next;
    public Node<E> prev;

    public Node(Node<E> prev, Task data, Node<E> next) {
        this.prev = prev;
        this.data = data;
        this.next = next;
    }

    @Override
    public String toString() {
        return "Node: " + data;
    }
}
