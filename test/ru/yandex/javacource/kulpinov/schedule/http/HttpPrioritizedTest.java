package ru.yandex.javacource.kulpinov.schedule.http;

import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import ru.yandex.javacource.kulpinov.schedule.manager.InMemoryTaskManager;
import ru.yandex.javacource.kulpinov.schedule.manager.TaskManager;
import ru.yandex.javacource.kulpinov.schedule.task.Status;
import ru.yandex.javacource.kulpinov.schedule.task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class HttpPrioritizedTest {

    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer;
    Gson gson = HttpTaskServer.getGson();

    public HttpPrioritizedTest() throws IOException {
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
    public void testGetPrioritizedTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Задача 1", "Описание", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        Task task2 = new Task("Задача 2", "Описание", Status.NEW, Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(10));
        manager.addTask(task1);
        manager.addTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Task[] prioritizedTasks = gson.fromJson(response.body(), Task[].class);
        assertEquals(2, prioritizedTasks.length, "Вернулось неверное количество задач по приоритетности");
        assertEquals(task1.getName(), prioritizedTasks[0].getName(), "Неверный порядок");
        assertEquals(task2.getName(), prioritizedTasks[1].getName(), "Неверный порядок");
    }
}

