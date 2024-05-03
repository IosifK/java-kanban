package ru.yandex.javacource.kulpinov.schedule.manager;

import ru.yandex.javacource.kulpinov.schedule.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node<Task>> history = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;


    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        final int id = task.getId();
        remove(id);
        linkLast(task);
        history.put(id, tail);
    }


    private void linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(task, null, oldTail);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.setNext(newNode);
        }
    }


    @Override
    public void remove(int id) {
        final Node<Task> node = history.remove(id);
        if (node == null) {
            return;
        }
        removeNode(node);
    }

    private void removeNode(Node<Task> node) {
        if (node == null) {
            return;
        }
        final Node<Task> next = node.getNext();
        final Node<Task> prev = node.getPrev();

        if (prev != null) {
            prev.setNext(next);
        } else {
            head = next;
        }
        if (next != null) {
            next.setPrev(prev);
        } else {
            tail = prev;
        }

        node.setTask(null);
    }

    private List<Task> getTasks() {
        List<Task> historyList = new ArrayList<>();
        Node<Task> node = head;
        while (node != null) {
            historyList.add(node.getTask());
            node = node.getNext();
        }
        return historyList;
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }


}





