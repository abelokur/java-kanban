package ru.yandex.javacourse.service;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.javacourse.service.handler.*;


import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private final HttpServer httpServer;
    private static final Gson gson = new Gson();

    public HttpTaskServer(InMemoryTaskManager/*TaskManager*/ taskManager) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);
        httpServer.createContext("/tasks", new TasksHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }

    public void start() {
        httpServer.start();
        System.out.println("Сервер запущен");
    }

    public void stop() {
        httpServer.stop(1);
    }

    public static void main(String[] args) throws IOException {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }
}
