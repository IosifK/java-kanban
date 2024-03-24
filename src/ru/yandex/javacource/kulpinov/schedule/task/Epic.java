package ru.yandex.javacource.kulpinov.schedule.task;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subTasks;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        this.subTasks = new ArrayList<>();
    }

    public Epic(String name, String description, Status status, int id) {
        super(name, description, status, id);
        this.subTasks = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtasks() {

        return subTasks;
    }


    public void addSubtask(Integer subtask) {

        subTasks.add(subtask);
    }

    public void removeSubtask(Integer subtask) {

        subTasks.remove(subtask);
    }

}
