package ru.yandex.javacource.kulpinov.schedule.http;


import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacource.kulpinov.schedule.manager.TaskManager;
import ru.yandex.javacource.kulpinov.schedule.task.Task;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = HttpTaskServer.getGson();

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            if (method.equals("GET")) {
                handleGet(exchange, path);
            } else if (method.equals("POST")) {
                handlePost(exchange);
            } else if (method.equals("DELETE")) {
                handleDelete(exchange, path);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendInternalServerError(exchange);
        }
    }

    private void handleGet(HttpExchange exchange, String path) throws IOException {
        if (path.equals("/tasks")) {
            String response = gson.toJson(taskManager.getAllTasks());
            sendText(exchange, response, 200);
        } else if (path.matches("/tasks/\\d+")) {
            int id = Integer.parseInt(path.split("/")[2]);
            Task task = taskManager.getTaskByID(id);
            if (task != null) {
                String response = gson.toJson(task);
                sendText(exchange, response, 200);
            } else {
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        try {
            InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
            Task task = gson.fromJson(reader, Task.class);
            int id = task.getId();
            if (id == 0) {
                taskManager.addTask(task);
                sendText(exchange, "{\"message\":\"Task created\"}", 201);
            } else {
                taskManager.updateTask(task);
                sendText(exchange, "{\"message\":\"Task updated\"}", 201);
            }
        } catch (IllegalArgumentException e) {
            sendHasInteractions(exchange);
        }
    }

    private void handleDelete(HttpExchange exchange, String path) throws IOException {
        if (path.matches("/tasks/\\d+")) {
            int id = Integer.parseInt(path.split("/")[2]);
            taskManager.deleteTask(id);
            sendText(exchange, "{\"message\":\"Task deleted\"}", 200);
        } else {
            sendNotFound(exchange);
        }
    }
}




