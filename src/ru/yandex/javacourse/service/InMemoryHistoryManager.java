package ru.yandex.javacourse.service;

import ru.yandex.javacourse.model.HistoryManager;
import ru.yandex.javacourse.model.Task;
import ru.yandex.javacourse.model.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

public class InMemoryHistoryManager implements HistoryManager {
    //private List<Task> historyList = new ArrayList<>();
    private HashMap<Integer, Node<Task>> hashMapList = new HashMap<>();
    private LinkedListClass<Task> linkedList = new LinkedListClass<>();
    //static final int HISTORY_LIST_LENGTH = 10;

    @Override
    public List<Task> getDefaultHistory() {
        /*return historyList;*/
        return linkedList.getTasks();
    }


    @Override
    public void add(Task object) {
        /*if (historyList.size() >= HISTORY_LIST_LENGTH ) {
            historyList.remove(0);
        }*/
        int taskId = object.getId();
        if (hashMapList.containsKey(taskId)) {
            final Node node = hashMapList.get(taskId);
            if (removeNode(node, object)) {
                hashMapList.put(taskId, linkedList.linkLast(object));
            }
        } else {
            hashMapList.put(taskId, linkedList.linkLast(object));
        }

        //historyList.add(object);
    }

    @Override
    public void remove(int id) {
        //historyList.remove(id);
        removeNode(hashMapList.get(id), null);
    }

    public boolean removeNode(Node node, Task object) {
        if (node.prev == null && node.next == null) {
            return false;
        }

        if (node.prev == null) {
            linkedList.head = node.next;
            LinkedListClass.size--;
        }
        if (node.next == null) {
            linkedList.tail = node.prev;
            LinkedListClass.size--;
        }

        Node nodePrev = node.prev;
        Node nodeNext = node.next;
        if (nodePrev != null) {
            nodePrev.next = nodeNext;
        }
        if (nodeNext != null) {
            nodeNext.prev = nodePrev;}
        LinkedListClass.size--;
        return true;
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
