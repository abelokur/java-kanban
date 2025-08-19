package ru.yandex.javacourse.service;

import ru.yandex.javacourse.model.HistoryManager;
import ru.yandex.javacourse.model.Task;
import ru.yandex.javacourse.model.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final HashMap<Integer, Node<Task>> hashMapList = new HashMap<>();
    private final CustomLinkedList<Task> linkedList = new CustomLinkedList<>();


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
                linkedList.head = node.next;
            }

            if (node.next != null) {
                node.next.prev = node.prev;
            } else {
                linkedList.tail = node.prev;
            }
            linkedList.size--;
        }
    }

    public int getSize() {
        return linkedList.size;
    }

    public void setSize(int size) {
        linkedList.size = size;

    }

    public static class CustomLinkedList<T> {
        public Node<Task> head;
        public Node<Task> tail;
        public int size = 0;

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
                return arrayList;
            while (start != null) {
                arrayList.add(start.data);
                start = start.next;
            }
            return arrayList;
        }
    }
}
