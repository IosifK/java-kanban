package ru.yandex.javacource.kulpinov.schedule.manager;

import org.junit.jupiter.api.Test;
import ru.yandex.javacource.kulpinov.schedule.task.Status;
import ru.yandex.javacource.kulpinov.schedule.task.Task;


import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {

    @Test
    void testHistoryEmpty() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void testAddingTaskToHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task("Задача 1", "Описание 1", Status.NEW, 1);
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size());
        assertEquals(task, historyManager.getHistory().get(0));
    }

    @Test
    void testDuplicateTasksInHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task("Задача 1", "Описание 1", Status.NEW, 1);
        historyManager.add(task);
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    void testRemovingTaskFromHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW, 1);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW, 2);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task1.getId());
        assertEquals(1, historyManager.getHistory().size());
        assertEquals(task2, historyManager.getHistory().get(0));
    }

    @Test
    void testRemovingMiddleTaskFromHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW, 1);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW, 2);
        Task task3 = new Task("Задача 3", "Описание 3", Status.NEW, 3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task2.getId());
        assertEquals(2, historyManager.getHistory().size());
        assertEquals(task1, historyManager.getHistory().get(0));
        assertEquals(task3, historyManager.getHistory().get(1));
    }

    @Test
    void testRemovingLastTaskFromHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW, 1);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW, 2);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task2.getId());
        assertEquals(1, historyManager.getHistory().size());
        assertEquals(task1, historyManager.getHistory().get(0));
    }
}