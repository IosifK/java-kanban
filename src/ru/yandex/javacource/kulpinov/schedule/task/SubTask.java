package ru.yandex.javacource.kulpinov.schedule.task;

public class SubTask extends Task {
    private int epicId;
    private int id;

    public SubTask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }
    public SubTask(String name, String description, Status status, int epicId, int id) {
        super(name, description, status, id);
        this.epicId = epicId;
    }


    public int getEpicId() {

        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
