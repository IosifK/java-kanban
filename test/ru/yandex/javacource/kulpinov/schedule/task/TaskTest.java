package ru.yandex.javacource.kulpinov.schedule.task;

import org.junit.jupiter.api.Test;
import ru.yandex.javacource.kulpinov.schedule.manager.InMemoryTaskManager;
import ru.yandex.javacource.kulpinov.schedule.manager.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    void testTaskEquality() {
        TaskManager taskManager = new InMemoryTaskManager();

        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        int taskId = taskManager.addTask(task1);

        Task foundTask1 = taskManager.getTaskByID(taskId);
        Task foundTask2 = taskManager.getTaskByID(taskId);

        assertEquals(foundTask1, foundTask2);
    }

    @Test
    void testTaskNotEqualWithDifferentIds() {
        TaskManager taskManager = new InMemoryTaskManager();

        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        int task1Id = taskManager.addTask(task1);

        Task task2 = new Task("Задача 1", "Описание 1", Status.NEW);
        int task2Id = taskManager.addTask(task2);

        Task foundTask1 = taskManager.getTaskByID(task1Id);
        Task foundTask2 = taskManager.getTaskByID(task2Id);

        assertNotEquals(foundTask1, foundTask2);
    }

    @Test
    void testTaskStatus() {
        TaskManager taskManager = new InMemoryTaskManager();

        Task task = new Task("Задача 1", "Описание 1", Status.NEW);
        int taskId = taskManager.addTask(task);

        task.setStatus(Status.DONE);
        taskManager.updateTask(task);

        Task foundTask = taskManager.getTaskByID(taskId);
        assertEquals(Status.DONE, foundTask.getStatus());
    }

    @Test
    void testTaskName() {
        TaskManager taskManager = new InMemoryTaskManager();

        Task task = new Task("Задача 1", "Описание 1", Status.NEW);
        int taskId = taskManager.addTask(task);

        task.setName("Задача обновленная");
        taskManager.updateTask(task);

        Task foundTask = taskManager.getTaskByID(taskId);
        assertEquals("Задача обновленная", foundTask.getName());
    }

    @Test
    void testTaskDescription() {
        TaskManager taskManager = new InMemoryTaskManager();

        Task task = new Task("Задача 1", "Описание 1", Status.NEW);
        int taskId = taskManager.addTask(task);

        task.setDescription("обновленное");
        taskManager.updateTask(task);

        Task foundTask = taskManager.getTaskByID(taskId);
        assertEquals("обновленное", foundTask.getDescription());
    }
}