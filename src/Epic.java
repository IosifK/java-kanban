import java.util.ArrayList;

class Epic extends Task {
    private final ArrayList<Subtask> subtasks;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        this.subtasks = new ArrayList<>();
    }

    public ArrayList<Subtask> getSubtasks() {

        return subtasks;
    }

    public void addSubtask(Subtask subtask) {

        subtasks.add(subtask);
    }

    public void removeSubtask(Subtask subtask) {

        subtasks.remove(subtask);
    }

}

