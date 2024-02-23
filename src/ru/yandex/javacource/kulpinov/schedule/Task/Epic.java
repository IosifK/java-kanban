package ru.yandex.javacource.kulpinov.schedule.Task;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<SubTask> subTasks;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        this.subTasks = new ArrayList<>();
    }

    public ArrayList<SubTask> getSubtasks() {

        return subTasks;
    }


    public void addSubtask(SubTask subtask) {

        subTasks.add(subtask);
    }

    public void removeSubtask(SubTask subtask) {

        subTasks.remove(subtask);
    }

}

