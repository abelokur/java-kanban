package ru.yandex.javacourse.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.model.Task;
import ru.yandex.javacourse.service.handler.DurationAdapter;
import ru.yandex.javacourse.service.handler.LocalDateTimeAdapter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {

    private InMemoryTaskManager taskManager = new InMemoryTaskManager();
    private HttpTaskServer httpTaskServer;

    private final HttpClient client = HttpClient.newHttpClient();
    private final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @BeforeEach
    public void startHttpServer() throws IOException  {

        taskManager.removeTasks();
        taskManager.removeSubtasks();
        taskManager.removeEpics();
        taskManager = new InMemoryTaskManager();
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }

    @Test
    @DisplayName("Создается Task")
    void test_Create_Task () throws IOException, InterruptedException {

        //given
        URI uri = Stub.getUriTasks();

        Task task = new Task("NAME7", "DESCRIPTION7");
        int id = task.getId();
        task.setId(id + 1);

        String json = "{" +
                "\"name\": \"NAME7\"," +
                "\"description\": \"DESCRIPTION7\"," +
                "\"id\" : null" +
                "}";

        //when
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(uri)
                .build();

        HttpResponse<String> response = client.send(request, handler);
        System.out.println(taskManager.getAllTasks());

        //then
        assertEquals(task.toString(), taskManager.getTask(id + 1).toString());
        assertEquals(201, response.statusCode());
    }

    @Test
    @DisplayName("Обновляется Task")
    void test_Update_Task() throws IOException, InterruptedException {

        //given
        URI uri = Stub.getUriTasks();

        Task task = new Task("NAME7", "DESCRIPTION7");
        int id = task.getId();
        task.setId(id + 1);
        task.setName("NAME7777");
        task.setDescription("DESCRIPTION7777");
        task.setDuration(Duration.ofSeconds(10));

        String json = "{" +
                "\"name\": \"NAME7\"," +
                "\"description\": \"DESCRIPTION7\"," +
                "\"id\" : null" +
                "}";

        String updateJson = "{" +
                "\"name\": \"NAME7777\"," +
                "\"description\": \"DESCRIPTION7777\"," +
                "\"id\": " + (id + 1) + "," +
                "\"status\": \"NEW\"," +
                "\"duration\": 10" +
                "}";

        //when
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, handler);

        body = HttpRequest.BodyPublishers.ofString(updateJson);
        request = HttpRequest.newBuilder()
                .POST(body)
                .uri(uri)
                .build();
        response = client.send(request, handler);
        System.out.println(taskManager.getAllTasks());

        //then
        assertEquals(task.toString(), taskManager.getTask(id + 1).toString());
        assertEquals(201, response.statusCode());
    }

    @Test
    @DisplayName("Получаем все Task")
    void test_Get_All_Task() throws IOException, InterruptedException {

        //given
        URI uri = Stub.getUriTasks();

        Task task = new Task("NAME7", "DESCRIPTION7");

        String json = "{" +
                "\"name\": \"NAME7\"," +
                "\"description\": \"DESCRIPTION7\"," +
                "\"id\" : null" +
                "}";

        //when
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(uri)
                .build();
        HttpResponse<String> response = client.send(request, handler);

        request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        response = client.send(request, handler);

        //then
        System.out.println(response.statusCode());
        assertEquals(Stub.getAllTaskManagerString(), taskManager.getAllTasks().toString());
        assertEquals(200, response.statusCode());
    }

    @AfterEach
    public void stopHttpServer() {
        httpTaskServer.stop();
    }

    static class Stub {
        public static String getAllTaskManagerString() {
            return "{1=Task{name='NAME7', description='DESCRIPTION7', id=1, status=NEW, duration=PT0S, startTime=null}}";
        }
        public static URI getUriTasks () {
            return URI.create("http://localhost:8080/tasks");
        }
        public static URI getUriTasksId () {
            return URI.create("http://localhost:8080/tasks");
        }
    }
}
