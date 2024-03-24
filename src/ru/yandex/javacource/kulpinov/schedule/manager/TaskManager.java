package ru.yandex.javacource.kulpinov.schedule.manager;

import ru.yandex.javacource.kulpinov.schedule.task.Epic;
import ru.yandex.javacource.kulpinov.schedule.task.SubTask;
import ru.yandex.javacource.kulpinov.schedule.task.Task;

import java.util.ArrayList;
import java.util.List;


public interface TaskManager {
    void addTask(Task task);

    void addSubTask(SubTask subTask);

    void addEpic(Epic epic);

    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);

    void deleteTask(int taskId);

    void deleteSubTask(Integer subTaskId);

    void deleteEpic(int epicId);

    ArrayList<Task> getAllTasks();

    ArrayList<SubTask> getAllSubTasks();

    ArrayList<Epic> getAllEpics();

    void clearTask();

    void clearEpic();

    void clearSubtask();

    ArrayList<SubTask> getAllSubTasksFromEpic(int epicId);

    Task getTaskByID(int id);

    SubTask getSubtaskByID(int id);

    Epic getEpicByID(int id);

    List<Task> getHistory();
}
