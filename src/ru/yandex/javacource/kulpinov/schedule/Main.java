package ru.yandex.javacource.kulpinov.schedule;

import ru.yandex.javacource.kulpinov.schedule.manager.FileBackedTaskManager;
import ru.yandex.javacource.kulpinov.schedule.task.Epic;
import ru.yandex.javacource.kulpinov.schedule.task.Status;
import ru.yandex.javacource.kulpinov.schedule.task.SubTask;
import ru.yandex.javacource.kulpinov.schedule.task.Task;
import ru.yandex.javacource.kulpinov.schedule.manager.Managers;


import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        FileBackedTaskManager taskManager = (FileBackedTaskManager) Managers.getDefault();

        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW, Duration.ofMinutes(60), LocalDateTime.now());
        taskManager.addTask(task1);

        Task task2 = new Task("Задача 2", "Описание 2", Status.IN_PROGRESS, Duration.ofMinutes(120), LocalDateTime.now().plusMinutes(60));
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", Status.NEW);
        taskManager.addEpic(epic1);

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now().plusMinutes(180), epic1.getId());
        taskManager.addSubTask(subTask1);

        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", Status.DONE, Duration.ofMinutes(45), LocalDateTime.now().plusMinutes(210), epic1.getId());
        taskManager.addSubTask(subTask2);

        System.out.println("Все задачи:");
        for (Task task : taskManager.getAllTasks()) {
            printTaskInfo(task);
        }

        System.out.println("\nВсе подзадачи:");
        for (SubTask subTask : taskManager.getAllSubTasks()) {
            printTaskInfo(subTask);
        }

        System.out.println("\nВсе эпики:");
        for (Epic epic : taskManager.getAllEpics()) {
            printTaskInfo(epic);
        }

        System.out.println("\nИстория:");
        for (Task task : taskManager.getHistory()) {
            printTaskInfo(task);
        }
    }

    private static void printTaskInfo(Task task) {
        System.out.println("ID: " + task.getId());
        System.out.println("Name: " + task.getName());
        System.out.println("Description: " + task.getDescription());
        System.out.println("Status: " + task.getStatus());
        System.out.println("Duration: " + task.getDuration().toMinutes() + " minutes");
        System.out.println("Start Time: " + task.getStartTime());
        System.out.println("End Time: " + task.getEndTime());
        if (task instanceof SubTask) {
            System.out.println("Epic ID: " + ((SubTask) task).getEpicId());
        }
        System.out.println();
    }
}