package ru.yandex.javacource.kulpinov.schedule.manager;

import ru.yandex.javacource.kulpinov.schedule.task.Task;

import java.util.List;

public interface HistoryManager {


    <T extends Task> void add(T task);

    List<Task> getHistory();


}
