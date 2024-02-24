package ru.yandex.javacource.kulpinov.schedule.manager;
import ru.yandex.javacource.kulpinov.schedule.task.Epic;
import ru.yandex.javacource.kulpinov.schedule.task.Status;
import ru.yandex.javacource.kulpinov.schedule.task.SubTask;
import ru.yandex.javacource.kulpinov.schedule.task.Task;

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
            epic.addSubtask(subTask.getId());
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
            Epic epic = epics.get(subTask.getEpicId());
            if (epic != null) {
                epic.addSubtask(subTask.getId());
                updateEpicStatus(epic);
            }
        }
    }

    public void updateEpic(Epic epic) {
        Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) {
            return;
        }
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
    }


    public void deleteTask(int taskId) {
            tasks.remove(taskId);

    }

    public void deleteSubTask(Integer subTaskId) {
        SubTask subTask = subTasks.remove(subTaskId);
        if (subTask != null) {
            Epic epic = epics.get(subTask.getEpicId());
            if (epic != null) {
                epic.removeSubtask(subTaskId);
                updateEpicStatus(epic);
            }
        }
    }


    public void deleteEpic(int epicId) {
        Epic epic = epics.remove(epicId);
        if (epic != null) {
            for (Integer subTask : epic.getSubtasks()) {
                subTasks.remove(subTask);
            }
        }
    }



    private void updateEpicStatus(Epic epic) {
        boolean hasNew = false;
        boolean hasInProgress = false;
        boolean hasDone = false;
        for (Integer subTaskID : epic.getSubtasks()) {
            SubTask subTask = subTasks.get(subTaskID);
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
            for (Integer subTaskID : epic.getSubtasks()) {
                SubTask subTask = subTasks.get(subTaskID);
                result.add(subTask);
            }
        }
        return result;
    }
}
