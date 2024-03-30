package ru.yandex.javacource.kulpinov.schedule.manager;

import ru.yandex.javacource.kulpinov.schedule.task.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final LinkedList<Task> history = new LinkedList<>();
    private static final int MAX_VIEWEDT_TASK_CAPACITY = 10;

    @Override
    public <T extends Task> void add(T task) {

        if (task == null) {
            return;
        }

        if (history.contains(task)) {
            return;
        }
        if (history.size() >= MAX_VIEWEDT_TASK_CAPACITY) {
            history.removeFirst();
        }
        history.addLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}


