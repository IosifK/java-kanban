package ru.yandex.javacource.kulpinov.schedule.manager;

import ru.yandex.javacource.kulpinov.schedule.task.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class InMemoryTaskManager implements TaskManager {
    protected int Id = 0;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, SubTask> subTasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));

    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    protected int generateId() {

        return ++Id;
    }

    @Override
    public int addTask(Task task) {
        checkForIntersections(task);
        final int id = generateId();
        task.setId(id);
        tasks.put(id, task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
        return id;
    }

    @Override
    public Integer addSubTask(SubTask subTask) {
        checkForIntersections(subTask);
        final int epicId = subTask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        final int id = generateId();
        subTask.setId(id);
        subTasks.put(id, subTask);
        epic.addSubtask(subTask.getId());
        if (subTask.getStartTime() != null) {
            prioritizedTasks.add(subTask);
        }
        epic.calculateEpicTimes(getAllSubTasksFromEpic(epicId));
        updateEpicStatus(epic);
        return id;
    }

    public int addEpic(Epic epic) {
        final int id = generateId();
        epic.setId(id);
        epics.put(id, epic);
        return id;

    }


    @Override
    public void updateTask(Task task) {
        prioritizedTasks.remove(task);
        checkForIntersections(task);
        if (task != null && tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
            if (task.getStartTime() != null) {
                prioritizedTasks.add(task);
            }
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        prioritizedTasks.remove(subTask);
        checkForIntersections(subTask);
        if (subTask != null && subTasks.containsKey(subTask.getId()) && epics.containsKey(subTask.getEpicId())) {
            subTasks.put(subTask.getId(), subTask);
            if (subTask.getStartTime() != null) {
                prioritizedTasks.add(subTask);
            }
            Epic epic = epics.get(subTask.getEpicId());
            if (epic != null) {
                epic.addSubtask(subTask.getId());
                epic.calculateEpicTimes(getAllSubTasksFromEpic(epic.getId()));
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
        epic.calculateEpicTimes(getAllSubTasksFromEpic(epic.getId()));
    }


    @Override
    public void deleteTask(int taskId) {
        Task task = tasks.remove(taskId);
        if (task != null) {
            prioritizedTasks.remove(task);
            clearHistoryForIds(Collections.singletonList(taskId));
        }
    }

    @Override
    public void deleteSubTask(Integer subTaskId) {
        SubTask subTask = subTasks.remove(subTaskId);
        if (subTask != null) {
            prioritizedTasks.remove(subTask);
            Epic epic = epics.get(subTask.getEpicId());
            if (epic != null) {
                epic.removeSubtask(subTaskId);
                epic.calculateEpicTimes(getAllSubTasksFromEpic(epic.getId()));
                updateEpicStatus(epic);
            }
        }
        clearHistoryForIds(Collections.singletonList(subTaskId));
    }


    @Override
    public void deleteEpic(int epicId) {
        Epic epic = epics.remove(epicId);
        if (epic != null) {
            List<Integer> subTaskIds = new ArrayList<>(epic.getSubtasks());
            subTaskIds.forEach(subTaskId -> {
                SubTask subTask = subTasks.remove(subTaskId);
                if (subTask != null) {
                    prioritizedTasks.remove(subTask);
                }
            });
            clearHistoryForIds(subTaskIds);
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
        prioritizedTasks.removeIf(task -> task.getType() == TaskType.TASK);
    }

    @Override
    public void clearEpic() {
        clearHistoryForIds(new ArrayList<>(epics.keySet()));
        clearHistoryForIds(new ArrayList<>(subTasks.keySet()));
        epics.clear();
        subTasks.clear();
        prioritizedTasks.removeIf(task -> task.getType() == TaskType.SUBTASK);
    }


    @Override
    public void clearSubtask() {
        clearHistoryForIds(new ArrayList<>(subTasks.keySet()));
        subTasks.clear();
        prioritizedTasks.removeIf(task -> task.getType() == TaskType.SUBTASK);
        epics.values().forEach(epic -> {
            epic.getSubtasks().clear();
            epic.calculateEpicTimes(new ArrayList<>());
            updateEpicStatus(epic);
        });
    }


    private void clearHistoryForIds(Collection<Integer> ids) {
        for (Integer id : ids) {
            historyManager.remove(id);
        }
    }

    private void checkForIntersections(Task newTask) {
        if (newTask.getStartTime() != null) {
            boolean intersects = prioritizedTasks.stream()
                    .filter(task -> !task.equals(newTask))
                    .anyMatch(task -> tasksIntersect(task, newTask));
            if (intersects) {
                throw new IllegalArgumentException("Время задачи пересекается с существующей задачей");
            }
        }
    }


    private boolean tasksIntersect(Task task1, Task task2) {
        if (task1.getStartTime() == null || task2.getStartTime() == null) {
            return false;
        }
        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = task1.getEndTime();
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = task2.getEndTime();
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    @Override
    public ArrayList<SubTask> getAllSubTasksFromEpic(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return new ArrayList<>();
        }
        return epic.getSubtasks().stream()
                .map(subTasks::get)
                .collect(Collectors.toCollection(ArrayList::new));
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

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }


}
