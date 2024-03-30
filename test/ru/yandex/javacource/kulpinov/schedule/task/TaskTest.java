package ru.yandex.javacource.kulpinov.schedule.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    public void Task() {
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        Task task2 = new Task("Задача 1", "Описание 1", Status.NEW);
        assertEquals(task1, task2, "Экземпляры классов Task не равны при равных ID");

    }
}