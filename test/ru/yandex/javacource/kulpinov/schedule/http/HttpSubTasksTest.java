package ru.yandex.javacource.kulpinov.schedule.http;

import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import ru.yandex.javacource.kulpinov.schedule.manager.InMemoryTaskManager;
import ru.yandex.javacource.kulpinov.schedule.manager.TaskManager;
import ru.yandex.javacource.kulpinov.schedule.task.Epic;
import ru.yandex.javacource.kulpinov.schedule.task.Status;
import ru.yandex.javacource.kulpinov.schedule.task.SubTask;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpSubTasksTest {

    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer;
    Gson gson = HttpTaskServer.getGson();

    public HttpSubTasksTest() throws IOException {
        taskServer = new HttpTaskServer(manager);
    }

    @BeforeEach
    public void setUp() {
        manager.clearTask();
        manager.clearSubtask();
        manager.clearEpic();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание", Status.NEW);
        int epicId = manager.addEpic(epic);

        SubTask subTask = new SubTask("Подзадача 1", "Описание", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now(), epicId);
        String subTaskJson = gson.toJson(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subTaskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<SubTask> subTasksFromManager = manager.getAllSubTasks();
        assertNotNull(subTasksFromManager, "Подзадачи не вернулись");
        assertEquals(1, subTasksFromManager.size(), "Неверное количество подзадач");
        assertEquals("Подзадача 1", subTasksFromManager.get(0).getName(), "Неверное имя подзадачи");
    }

    @Test
    public void testGetAllSubTasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание", Status.NEW);
        int epicId = manager.addEpic(epic);

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now(), epicId);
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание", Status.NEW, Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(10), epicId);
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        SubTask[] subTasks = gson.fromJson(response.body(), SubTask[].class);
        assertEquals(2, subTasks.length, "Вернулась неверное количество подзадач");
    }

    @Test
    public void testGetSubTaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание", Status.NEW);
        int epicId = manager.addEpic(epic);

        SubTask subTask = new SubTask("Подзадача 1", "Описание", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now(), epicId);
        int subTaskId = manager.addSubTask(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subTaskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        SubTask returnedSubTask = gson.fromJson(response.body(), SubTask.class);
        assertEquals(subTask.getName(), returnedSubTask.getName(), "Неверное имя подзадачи");
    }

    @Test
    public void testDeleteSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик 1", "Описание ", Status.NEW);
        int epicId = manager.addEpic(epic);

        SubTask subTask = new SubTask("Подзадача 1", "Описание", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now(), epicId);
        int subTaskId = manager.addSubTask(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subTaskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<SubTask> subTasksFromManager = manager.getAllSubTasks();
        assertEquals(0, subTasksFromManager.size(), "Подзадача не удалилась");
    }
}

