package ru.yandex.javacourse.service;

import ru.yandex.javacourse.model.HistoryManager;
import ru.yandex.javacourse.model.Task;
import ru.yandex.javacourse.model.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

public class InMemoryHistoryManager implements HistoryManager {

    private final HashMap<Integer, Node<Task>> hashMapList = new HashMap<>();
    private final LinkedListClass<Task> linkedList = new LinkedListClass<>();


    @Override
    public List<Task> getDefaultHistory() {

        return linkedList.getTasks();
    }


    @Override
    public void add(Task object) {

        int taskId = object.getId();
        if (hashMapList.containsKey(taskId)) {
            removeNode(hashMapList.get(taskId));
        }

        Node<Task> newNode = linkedList.linkLast(object);
        hashMapList.put(taskId, newNode);
    }

    @Override
    public void remove(int id) {
        removeNode(hashMapList.get(id));
    }

    public void removeNode(Node node) {
        if (node != null) {

            if (node.prev != null) {
                node.prev.next = node.next;
            } else {
                linkedList.head = node.next; // Если узел — голова
            }

            if (node.next != null) {
                node.next.prev = node.prev;
            } else {
                linkedList.tail = node.prev; // Если узел — хвост
            }
            LinkedListClass.size--;
        }
    }

    public static class LinkedListClass<T> {
        public Node<Task> head;
        public Node<Task> tail;
        public static int size = 0;

        public Node linkLast(Task element) {
            final Node<Task> oldLast = tail;
            final Node<Task> newNode = new Node(oldLast, element, null);
            tail = newNode;
            if (oldLast == null)
                head = newNode;
            else
                oldLast.next = newNode;
            size++;
            return newNode;
        }

        public ArrayList<Task> getTasks() {
            ArrayList<Task> arrayList = new ArrayList<>();
            Node<Task> start = head;
            if (start == null)
                throw new NoSuchElementException();
            while (start != null) {
                arrayList.add(start.data);
                start = start.next;
            }
            return arrayList;
        }
    }
}
