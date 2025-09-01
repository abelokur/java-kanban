package ru.yandex.javacourse.service.handler;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacourse.model.BaseHttpHandler;
import ru.yandex.javacourse.model.EndPoint;
import ru.yandex.javacourse.model.Epic;
import ru.yandex.javacourse.service.InMemoryTaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static ru.yandex.javacourse.model.EndPoint.*;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final InMemoryTaskManager taskManager;

    public EpicHandler(InMemoryTaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().toString();
        String[] splitPath = path.split("/");

        EndPoint endPoint = getEndPoint(requestMethod, path, splitPath);

        switch (endPoint) {
            case GET_EPICS:
                sendText(httpExchange, gson.toJson(taskManager.getAllEpics()), 200);
                break;
            case GET_EPICS_ID:
                try {
                    sendText(httpExchange, gson.toJson(taskManager.getEpic(Integer.parseInt(splitPath[2]))), 200);
                } catch (RuntimeException e) {
                    sendNotFound(httpExchange, "Epic c id: " + splitPath[2] + " не существует");
                }
                break;
            case GET_EPICS_ID_SUBTASKS:
                try {
                    Epic epic = taskManager.getEpic(Integer.parseInt(splitPath[2]));
                    sendText(httpExchange, gson.toJson(epic.getSubTaskList()), 200);
                } catch (RuntimeException e) {
                    sendNotFound(httpExchange, "Epic c id: " + splitPath[2] + " не существует");
                }
                break;
            case POST_EPICS:
                try {
                InputStream inputStream = httpExchange.getRequestBody();
                String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

                JsonElement jsonElement = JsonParser.parseString(json);
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                if (jsonObject.get("id").isJsonNull()) {
                    Epic epic = new Epic(jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString());
                    taskManager.createEpics(epic);
                    sendText(httpExchange, "Epic с id: " + epic.getId() + " успешно создан", 201);
                } else {

                    try {

                        Epic existedEpic = taskManager.getEpic(Integer.parseInt(jsonObject.get("id").getAsString()));
                        Epic updatedEpic = gson.fromJson(json, Epic.class);
                        taskManager.updateEpic(updatedEpic);
                        sendText(httpExchange, "Epic с id: " + updatedEpic.getId() + " успешно обновлена " + gson.toJson(taskManager.getEpic(updatedEpic.getId())), 201);
                    } catch (RuntimeException e) {
                        sendNotFound(httpExchange, "Epic c id: " + splitPath[2] + " не существует");
                    }

                }
                } catch (RuntimeException e) {
                    sendServerError(httpExchange, e.getMessage());
                }
                break;
            case DELETE_EPICS_ID:
                try {
                    taskManager.removeEpic(Integer.parseInt(splitPath[2]));
                    sendText(httpExchange, "Epic с id: " + splitPath[2] + " удален", 200);
                } catch (RuntimeException e) {
                    sendText(httpExchange, "Epic c id: " + splitPath[2] + " не существует", 404);
                }
                break;
            case UNKNOWN:
                sendText(httpExchange, "Передан неизвестный HTTP-метод", 200);
                break;
        }
    }

    private EndPoint getEndPoint(String requestMethod, String path, String[] splitPath) {

        int pathLength = splitPath.length;

        switch (requestMethod) {
            case "GET":
                if (pathLength == 2) {
                    return GET_EPICS;
                } else if (pathLength == 3) {
                    return GET_EPICS_ID;
                } else if (pathLength == 4) {
                    return GET_EPICS_ID_SUBTASKS;
                }
                break;
            case "POST":
                if (pathLength == 2) {
                    return POST_EPICS;
                }
                break;
            case "DELETE":
                if (pathLength == 3) {
                    return DELETE_EPICS_ID;
                }
                break;
        }
        return UNKNOWN;
    }
}
