package ru.yandex.javacource.kulpinov.schedule.manager;

import ru.yandex.javacource.kulpinov.schedule.task.Epic;
import ru.yandex.javacource.kulpinov.schedule.task.Status;
import ru.yandex.javacource.kulpinov.schedule.task.SubTask;
import ru.yandex.javacource.kulpinov.schedule.task.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int Id = 0;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private int generateId() {

        return ++Id;
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
        final Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) {
            return;
        }
        epic.setSubtaskIds(savedEpic.getSubtasks());
        epic.setStatus(savedEpic.getStatus());
        epics.put(epic.getId(), epic);
    }


    @Override
    public void deleteTask(int taskId) {
        tasks.remove(taskId);
        clearHistoryForIds(Collections.singletonList(taskId));

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
        clearHistoryForIds(Collections.singletonList(subTaskId));
    }


    @Override
    public void deleteEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.remove(epicId);
            if (epic != null) {
                List<Integer> subTaskIds = new ArrayList<>(epic.getSubtasks());
                for (Integer subTaskId : subTaskIds) {
                    subTasks.remove(subTaskId);
                }
                clearHistoryForIds(subTaskIds);
            }
            clearHistoryForIds(Collections.singletonList(epicId));
        }
    }

    private void updateEpicStatus(Epic epic) {
        boolean hasNew = false;
        boolean hasInProgress = false;
        boolean hasDone = false;
        for (Integer subTaskID : epic.getSubtasks()) {
            SubTask subTask = subTasks.get(subTaskID);
            if (subTask == null) {
                continue;
            }
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
        clearHistoryForIds(new ArrayList<>(tasks.keySet()));
        tasks.clear();
    }

    @Override
    public void clearEpic() {
        clearHistoryForIds(new ArrayList<>(epics.keySet()));
        clearHistoryForIds(new ArrayList<>(subTasks.keySet()));
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void clearSubtask() {
        clearHistoryForIds(new ArrayList<>(subTasks.keySet()));
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            updateEpicStatus(epic);
        }
    }

    private void clearHistoryForIds(Collection<Integer> ids) {
        for (Integer id : ids) {
            historyManager.remove(id);
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
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public SubTask getSubtaskByID(int id) {
        SubTask subTask = subTasks.get(id);
        historyManager.add(subTask);
        return subTask;
    }

    @Override
    public Epic getEpicByID(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }


    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
