package ru.yandex.javacource.kulpinov.schedule.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.kulpinov.schedule.task.Epic;
import ru.yandex.javacource.kulpinov.schedule.task.Status;
import ru.yandex.javacource.kulpinov.schedule.task.SubTask;
import ru.yandex.javacource.kulpinov.schedule.task.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    protected abstract T createTaskManager();

    @BeforeEach
    void setUp() {
        taskManager = createTaskManager();
    }

    @Test
    void testAddAndFindTaskById() {
        Task task = new Task("Задача 1", "Описание 1", Status.NEW);
        int taskId = taskManager.addTask(task);
        Task foundTask = taskManager.getTaskByID(taskId);

        assertNotNull(foundTask, "Задача должна быть найдена по ID");
        assertEquals(task, foundTask, "Найденная задача должна соответствовать добавленной");
    }

    @Test
    void testAddAndFindSubTaskById() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", Status.NEW);
        int epicId = taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Подзадача 1", "Описание подзадачи 1", Status.NEW, epicId);
        int subTaskId = taskManager.addSubTask(subTask);
        SubTask foundSubTask = taskManager.getSubtaskByID(subTaskId);

        assertNotNull(foundSubTask, "Подзадача должна быть найдена по ID");
        assertEquals(subTask, foundSubTask, "Найденная подзадача должна соответствовать добавленной");
    }

    @Test
    void testAddAndFindEpicById() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", Status.NEW);
        int epicId = taskManager.addEpic(epic);
        Epic foundEpic = taskManager.getEpicByID(epicId);

        assertNotNull(foundEpic, "Эпик должен быть найден по ID");
        assertEquals(epic, foundEpic, "Найденный эпик должен соответствовать добавленному");
    }

    @Test
    void testUpdateTask() {
        Task task = new Task("Задача 1", "Описание 1", Status.NEW);
        int taskId = taskManager.addTask(task);
        Task updatedTask = new Task("Задача 1 обновленная", "Описание 1 обновленное", Status.DONE, taskId);
        taskManager.updateTask(updatedTask);
        Task foundTask = taskManager.getTaskByID(taskId);

        assertNotNull(foundTask, "Задача должна быть найдена по ID после обновления");
        assertEquals(updatedTask, foundTask, "Обновленная задача должна соответствовать найденной");
    }

    @Test
    void testUpdateSubTask() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", Status.NEW);
        int epicId = taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Подзадача 1", "Описание подзадачи 1", Status.NEW, epicId);
        int subTaskId = taskManager.addSubTask(subTask);
        SubTask updatedSubTask = new SubTask("Подзадача 1 обновленная", "Описание подзадачи 1 обновленное", Status.DONE, epicId, subTaskId);
        taskManager.updateSubTask(updatedSubTask);
        SubTask foundSubTask = taskManager.getSubtaskByID(subTaskId);

        assertNotNull(foundSubTask, "Подзадача должна быть найдена по ID после обновления");
        assertEquals(updatedSubTask, foundSubTask, "Обновленная подзадача должна соответствовать найденной");
    }

    @Test
    void testUpdateEpic() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", Status.NEW);
        int epicId = taskManager.addEpic(epic);
        Epic updatedEpic = new Epic("Эпик 1 обновленный", "Описание эпика 1 обновленное", Status.DONE, epicId);
        taskManager.updateEpic(updatedEpic);
        Epic foundEpic = taskManager.getEpicByID(epicId);

        assertNotNull(foundEpic, "Эпик должен быть найден по ID после обновления");
        assertEquals(updatedEpic, foundEpic, "Обновленный эпик должен соответствовать найденному");
    }

    @Test
    void testDeleteTask() {
        Task task = new Task("Задача 1", "Описание 1", Status.NEW);
        int taskId = taskManager.addTask(task);
        taskManager.deleteTask(taskId);
        Task foundTask = taskManager.getTaskByID(taskId);

        assertNull(foundTask, "Задача не должна быть найдена после удаления");
    }

    @Test
    void testDeleteSubTask() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", Status.NEW);
        int epicId = taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Подзадача 1", "Описание подзадачи 1", Status.NEW, epicId);
        int subTaskId = taskManager.addSubTask(subTask);
        taskManager.deleteSubTask(subTaskId);
        SubTask foundSubTask = taskManager.getSubtaskByID(subTaskId);

        assertNull(foundSubTask, "Подзадача не должна быть найдена после удаления");
    }

    @Test
    void testDeleteEpic() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", Status.NEW);
        int epicId = taskManager.addEpic(epic);
        taskManager.deleteEpic(epicId);
        Epic foundEpic = taskManager.getEpicByID(epicId);

        assertNull(foundEpic, "Эпик не должен быть найден после удаления");
    }

    @Test
    void testGetAllTasks() {
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание 2", Status.IN_PROGRESS);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        assertEquals(2, taskManager.getAllTasks().size());
    }

    @Test
    void testGetAllSubTasks() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", Status.NEW);
        int epicId = taskManager.addEpic(epic);
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", Status.NEW, epicId);
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", Status.IN_PROGRESS, epicId);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        assertEquals(2, taskManager.getAllSubTasks().size());
    }

    @Test
    void testGetAllEpics() {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", Status.NEW);
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2", Status.DONE);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        assertEquals(2, taskManager.getAllEpics().size());
    }

    @Test
    void testClearTask() {
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание 2", Status.IN_PROGRESS);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.clearTask();
        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    void testClearSubtask() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", Status.NEW);
        int epicId = taskManager.addEpic(epic);
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", Status.NEW, epicId);
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", Status.IN_PROGRESS, epicId);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.clearSubtask();
        assertTrue(taskManager.getAllSubTasks().isEmpty());
        assertTrue(taskManager.getAllSubTasksFromEpic(epicId).isEmpty());
    }

    @Test
    void testClearEpic() {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", Status.NEW);
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2", Status.DONE);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.clearEpic();
        assertTrue(taskManager.getAllEpics().isEmpty());
        assertTrue(taskManager.getAllSubTasks().isEmpty());
    }

    @Test
    void testCheckForIntersections() {
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        task1.setStartTime(LocalDateTime.of(2024, 7, 1, 10, 0));
        task1.setDuration(Duration.ofHours(1));
        taskManager.addTask(task1);

        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW);
        task2.setStartTime(LocalDateTime.of(2024, 7, 1, 10, 30));
        task2.setDuration(Duration.ofHours(1));

        assertThrows(IllegalArgumentException.class, () -> taskManager.addTask(task2), "Время задачи пересекается с существующей задачей");
    }
}

