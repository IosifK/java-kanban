package ru.yandex.javacource.kulpinov.schedule;

import ru.yandex.javacource.kulpinov.schedule.manager.TaskManager;
import ru.yandex.javacource.kulpinov.schedule.task.Epic;
import ru.yandex.javacource.kulpinov.schedule.task.Status;
import ru.yandex.javacource.kulpinov.schedule.task.SubTask;
import ru.yandex.javacource.kulpinov.schedule.task.Task;
import ru.yandex.javacource.kulpinov.schedule.manager.Managers;


public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();


        Task task1 = new Task("Задача 1", "Описнаие 1", Status.NEW);
        taskManager.addTask(task1);

        Task task2 = new Task("Задача 2", "Описание 2", Status.IN_PROGRESS);
        taskManager.addTask(task2);

        Task task3 = new Task("Задача 3", "Описание 3", Status.NEW, 2);
        taskManager.addTask(task3);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", Status.NEW);
        taskManager.addEpic(epic1);

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадача 1", Status.NEW, epic1.getId());
        taskManager.addSubTask(subTask1);

        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", Status.DONE, epic1.getId());
        taskManager.addSubTask(subTask2);

        SubTask subTask3 = new SubTask("Подзадача 3", "Описание подзадачи 3", Status.IN_PROGRESS, epic1.getId(), 5);
        taskManager.addSubTask(subTask3);

        Epic epic2 = new Epic("Эпик 1", "Описание эпика 1", Status.NEW, 6);
        taskManager.addEpic(epic2);


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


        System.out.println("\nВзять задачу по ID и история:");
        Task retrievedTask = taskManager.getTaskByID(task1.getId());
        printTaskInfo(retrievedTask);

        Task retrievedSubTask1 = taskManager.getSubtaskByID(subTask1.getId());
        printTaskInfo(retrievedSubTask1);

        System.out.println("\nИстория:");
        for (Task task : taskManager.getHistory()) {
            printTaskInfo(task);
        }


        task1.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task1);

        epic1.setName("Обновленый Эпик 1");
        taskManager.updateEpic(epic1);


        taskManager.deleteTask(task1.getId());
        taskManager.deleteSubTask(subTask1.getId());
        taskManager.deleteEpic(epic1.getId());


        taskManager.clearTask();
        taskManager.clearSubtask();
        taskManager.clearEpic();
    }


    private static void printTaskInfo(Task task) {
        System.out.println("ID: " + task.getId());
        System.out.println("Name: " + task.getName());
        System.out.println("Description: " + task.getDescription());
        System.out.println("Status: " + task.getStatus());
        System.out.println();
    }
}






