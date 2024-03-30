package ru.yandex.javacource.kulpinov.schedule.manager;

import org.junit.jupiter.api.Test;
import ru.yandex.javacource.kulpinov.schedule.task.Epic;
import ru.yandex.javacource.kulpinov.schedule.task.Status;
import ru.yandex.javacource.kulpinov.schedule.task.SubTask;
import ru.yandex.javacource.kulpinov.schedule.task.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    public void shouldPreservePreviousTaskVersion() {
        TaskManager taskManager = Managers.getDefault();
        Task task = new Task("Задача  1", "Описание 1", Status.NEW);

        taskManager.addTask(task);
        taskManager.getTaskByID(task.getId());

        Task updatedTask = new Task("Задача 2", "Описание 2", Status.NEW, task.getId());
        taskManager.updateTask(updatedTask);
        taskManager.getTaskByID(updatedTask.getId());

        List<Task> history = taskManager.getHistory();

        assertEquals(2, history.size());
        assertEquals("Описание 1", history.get(0).getDescription());
        assertEquals("Описание 2", history.get(1).getDescription());
    }

    @Test
    public void shouldPreservePreviousSubTaskVersion() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Эпик 1", "Описание", Status.NEW);
        taskManager.addEpic(epic);

        SubTask subTask = new SubTask("Подзадача 1", "Описание 1", Status.NEW, epic.getId());
        taskManager.addSubTask(subTask);
        taskManager.getSubtaskByID(subTask.getId());

        SubTask updatedSubTask = new SubTask("Подзадача 2", "Описание 2", Status.IN_PROGRESS, epic.getId(), subTask.getId());
        taskManager.updateSubTask(updatedSubTask);
        taskManager.getSubtaskByID(updatedSubTask.getId());

        List<Task> history = taskManager.getHistory();

        assertEquals(2, history.size());
        assertEquals("Описание 1", history.get(0).getDescription());
        assertEquals("Описание 2", history.get(1).getDescription());
    }


    @Test
    public void shouldPreservePreviousEpicVersion() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Эпик 1", "Описание 1", Status.NEW);
        taskManager.addEpic(epic);
        taskManager.getEpicByID(epic.getId());

        Epic updatedEpic = new Epic("Эпик 2", "Описание 2", Status.NEW, epic.getId());

        taskManager.updateEpic(updatedEpic);
        taskManager.getEpicByID(updatedEpic.getId());

        List<Task> history = taskManager.getHistory();

        assertEquals(2, history.size());
        assertEquals("Описание 1", history.get(0).getDescription());
        assertEquals("Описание 2", history.get(1).getDescription());
    }

}