package ru.yandex.javacource.kulpinov.schedule.manager;

import ru.yandex.javacource.kulpinov.schedule.task.Epic;
import ru.yandex.javacource.kulpinov.schedule.task.Status;
import ru.yandex.javacource.kulpinov.schedule.task.SubTask;
import ru.yandex.javacource.kulpinov.schedule.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int Id = 0;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private int generateId() {

        return ++Id;
    }


    private boolean isIdOccupied(int id) {
        boolean meaning = true;
        if (id != 0) {
            if (tasks.containsKey(id) || subTasks.containsKey(id) || epics.containsKey(id)) {
                meaning = false;
            }
        }
        return meaning;
    }

    @Override
    public void addTask(Task task) {
        if (task.getId() == 0) {
            task.setId(generateId());
            tasks.put(task.getId(), task);
        } else if (isIdOccupied(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }


    @Override
    public void addSubTask(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpicId());
        if (epic == null) {
            System.out.println("эпик с id " + subTask.getEpicId() + " не найдет");
            return;
        }
        if (subTask.getId() == 0) {
            subTask.setId(generateId());
            subTasks.put(subTask.getId(), subTask);
            epic.addSubtask(subTask.getId());
            updateEpicStatus(epic);
        } else if (isIdOccupied(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            epic.addSubtask(subTask.getId());
            updateEpicStatus(epic);
        }
    }


    @Override
    public void addEpic(Epic epic) {
        if(epic.getId() == 0) {
            epic.setId(generateId());
            epics.put(epic.getId(), epic);
        } else if (isIdOccupied(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }


    @Override
    public void updateTask(Task task) {
        if (task != null && tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
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

    @Override
    public void updateEpic(Epic epic) {
        Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) {
            return;
        }
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
    }


    @Override
    public void deleteTask(int taskId) {
        tasks.remove(taskId);

    }

    @Override
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


    @Override
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

    @Override
    public ArrayList<Task> getAllTasks() {

        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {

        return new ArrayList<>(subTasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {

        return new ArrayList<>(epics.values());
    }

    @Override
    public void clearTask() {
        tasks.clear();
    }

    @Override
    public void clearEpic() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void clearSubtask() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            updateEpicStatus(epic);
        }
    }

    @Override
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

    @Override
    public Task getTaskByID(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public SubTask getSubtaskByID(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public Epic getEpicByID(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
