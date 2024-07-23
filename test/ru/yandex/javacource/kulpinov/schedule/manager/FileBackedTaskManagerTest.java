package ru.yandex.javacource.kulpinov.schedule.manager;

import org.junit.jupiter.api.Test;
import ru.yandex.javacource.kulpinov.schedule.task.Status;
import ru.yandex.javacource.kulpinov.schedule.task.Task;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @Override
    protected FileBackedTaskManager createTaskManager() {
        return new FileBackedTaskManager(new File("test_tasks.csv"));
    }

    @Test
    public void testSaveException() {
        File invalidFile = new File("");
        FileBackedTaskManager invalidTaskManager = new FileBackedTaskManager(invalidFile);
        Task task = new Task("Задача 1", "Описание 1", Status.NEW);
        assertThrows(ManagerSaveException.class, () -> invalidTaskManager.addTask(task));
    }

    @Test
    public void testLoadException() {
        File invalidFile = new File("");
        assertThrows(ManagerSaveException.class, () -> FileBackedTaskManager.loadFromFile(invalidFile));
    }
}


