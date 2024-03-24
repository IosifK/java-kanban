package ru.yandex.javacource.kulpinov.schedule.manager;

import ru.yandex.javacource.kulpinov.schedule.task.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final LinkedList<Task> history = new LinkedList<>();
    private int maxViewedTaskCapacity = 10;

    @Override
    public <T extends Task> void add(T task) {
        if (history.contains(task)) {
            history.remove(task);
        }
        if (history.size() >= maxViewedTaskCapacity) {
            history.removeFirst();
        }
        history.addLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}


