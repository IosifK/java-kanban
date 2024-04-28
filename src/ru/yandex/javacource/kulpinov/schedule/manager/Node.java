package ru.yandex.javacource.kulpinov.schedule.manager;

import ru.yandex.javacource.kulpinov.schedule.task.Task;

public class Node<T> {

    private Task task;
    private Node<T> next;
    private Node<T> prev;

    public Node(Task task, Node<T> next, Node<T> prev) {
        this.task = task;
        this.next = next;
        this.prev = prev;
    }

    public Node<T> getNext() {
        return next;
    }

    public Node<T> getPrev() {
        return prev;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    public void setPrev(Node<T> prev) {
        this.prev = prev;
    }

    public Task getTask() {
        return task;
    }
}
