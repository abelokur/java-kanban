package ru.yandex.javacourse.service.handler;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacourse.model.BaseHttpHandler;
import ru.yandex.javacourse.model.EndPoint;
import ru.yandex.javacourse.model.Task;
import ru.yandex.javacourse.service.InMemoryTaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static ru.yandex.javacourse.model.EndPoint.*;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {

    private final InMemoryTaskManager taskManager;

    public TasksHandler(InMemoryTaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().toString();
        String[] splitPath = path.split("/");

        EndPoint endPoint = getEndPoint(requestMethod, path, splitPath);

        switch (endPoint) {
            case GET_TASKS:
                sendText(httpExchange, gson.toJson(taskManager.getAllTasks()), 200);
                break;
            case GET_TASKS_BY_ID:
                try {
                    sendText(httpExchange, gson.toJson(taskManager.getTask(Integer.parseInt(splitPath[2]))), 200);
                } catch (RuntimeException e) {
                    sendNotFound(httpExchange, "Task c id: " + splitPath[2] + " не существует");
                }
                break;
            case POST_TASKS:
                try {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

                    JsonElement jsonElement = JsonParser.parseString(json);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();

                    if (jsonObject.get("id").isJsonNull()) {
                        Task task = new Task(jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString());

                        taskManager.createTasks(task);
                        sendText(httpExchange, "Task с id: " + task.getId() + " успешно создана", 201);
                    } else {
                        try {
                            Task existedTask = taskManager.getTask(Integer.parseInt(jsonObject.get("id").getAsString()));
                            Task updatedTask = gson.fromJson(json, Task.class);
                            taskManager.updateTask(updatedTask);
                            sendText(httpExchange, "Task с id: " + updatedTask.getId() + " успешно обновлена " + gson.toJson(taskManager.getTask(updatedTask.getId())), 201);
                        } catch (RuntimeException e) {
                            sendNotFound(httpExchange, "Task c id: " + splitPath[2] + " не существует");
                        }
                    }

                } catch (RuntimeException e) {
                    sendServerError(httpExchange, e.getMessage());
                }
                break;
            case DELETE_TASKS:
                try {
                    taskManager.removeTask(Integer.parseInt(splitPath[2]));
                    sendText(httpExchange, "Task с id: " + splitPath[2] + " удалена", 200);
                } catch (RuntimeException e) {
                    sendNotFound(httpExchange, "Task c id: " + splitPath[2] + " не существует");
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
                    return GET_TASKS;
                } else if (pathLength == 3) {
                    return GET_TASKS_BY_ID;
                }
                break;
            case "POST":
                if (pathLength == 2) {
                    return POST_TASKS;
                }
                break;
            case "DELETE":
                if (pathLength == 3) {
                    return DELETE_TASKS;
                }
                break;
        }
        return UNKNOWN;
    }
}
