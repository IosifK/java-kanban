package ru.yandex.javacource.kulpinov.schedule.task;

import org.junit.jupiter.api.Test;
import ru.yandex.javacource.kulpinov.schedule.manager.Managers;
import ru.yandex.javacource.kulpinov.schedule.manager.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {


    @Test
    public void Epic() {

        Epic epic = new Epic("Эпик 1", "Описание 1", Status.NEW);
        Epic epic2 = new Epic("Эпик 1", "Описание 1", Status.NEW);
        assertEquals(epic, epic2, "Экземпляры классов Epic не равны при равных ID");

    }

    @Test
    public void shouldNotAllowEpicToBeItsOwnSubtask() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Эпик", "Описание", Status.NEW);

        taskManager.addEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание", Status.NEW, epic.getId());
        subTask.setId(epic.getId());

        taskManager.addSubTask(subTask);

        assertFalse(epic.getSubtasks().contains(epic.getId()), "Объект Epic добавляется в самого себя в виде подзадачи");
    }
}