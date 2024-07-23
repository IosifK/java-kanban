package ru.yandex.javacource.kulpinov.schedule.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTasks;
    private LocalDateTime endTime;


    public Epic(String name, String description, Status status) {
        super(name, description, status);
        this.subTasks = new ArrayList<>();
    }

    public Epic(String name, String description, Status status, int id) {
        super(name, description, status, id);
        this.subTasks = new ArrayList<>();
    }

    public Epic(String name, String description, Status status, int id, Duration duration, LocalDateTime startTime, LocalDateTime endTime) {
        super(name, description, id, status, duration, startTime);
        this.subTasks = new ArrayList<>();
        this.endTime = endTime;
    }


    public List<Integer> getSubtasks() {

        return subTasks;
    }


    public void addSubtask(Integer subtask) {
        subTasks.add(subtask);
    }

    public void setSubtaskIds(List<Integer> addSubTasks) {
        subTasks = addSubTasks;
    }

    public void removeSubtask(Integer subtask) {

        subTasks.remove(subtask);
    }

    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

}
