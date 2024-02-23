package ru.yandex.javacource.kulpinov.schedule;
import ru.yandex.javacource.kulpinov.schedule.Manager.TaskManager;
import ru.yandex.javacource.kulpinov.schedule.Task.Epic;
import ru.yandex.javacource.kulpinov.schedule.Task.Status;
import ru.yandex.javacource.kulpinov.schedule.Task.SubTask;
import ru.yandex.javacource.kulpinov.schedule.Task.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        // создаю задачи
        Task task1 = new Task("Задача 1", "Опсиание 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Опсиание 2", Status.NEW);
        manager.addTask(task1);
        manager.addTask(task2);

        // создаю эпики и подзадачи
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1 ", Status.NEW);
        manager.addEpic(epic1);
        SubTask subTask1 = new SubTask("Подзадача 1", "Описание 1", Status.NEW, epic1.getId());
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание 2", Status.NEW, epic1.getId());
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);

        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2", Status.NEW);
        manager.addEpic(epic2);
        SubTask subTask3 = new SubTask("Подзадача 3", "Описание для подзадачи 3", Status.NEW, epic2.getId());
        manager.addSubTask(subTask3);

        // Печать всех задач, подзадач и эпиков
        System.out.println("Все задачи: " + manager.getAllTasks());
        System.out.println("Все подзадачи: " + manager.getAllSubTasks());
        System.out.println("Все эпики: " + manager.getAllEpics());

        // Изменение статусов
        task1.setStatus(Status.DONE);
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.IN_PROGRESS);
        subTask3.setStatus(Status.NEW);
        manager.updateTask(task1);
        manager.updateTask(subTask1);
        manager.updateTask(subTask2);
        manager.updateTask(subTask3);

        // Повторная печать задач после изменения статусов
        System.out.println("Изменили статус: " + manager.getAllTasks());
        System.out.println("Изменили статус: " + manager.getAllSubTasks());
        System.out.println("Изменили статус: " + manager.getAllEpics());

        // Удаление задачи и эпика
        manager.deleteTask(task1.getId());
        manager.deleteTask(epic1.getId());

        // Печать задач после удаления
        System.out.println("Задачи после удаления: " + manager.getAllTasks());
        System.out.println("Подзадачи после удаления: " + manager.getAllSubTasks());
        System.out.println("Эпики после удаления " + manager.getAllEpics());

        manager.deleteSubTask(subTask1.getId());
        System.out.println("Эпик после удаления подзадачи: " + manager.getAllSubTasksFromEpic(epic1.getId()));
        System.out.println("Статус эпика 1: " + epic1.getStatus());


        System.out.println("подзадачт эпика 1: " + manager.getAllSubTasksFromEpic(epic1.getId()));

        manager.deleteSubTask(subTask1.getId());
        System.out.println("Эпик после удаления подзадач: " + manager.getAllSubTasksFromEpic(epic1.getId()));

        System.out.println("Все задачи: " + manager.getAllTasks());
        System.out.println("Все подзадачи: " + manager.getAllSubTasks());
        System.out.println("Все эпики: " + manager.getAllEpics());

    }
}

