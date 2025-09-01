package ru.yandex.javacourse.service.handler;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacourse.model.BaseHttpHandler;
import ru.yandex.javacourse.model.EndPoint;
import ru.yandex.javacourse.model.Subtask;
import ru.yandex.javacourse.service.InMemoryTaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static ru.yandex.javacourse.model.EndPoint.*;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private final InMemoryTaskManager taskManager;

    public SubtaskHandler(InMemoryTaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().toString();
        String[] splitPath = path.split("/");

        EndPoint endPoint = getEndPoint(requestMethod, path, splitPath);

        switch (endPoint) {
            case GET_SUBTASKS:
                sendText(httpExchange, gson.toJson(taskManager.getAllSubTasks()), 200);
                break;
            case GET_SUBTASKS_ID:
                try {
                    sendText(httpExchange, gson.toJson(taskManager.getSubtask(Integer.parseInt(splitPath[2]))), 200);
                } catch (RuntimeException e) {
                    sendNotFound(httpExchange, "Subtask c id: " + splitPath[2] + " не существует");
                }
                break;
            case POST_SUBTASKS:
                try {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

                    JsonElement jsonElement = JsonParser.parseString(json);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();

                    if (jsonObject.get("id").isJsonNull()) {

                        Subtask subtask = new Subtask(jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString());
                        taskManager.createSubtasks(subtask);
                        sendText(httpExchange, "Subtask с id: " + subtask.getId() + " успешно создана", 201);

                    } else {

                        try {
                            Subtask existedSubtask = taskManager.getSubtask(Integer.parseInt(jsonObject.get("id").getAsString()));
                            Subtask updatedSubtask = gson.fromJson(json, Subtask.class);
                            taskManager.updateSubtask(updatedSubtask);
                            sendText(httpExchange, "Subtask с id: " + updatedSubtask.getId() + " успешно обновлена " + gson.toJson(taskManager.getSubtask(updatedSubtask.getId())), 201);
                        } catch (RuntimeException e) {
                            sendNotFound(httpExchange, "Subtask c id: " + splitPath[2] + " не существует");
                        }

                    }
                } catch (RuntimeException e) {
                    sendServerError(httpExchange, e.getMessage());
                }
                break;
            case DELETE_SUBTASKS:
                try {
                    taskManager.removeSubtask(Integer.parseInt(splitPath[2]));
                    sendText(httpExchange, "Subtask с id: " + splitPath[2] + " удалена", 200);
                } catch (RuntimeException e) {
                    sendNotFound(httpExchange, "Subtask c id: " + splitPath[2] + " не существует");
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
                    return GET_SUBTASKS;
                } else if (pathLength == 3) {
                    return GET_SUBTASKS_ID;
                }
                break;
            case "POST":
                if (pathLength == 2) {
                    return POST_SUBTASKS;
                }
                break;
            case "DELETE":
                if (pathLength == 3) {
                    return DELETE_SUBTASKS;
                }
                break;
        }
        return UNKNOWN;
    }
}
