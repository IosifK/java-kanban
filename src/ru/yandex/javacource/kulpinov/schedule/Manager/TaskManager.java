package ru.yandex.javacource.kulpinov.schedule.Manager;
import ru.yandex.javacource.kulpinov.schedule.Task.Epic;
import ru.yandex.javacource.kulpinov.schedule.Task.Status;
import ru.yandex.javacource.kulpinov.schedule.Task.SubTask;
import ru.yandex.javacource.kulpinov.schedule.Task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int Id = 0;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();

    private int generateId() {

        return Id++;
    }

    public void addTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    public void addSubTask(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpicId());
        if (epic != null) {
            subTask.setId(generateId());
            subTasks.put(subTask.getId(), subTask);
            epic.addSubtask(subTask);
            updateEpicStatus(epic);
        } else {
            System.out.println("эпик с id " + subTask.getEpicId() + " не найдет");
        }
    }

    public void addEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }


    public void updateTask(Task task) {
        if (task != null && tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateSubTask(SubTask subTask) {
        if (subTask != null && subTasks.containsKey(subTask.getId()) && epics.containsKey(subTask.getEpicId())) {
            subTasks.put(subTask.getId(), subTask);
            updateSubTaskInEpic(subTask);

        }
    }

    private void updateSubTaskInEpic(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpicId());
        ArrayList<SubTask> list = epic.getSubtasks();
        SubTask oldSubTask = null;

        for (SubTask task : list) {
            if (task.getId() == subTask.getId()) {
                oldSubTask = task;
            }
        }

        if (oldSubTask != null) {
            list.remove(oldSubTask);
        }

        list.add(subTask);
        updateEpicStatus(epic);
    }

    public void updateEpic(Epic epic) {
        if (epic != null && epics.containsKey(epic.getId())) {
            Epic oldEpic = epics.get(epic.getId());
            for (SubTask subtask : oldEpic.getSubtasks()) {
                subTasks.remove(subtask.getId());
            }
            for (SubTask subtask : epic.getSubtasks()) {
                addSubtask2(subtask);
            }
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic);
        }
    }

    private void addSubtask2(SubTask subtask) {
        int id = generateId();
        subtask.setId(id);
        subTasks.put(id, subtask);
    }


    public void deleteTask(int taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId);
        }
    }

    public void deleteSubTask(int subTaskId) {
        if (subTasks.containsKey(subTaskId)) {
            SubTask subTask = subTasks.remove(subTaskId);
            Epic epic = epics.get(subTask.getEpicId());
            if (epic != null) {
                epic.removeSubtask(subTask);
                updateEpicStatus(epic);
            }
        }
    }

    public void deleteEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.remove(epicId);
            for (SubTask subTask : epic.getSubtasks()) {
                subTasks.remove(subTask.getId());
            }
        }
    }


    private void updateEpicStatus(Epic epic) {
        boolean hasNew = false;
        boolean hasInProgress = false;
        boolean hasDone = false;
        for (SubTask subTask : epic.getSubtasks()) {
            switch (subTask.getStatus()) {
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

    public ArrayList<SubTask> getAllSubTasks() {

        return new ArrayList<>(subTasks.values());
    }

    public ArrayList<Epic> getAllEpics() {

        return new ArrayList<>(epics.values());
    }

    public void clearTask() {
        tasks.clear();
    }

    public void clearEpic() {
        epics.clear();
        subTasks.clear();
    }

    public void clearSubtask() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            updateEpicStatus(epic);
        }
    }

    public ArrayList<SubTask> getAllSubTasksFromEpic(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<SubTask> result = new ArrayList<>();

        if (epic != null) {
            for (SubTask subTask : epic.getSubtasks()) {
                result.add(subTask);
            }
        }
        return result;
    }
}
