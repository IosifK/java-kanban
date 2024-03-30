package ru.yandex.javacource.kulpinov.schedule.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {


    @Test
    public void SubTask() {

        SubTask subTask = new SubTask("Подзадача 1", "Описание 1", Status.NEW, 1);
        SubTask subTask2 = new SubTask("Подзадача 1", "Описание 1", Status.NEW, 1);
        assertEquals(subTask, subTask2, "Экземпляры классов SubTask не равны при равных ID");
    }


}