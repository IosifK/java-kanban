package ru.yandex.javacource.kulpinov.schedule;

import ru.yandex.javacource.kulpinov.schedule.manager.FileBackedTaskManager;
import ru.yandex.javacource.kulpinov.schedule.task.Epic;
import ru.yandex.javacource.kulpinov.schedule.task.Status;
import ru.yandex.javacource.kulpinov.schedule.task.SubTask;
import ru.yandex.javacource.kulpinov.schedule.task.Task;
import ru.yandex.javacource.kulpinov.schedule.manager.Managers;


import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        FileBackedTaskManager taskManager = (FileBackedTaskManager) Managers.getDefault();


        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        taskManager.addTask(task1);

        Task task2 = new Task("Задача 2", "Описание 2", Status.IN_PROGRESS);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", Status.NEW);
        taskManager.addEpic(epic1);

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", Status.NEW, epic1.getId());
        taskManager.addSubTask(subTask1);

        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", Status.DONE, epic1.getId());
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


        System.out.println("\nВзял задачи по ID:");
        Task retrievedTask1 = taskManager.getTaskByID(task1.getId());
        Task retrievedTask2 = taskManager.getTaskByID(task2.getId());


        System.out.println("\nОбновил задачу по номером 1:");
        Task task3 = new Task("Обновленная Задача 3(1)", "Описание 3", Status.IN_PROGRESS);
        task3.setId(task1.getId());
        taskManager.updateTask(task3);
        Task retrievedTask3 = taskManager.getTaskByID(task3.getId());


        System.out.println("\nИстория:");
        for (Task task : taskManager.getHistory()) {
            printTaskInfo(task);
        }


        taskManager = FileBackedTaskManager.loadFromFile(new File("resources/task.csv"));


        System.out.println("\nВсе задачи из файла:");
        List<Task> tasks = taskManager.getAllTasks();
        for (Task task : tasks) {
            printTaskInfo(task);
        }

        System.out.println("\nВсе подзадачи из файла:");
        List<SubTask> subTasks = taskManager.getAllSubTasks();
        for (SubTask subTask : subTasks) {
            printTaskInfo(subTask);
        }

        System.out.println("\nВсе эпики из файла:");
        List<Epic> epics = taskManager.getAllEpics();
        for (Epic epic : epics) {
            printTaskInfo(epic);
        }

        System.out.println("\nИстория из файла:");
        for (Task task : taskManager.getHistory()) {
            printTaskInfo(task);
        }
    }

    private static void printTaskInfo(Task task) {
        System.out.println("ID: " + task.getId());
        System.out.println("Name: " + task.getName());
        System.out.println("Description: " + task.getDescription());
        System.out.println("Status: " + task.getStatus());
        if (task instanceof SubTask) {
            System.out.println("Epic ID: " + ((SubTask) task).getEpicId());
        }
        System.out.println();
    }
}

















