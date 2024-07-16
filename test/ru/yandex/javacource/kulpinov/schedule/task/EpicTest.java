package ru.yandex.javacource.kulpinov.schedule.task;

import org.junit.jupiter.api.Test;
import ru.yandex.javacource.kulpinov.schedule.manager.InMemoryTaskManager;
import ru.yandex.javacource.kulpinov.schedule.manager.Managers;
import ru.yandex.javacource.kulpinov.schedule.manager.TaskManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {

    @Test
    void testAllSubtasksNew() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Эпик", "Описание", Status.NEW);
        int epicId = taskManager.addEpic(epic);

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание 1", Status.NEW, epicId);
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание 2", Status.NEW, epicId);


        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);


        epic = taskManager.getEpicByID(epicId);
        assertEquals(Status.NEW, epic.getStatus());
    }



    @Test
    void testAllSubtasksDone() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Эпик", "Описание", Status.NEW);
        int epicId = taskManager.addEpic(epic);

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание 1", Status.NEW, epicId);
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание 2", Status.NEW, epicId);

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        epic = taskManager.getEpicByID(epicId);
        assertEquals(Status.NEW, epic.getStatus());

        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);
        taskManager.updateSubTask(subTask1);
        taskManager.updateSubTask(subTask2);

        epic = taskManager.getEpicByID(epicId);
        assertEquals(Status.DONE, epic.getStatus());
    }


    @Test
    void testSubtasksInProgress() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Эпик", "Описание", Status.NEW);
        int epicId = taskManager.addEpic(epic);

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание 1", Status.IN_PROGRESS, epicId);
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание 2", Status.IN_PROGRESS, epicId);

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        epic = taskManager.getEpicByID(epicId);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }
}