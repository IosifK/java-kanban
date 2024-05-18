package ru.yandex.javacource.kulpinov.schedule.manager;

import ru.yandex.javacource.kulpinov.schedule.task.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    private void save() {
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write("id,type,name,status,description,epic");
            writer.newLine();
            for (Task task : getAllTasks()) {
                writer.write(toString(task));
                writer.newLine();
            }
            for (Epic epic : getAllEpics()) {
                writer.write(toString(epic));
                writer.newLine();
            }
            for (SubTask subTask : getAllSubTasks()) {
                writer.write(toString(subTask));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении в файл", e);
        }
    }


    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteTask(int taskId) {
        super.deleteTask(taskId);
        save();
    }

    @Override
    public void deleteSubTask(Integer subTaskId) {
        super.deleteSubTask(subTaskId);
        save();
    }

    @Override
    public void deleteEpic(int epicId) {
        super.deleteEpic(epicId);
        save();
    }

    @Override
    public void clearTask() {
        super.clearTask();
        save();
    }

    @Override
    public void clearEpic() {
        super.clearEpic();
        save();
    }

    @Override
    public void clearSubtask() {
        super.clearSubtask();
        save();
    }

    private String toString(Task task) {
        if (task instanceof Epic) {
            return taskToString(task, TaskType.EPIC);
        } else if (task instanceof SubTask) {
            return taskToString(task, TaskType.SUBTASK);
        } else {
            return taskToString(task, TaskType.TASK);
        }
    }

    private String taskToString(Task task, TaskType type) {
        StringBuilder string = new StringBuilder();
        string.append(task.getId()).append(",");
        string.append(type).append(",");
        string.append(task.getName()).append(",");
        string.append(task.getStatus()).append(",");
        string.append(task.getDescription());

        if (task instanceof SubTask) {
            SubTask subTask = (SubTask) task;
            string.append(",").append(subTask.getEpicId());
        }

        return string.toString();
    }

    private Task fromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        TaskType type = TaskType.valueOf(fields[1]);
        String name = fields[2];
        Status status = Status.valueOf(fields[3]);
        String description = fields[4];

        switch (type) {
            case TASK:
                Task task = new Task(name, description, status);
                task.setId(id);
                return task;
            case EPIC:
                Epic epic = new Epic(name, description, status);
                epic.setId(id);
                return epic;
            case SUBTASK:
                int epicId = Integer.parseInt(fields[5]);
                SubTask subTask = new SubTask(name, description, status, epicId);
                subTask.setId(id);
                return subTask;
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        Map<Integer, SubTask> subTasks = new HashMap<>();
        Map<Integer, Epic> epics = new HashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                Task task = manager.fromString(line);
                if (task instanceof Epic) {
                    Epic epic = (Epic) task;
                    manager.addEpic(epic);
                    epics.put(epic.getId(), epic);
                } else if (task instanceof SubTask) {
                    SubTask subTask = (SubTask) task;
                    subTasks.put(subTask.getId(), subTask);
                } else {
                    manager.addTask(task);
                }
            }

            for (SubTask subTask : subTasks.values()) {
                Epic epic = epics.get(subTask.getEpicId());
                if (epic != null) {
                    epic.addSubtask(subTask.getId());
                    manager.addSubTask(subTask);
                } else {
                    System.err.println("Ошибка: эпик с ID " + subTask.getEpicId() + " не найден для подзадачи с ID " + subTask.getId());
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке из файла", e);
        }
        return manager;
    }

}







