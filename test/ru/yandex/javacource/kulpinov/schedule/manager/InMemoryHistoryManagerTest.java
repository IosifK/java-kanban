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
    void testHistoryEmpty() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty(), "Изначально история должна быть пустой");
    }

    @Test
    void testDeletingNon_ExistentTask() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        historyManager.remove(888);

        assertTrue(historyManager.getHistory().isEmpty());
    }


    @Test
    void shouldMaintainTheCorrectOrderInHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW, 1);
        Task task2 = new Task("Задача 2", "Описание 2", Status.DONE, 2);
        Task task3 = new Task("Задача 3", "Описание 3", Status.IN_PROGRESS, 3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        List<Task> history = historyManager.getHistory();

        assertEquals(3, history.size());
        assertEquals(task1.getId(), history.get(0).getId());
        assertEquals(task2.getId(), history.get(1).getId());
        assertEquals(task3.getId(), history.get(2).getId());
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

        assertEquals(1, history.size());
        assertEquals("Описание 2", history.get(0).getDescription());
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

        assertEquals(1, history.size());
        assertEquals("Описание 2", history.get(0).getDescription());
    }

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

        assertEquals(1, history.size());
        assertEquals("Описание 2", history.get(0).getDescription());
    }
}