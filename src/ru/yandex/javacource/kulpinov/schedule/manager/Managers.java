package ru.yandex.javacource.kulpinov.schedule.manager;

import java.io.File;
import java.nio.file.Path;

public class Managers {

    public static TaskManager getDefault() {
        return new FileBackedTaskManager(new File("resources/task.csv"));
    }

    public static TaskManager getFileBackedTasksManager(Path path) {
        return new FileBackedTaskManager(path.toFile());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
