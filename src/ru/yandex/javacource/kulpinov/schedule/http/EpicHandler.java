package ru.yandex.javacource.kulpinov.schedule.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacource.kulpinov.schedule.manager.TaskManager;
import ru.yandex.javacource.kulpinov.schedule.task.Epic;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class EpicHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = HttpTaskServer.getGson();

    public EpicHandler(TaskManager taskManager) {
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
        if (path.equals("/epics")) {
            String response = gson.toJson(taskManager.getAllEpics());
            sendText(exchange, response, 200);
        } else if (path.matches("/epics/\\d+")) {
            int id = Integer.parseInt(path.split("/")[2]);
            Epic epic = taskManager.getEpicByID(id);
            if (epic != null) {
                String response = gson.toJson(epic);
                sendText(exchange, response, 200);
            } else {
                sendNotFound(exchange);
            }
        } else if (path.matches("/epics/\\d+/subtasks")) {
            int id = Integer.parseInt(path.split("/")[2]);
            String response = gson.toJson(taskManager.getAllSubTasksFromEpic(id));
            sendText(exchange, response, 200);
        } else {
            sendNotFound(exchange);
        }
    }


    private void handlePost(HttpExchange exchange) throws IOException {
        InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        Epic epic = gson.fromJson(reader, Epic.class);
        int id = epic.getId();
        if (id == 0) {
            taskManager.addEpic(epic);
            sendText(exchange, "{\"message\":\"Epic created\"}", 201);
        } else {
            taskManager.updateEpic(epic);
            sendText(exchange, "{\"message\":\"Epic updated\"}", 201);
        }
    }


    private void handleDelete(HttpExchange exchange, String path) throws IOException {
        if (path.matches("/epics/\\d+")) {
            int id = Integer.parseInt(path.split("/")[2]);
            taskManager.deleteEpic(id);
            sendText(exchange, "{\"message\":\"Epic deleted\"}", 200);
        } else {
            sendNotFound(exchange);
        }
    }
}
