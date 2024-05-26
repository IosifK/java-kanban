package ru.yandex.javacource.kulpinov.schedule.manager;

import ru.yandex.javacource.kulpinov.schedule.task.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;
    private static final String HEADER = "id,type,name,status,description,epic";

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    private void save() {
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(HEADER);
            writer.newLine();
            for (Task task : getAllTasks()) {
                writer.write(taskToString(task));
                writer.newLine();
            }
            for (Epic epic : getAllEpics()) {
                writer.write(taskToString(epic));
                writer.newLine();
            }
            for (SubTask subTask : getAllSubTasks()) {
                writer.write(taskToString(subTask));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении в файл", e);
        }
    }


    @Override
    public int addTask(Task task) {
        int id = super.addTask(task);
        save();
        return id;
    }

    @Override
    public Integer addSubTask(SubTask subTask) {
        int id = super.addSubTask(subTask);
        save();
        return id;
    }

    @Override
    public int addEpic(Epic epic) {
        int id = super.addEpic(epic);
        save();
        return id;
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


    private String taskToString(Task task) {
        return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() +
                (task instanceof SubTask ? "," + ((SubTask) task).getEpicId() : "");
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
                return new Task(name, description, status, id);
            case EPIC:
                return new Epic(name, description, status, id);
            case SUBTASK:
                int epicId = Integer.parseInt(fields[5]);
                return new SubTask(name, description, status, epicId, id);
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }



    public static FileBackedTaskManager loadFromFile(File file) {
        final FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        try {
            final String csv = Files.readString(file.toPath());
            final String[] lines = csv.split(System.lineSeparator());
            int generatorId = 0;
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                if (line.isEmpty()) {
                    break;
                }
                final Task task = taskManager.fromString(line);
                final int id = task.getId();
                if (id > generatorId) {
                    generatorId = id;
                }
                taskManager.addAnyTask(task);
            }
            for (Map.Entry<Integer, SubTask> e : taskManager.subTasks.entrySet()) {
                final SubTask subtask = e.getValue();
                final Epic epic = taskManager.epics.get(subtask.getEpicId());
                epic.addSubtask(subtask.getId());
            }
            taskManager.Id = generatorId;
        } catch (IOException e) {
            throw new ManagerSaveException("Can't read form file: " + file.getName(), e);
        }
        return taskManager;
    }

    protected void addAnyTask(Task task) {
        if (task instanceof Epic) {
            epics.put(task.getId(), (Epic) task);
        } else if (task instanceof SubTask) {
            subTasks.put(task.getId(), (SubTask) task);
        } else {
            tasks.put(task.getId(), task);
        }
    }
}







