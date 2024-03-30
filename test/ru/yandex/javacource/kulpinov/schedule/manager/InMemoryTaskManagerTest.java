package ru.yandex.javacource.kulpinov.schedule.manager;

import org.junit.jupiter.api.Test;
import ru.yandex.javacource.kulpinov.schedule.task.Epic;
import ru.yandex.javacource.kulpinov.schedule.task.Status;
import ru.yandex.javacource.kulpinov.schedule.task.SubTask;
import ru.yandex.javacource.kulpinov.schedule.task.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {


    @Test
    public void testInMemoryTaskManagerAddsAndFindsVariousTaskTypesById() {
        TaskManager taskManager = Managers.getDefault();

        Task task = new Task("Задача 1", "Описнаие 1", Status.NEW);
        taskManager.addTask(task);

        Epic epic = new Epic("Эпик 1", "Описание эпика 1", Status.NEW);
        taskManager.addEpic(epic);

        SubTask subTask = new SubTask("Подзадача 1", "Описание подзадача 1", Status.NEW, epic.getId());
        taskManager.addSubTask(subTask);

        assertEquals(task, taskManager.getTaskByID(task.getId()), "Не нашел задачу по ID");
        assertEquals(epic, taskManager.getEpicByID(epic.getId()), "Не нашел эпик по ID");
        assertEquals(subTask, taskManager.getSubtaskByID(subTask.getId()), "Не нашел подзадачу по ID");

    }

    @Test
    public void testTaskIdConflictsHandling() {

        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task("Задача 1", "Описнаие 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описнаие 2", Status.NEW);

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        assertNotNull(taskManager.getTaskByID(2));
        assertNotNull(taskManager.getTaskByID(task2.getId()));
        assertNotEquals(task1.getId(), task2.getId());
    }

    @Test
    public void shouldPreserveTaskIntegrity() {
        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task("Задача", "Описание", Status.NEW);

        taskManager.addTask(task1);
        Task task2 = taskManager.getTaskByID(task1.getId());

        assertEquals(task1.getName(), task2.getName());
        assertEquals(task1.getDescription(), task2.getDescription());
        assertEquals(task1.getStatus(), task2.getStatus());
    }


    @Test
    public void clearTasksTest() {
        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW);
        taskManager.addTask(task2);

        taskManager.clearTask();

        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    public void testEpicSubTasksRetrieval() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Эпик", "Описние", Status.NEW);
        taskManager.addEpic(epic);

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание", Status.NEW, epic.getId());
        taskManager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание", Status.DONE, epic.getId());
        taskManager.addSubTask(subTask2);

        List<SubTask> SubTasks = taskManager.getAllSubTasksFromEpic(epic.getId());

        assertNotNull(SubTasks);
        assertEquals(2, SubTasks.size());
        assertTrue(SubTasks.contains(subTask1));
        assertTrue(SubTasks.contains(subTask2));
    }

    @Test
    public void testSubTaskDeletion() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Эпик", "Описание", Status.NEW);
        taskManager.addEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание", Status.NEW, epic.getId());
        taskManager.addSubTask(subTask);

        taskManager.deleteSubTask(subTask.getId());

        assertNull(taskManager.getSubtaskByID(subTask.getId()));
        assertTrue(taskManager.getAllSubTasksFromEpic(epic.getId()).isEmpty());
    }


    @Test
    public void testEpicClearing() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Эпик", "Описание", Status.NEW);
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Подзадача", "Описание", Status.NEW, epic.getId());
        taskManager.addSubTask(subTask);

        taskManager.clearEpic();

        assertTrue(taskManager.getAllEpics().isEmpty());
        assertTrue(taskManager.getAllSubTasks().isEmpty());
    }


    @Test
    public void testSubtaskClearing() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Эпик", "Описание", Status.NEW);
        taskManager.addEpic(epic);
        SubTask subTask1 = new SubTask("Задача 1", "Описание", Status.NEW, epic.getId());
        SubTask subTask2 = new SubTask("Задача 2", "Описание", Status.IN_PROGRESS, epic.getId());
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);

        taskManager.clearSubtask();

        assertTrue(taskManager.getAllSubTasks().isEmpty());
        assertTrue(taskManager.getAllSubTasksFromEpic(epic.getId()).isEmpty());

    }

    @Test
    public void whenUpdatingSubTask_thenSubTaskIsUpdated() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Эпик", "Описание", Status.NEW);
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Подзадача", "Описание", Status.IN_PROGRESS, epic.getId());
        taskManager.addSubTask(subTask);

        subTask.setName("Обновленная подзадача");
        taskManager.updateSubTask(subTask);

        SubTask subTask1 = taskManager.getSubtaskByID(subTask.getId());
        assertNotNull(subTask1);
        assertEquals("Обновленная подзадача", subTask1.getName());
    }

}


