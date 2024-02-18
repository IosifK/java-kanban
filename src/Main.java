
public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        // создаю задачи
        Task task1 = new Task("Задача 1", "Опсиание 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Опсиание 2", Status.NEW);
        manager.addTask(task1);
        manager.addTask(task2);

        // создаю эпики и подзадачи
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1 ", Status.NEW);
        manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", Status.NEW, epic1.getId());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2", Status.NEW);
        manager.addEpic(epic2);
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание для подзадачи 3", Status.NEW, epic2.getId());
        manager.addSubtask(subtask3);

        // Печать всех задач, подзадач и эпиков
        System.out.println("Все задачи: " + manager.getAllTasks());
        System.out.println("Все подзадачи: " + manager.getAllSubtasks());
        System.out.println("Все эпики: " + manager.getAllEpics());

        // Изменение статусов
        task1.setStatus(Status.DONE);
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.IN_PROGRESS);
        subtask3.setStatus(Status.NEW);
        manager.updateTask(task1);
        manager.updateTask(subtask1);
        manager.updateTask(subtask2);
        manager.updateTask(subtask3);

        // Повторная печать задач после изменения статусов
        System.out.println("Изменили статус: " + manager.getAllTasks());
        System.out.println("Изменили статус: " + manager.getAllSubtasks());
        System.out.println("Изменили статус: " + manager.getAllEpics());

        // Удаление задачи и эпика
        manager.deleteTask(task1.getId());
        manager.deleteTask(epic1.getId());

        // Печать задач после удаления
        System.out.println("Задачи после удаления: " + manager.getAllTasks());
        System.out.println("Подзадачи после удаления: " + manager.getAllSubtasks());
        System.out.println("Эпики после удаления " + manager.getAllEpics());
    }
}

