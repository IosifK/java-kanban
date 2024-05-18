package ru.yandex.javacource.kulpinov.schedule.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.kulpinov.schedule.task.Epic;
import ru.yandex.javacource.kulpinov.schedule.task.Status;
import ru.yandex.javacource.kulpinov.schedule.task.SubTask;
import ru.yandex.javacource.kulpinov.schedule.task.Task;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

    public class FileBackedTaskManagerTest {
        private File file;
        private FileBackedTaskManager taskManager;

        @BeforeEach
        public void setUp() throws IOException {
            file = File.createTempFile("tasks", ".csv");
            taskManager = new FileBackedTaskManager(file);
        }

        @Test
        public void testSaveAndLoadEmptyFile() {
            taskManager = FileBackedTaskManager.loadFromFile(file);
            assertTrue(taskManager.getAllTasks().isEmpty());
            assertTrue(taskManager.getAllSubTasks().isEmpty());
            assertTrue(taskManager.getAllEpics().isEmpty());
        }

        @Test
        public void testSaveAndLoadTasks() {
            Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
            taskManager.addTask(task1);

            Task task2 = new Task("Задача 2", "Описание 2", Status.IN_PROGRESS);
            taskManager.addTask(task2);

            Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", Status.NEW);
            taskManager.addEpic(epic1);

            SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", Status.NEW, epic1.getId());
            taskManager.addSubTask(subTask1);

            taskManager = FileBackedTaskManager.loadFromFile(file);

            List<Task> tasks = taskManager.getAllTasks();
            assertEquals(2, tasks.size());
            assertEquals("Задача 1", tasks.get(0).getName());
            assertEquals("Задача 2", tasks.get(1).getName());

            List<Epic> epics = taskManager.getAllEpics();
            assertEquals(1, epics.size());
            assertEquals("Эпик 1", epics.get(0).getName());

            List<SubTask> subTasks = taskManager.getAllSubTasks();
            assertEquals(1, subTasks.size());
            assertEquals("Подзадача 1", subTasks.get(0).getName());
            assertEquals(epic1.getId(), subTasks.get(0).getEpicId());
        }

        @Test
        public void testSaveAndLoadMultipleTasks() {
            Task task1 = new Task("Задание 1", "Описание 1", Status.NEW);
            taskManager.addTask(task1);

            Task task2 = new Task("Задание 2", "Описание 2", Status.IN_PROGRESS);
            taskManager.addTask(task2);

            taskManager = FileBackedTaskManager.loadFromFile(file);

            List<Task> tasks = taskManager.getAllTasks();
            assertEquals(2, tasks.size());
            assertEquals("Задание 1", tasks.get(0).getName());
            assertEquals("Задание 2", tasks.get(1).getName());
        }
    }


