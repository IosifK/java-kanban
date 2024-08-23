package ru.yandex.javacource.kulpinov.schedule.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacource.kulpinov.schedule.manager.TaskManager;
import ru.yandex.javacource.kulpinov.schedule.task.SubTask;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class SubTaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = HttpTaskServer.getGson();

    public SubTaskHandler(TaskManager taskManager) {
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
        if (path.equals("/subtasks")) {
            String response = gson.toJson(taskManager.getAllSubTasks());
            sendText(exchange, response, 200);
        } else if (path.matches("/subtasks/\\d+")) {
            int id = Integer.parseInt(path.split("/")[2]);
            SubTask subTask = taskManager.getSubtaskByID(id);
            if (subTask != null) {
                String response = gson.toJson(subTask);
                sendText(exchange, response, 200);
            } else {
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }


    private void handlePost(HttpExchange exchange) throws IOException {
        InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        SubTask subTask = gson.fromJson(reader, SubTask.class);
        int id = subTask.getId();
        if (id == 0) {
            taskManager.addSubTask(subTask);
            sendText(exchange, "{\"message\":\"SubTask created\"}", 201);
        } else {
            taskManager.updateSubTask(subTask);
            sendText(exchange, "{\"message\":\"SubTask updated\"}", 201);
        }
    }


    private void handleDelete(HttpExchange exchange, String path) throws IOException {
        if (path.matches("/subtasks/\\d+")) {
            int id = Integer.parseInt(path.split("/")[2]);
            taskManager.deleteSubTask(id);
            sendText(exchange, "{\"message\":\"SubTask deleted\"}", 200);
        } else {
            sendNotFound(exchange);
        }
    }
}
