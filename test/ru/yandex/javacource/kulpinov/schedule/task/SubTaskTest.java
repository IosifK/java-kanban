package ru.yandex.javacource.kulpinov.schedule.task;

import org.junit.jupiter.api.Test;
import ru.yandex.javacource.kulpinov.schedule.manager.InMemoryTaskManager;
import ru.yandex.javacource.kulpinov.schedule.manager.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

public class SubTaskTest {



    @Test
    void testSubTaskNotEqualWithDifferentIds() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Эпик", "Описание", Status.NEW);
        int epicId = taskManager.addEpic(epic);

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание 1", Status.NEW, epicId);
        SubTask subTask2 = new SubTask("Подзадача 1", "Описание 1", Status.NEW, epicId);
        int subTask1Id = taskManager.addSubTask(subTask1);
        int subTask2Id = taskManager.addSubTask(subTask2);

        SubTask foundSubTask1 = taskManager.getSubtaskByID(subTask1Id);
        SubTask foundSubTask2 = taskManager.getSubtaskByID(subTask2Id);

        assertNotEquals(foundSubTask1, foundSubTask2);
    }

    @Test
    void testSubTaskStatus() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Эпик", "Описание", Status.NEW);
        int epicId = taskManager.addEpic(epic);

        SubTask subTask = new SubTask("Подзадача 1", "Описание 1", Status.NEW, epicId);
        int subTaskId = taskManager.addSubTask(subTask);
        subTask.setStatus(Status.DONE);
        taskManager.updateSubTask(subTask);

        SubTask foundSubTask = taskManager.getSubtaskByID(subTaskId);
        assertEquals(Status.DONE, foundSubTask.getStatus());
    }

    @Test
    void testSubTaskName() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Эпик", "Описание", Status.NEW);
        int epicId = taskManager.addEpic(epic);

        SubTask subTask = new SubTask("Подзадача 1", "Описание 1", Status.NEW, epicId);
        int subTaskId = taskManager.addSubTask(subTask);
        subTask.setName("Подзадача обновленная");
        taskManager.updateSubTask(subTask);

        SubTask foundSubTask = taskManager.getSubtaskByID(subTaskId);
        assertEquals("Подзадача обновленная", foundSubTask.getName());
    }

    @Test
    void testSubTaskDescription() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Эпик", "Описание", Status.NEW);
        int epicId = taskManager.addEpic(epic);

        SubTask subTask = new SubTask("Подзадача 1", "Описание 1", Status.NEW, epicId);
        int subTaskId = taskManager.addSubTask(subTask);
        subTask.setDescription("Описание обновленное");
        taskManager.updateSubTask(subTask);

        SubTask foundSubTask = taskManager.getSubtaskByID(subTaskId);
        assertEquals("Описание обновленное", foundSubTask.getDescription());
    }

    @Test
    void testSubTaskEpicId() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Эпик", "Описание", Status.NEW);
        int epicId = taskManager.addEpic(epic);

        SubTask subTask = new SubTask("Подзадача 1", "Описание 1", Status.NEW, epicId);
        int subTaskId = taskManager.addSubTask(subTask);

        SubTask foundSubTask = taskManager.getSubtaskByID(subTaskId);
        assertEquals(epicId, foundSubTask.getEpicId());
    }

    @Test
    void testSubTaskWithEpicId() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic("Эпик 1", "Описание 1", Status.NEW);
        int epic1Id = taskManager.addEpic(epic1);

        Epic epic2 = new Epic("Эпик 2", "Описание 2", Status.NEW);
        int epic2Id = taskManager.addEpic(epic2);

        SubTask subTask = new SubTask("Подзадача 1", "Описание 1", Status.NEW, epic1Id);
        int subTaskId = taskManager.addSubTask(subTask);
        subTask.setEpicId(epic2Id);
        taskManager.updateSubTask(subTask);

        SubTask foundSubTask = taskManager.getSubtaskByID(subTaskId);
        assertEquals(epic2Id, foundSubTask.getEpicId());
    }
}