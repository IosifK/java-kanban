package ru.yandex.javacource.kulpinov.schedule.task;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, Status status, int epicId, int id) {
        super(name, description, status, id);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, Status status, Duration duration, LocalDateTime startTime, int epicId) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, int id, Status status, Duration duration, LocalDateTime startTime, int epicId) {
        super(name, description, id, status, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {

        return epicId;
    }

    public void setEpicId(int epicId) {

        this.epicId = epicId;
    }

    public TaskType getType() {
        return TaskType.SUBTASK;
    }
}
