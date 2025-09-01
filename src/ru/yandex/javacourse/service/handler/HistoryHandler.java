package ru.yandex.javacourse.service.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacourse.model.BaseHttpHandler;
import ru.yandex.javacourse.model.EndPoint;
import ru.yandex.javacourse.service.InMemoryTaskManager;

import java.io.IOException;

import static ru.yandex.javacourse.model.EndPoint.*;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final InMemoryTaskManager taskManager;

    public HistoryHandler(InMemoryTaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().toString();
        String[] splitPath = path.split("/");

        EndPoint endPoint = getEndPoint(requestMethod, path, splitPath);

        switch (endPoint) {
            case GET_HISTORY:
                sendText(httpExchange, gson.toJson(taskManager.getDefaultHistory()), 200);
                break;
            case UNKNOWN:
                sendText(httpExchange, "Передан неизвестный HTTP-метод", 200);
                break;
        }
    }

    private EndPoint getEndPoint(String requestMethod, String path, String[] splitPath) {

        switch (requestMethod) {
            case "GET":
                return GET_HISTORY;
            default:
                return UNKNOWN;
        }
    }
}
