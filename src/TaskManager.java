import java.util.ArrayList;
import java.util.HashMap;

class TaskManager {
    private int Id = 0;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();

    private int generateId() {

        return Id++;
    }

    public void addTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    public void addSubtask(Subtask subtask) {
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask);
        updateEpicStatus(epic);
    }

    public void addEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }


    public void updateTask(Task task) {
        if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                updateEpicStatus(epic);
            }
        } else if (task instanceof Epic) {
            Epic epic = (Epic) task;
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic);
        } else {
            tasks.put(task.getId(), task);
        }
    }

    public void deleteTask(int id) {
        tasks.remove(id);
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.remove(id);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtask(subtask);
                updateEpicStatus(epic);
            }
        } else if (epics.containsKey(id)) {
            Epic epic = epics.remove(id);
            for (Subtask subtask : epic.getSubtasks()) {
                subtasks.remove(subtask.getId());
            }
        }
    }

    private void updateEpicStatus(Epic epic) {
        boolean hasNew = false;
        boolean hasInProgress = false;
        boolean hasDone = false;
        for (Subtask subtask : epic.getSubtasks()) {
            switch (subtask.getStatus()) {
                case NEW:
                    hasNew = true;
                    break;
                case IN_PROGRESS:
                    hasInProgress = true;
                    break;
                case DONE:
                    hasDone = true;
                    break;
            }
        }
        if (hasInProgress || (hasNew && hasDone)) {
            epic.setStatus(Status.IN_PROGRESS);
        } else if (hasDone && !hasNew) {
            epic.setStatus(Status.DONE);
        } else epic.setStatus(Status.NEW);

    }

    public ArrayList<Task> getAllTasks() {

        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {

        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Epic> getAllEpics() {

        return new ArrayList<>(epics.values());
    }
}
