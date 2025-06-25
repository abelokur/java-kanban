package ru.yandex.javacourse.service;

import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.model.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    @Test
    void saveInHistory() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Task task1 = new Task("Task1", "DESCRIPTION");

        inMemoryTaskManager.createTasks(task1);
        inMemoryTaskManager.getTask(task1.getId());

        List<Task> history = inMemoryTaskManager.getDefaultHistory();
        assertEquals("Task{name='Task1', description='DESCRIPTION', id=0, status=NEW}", history.get(0).toString(), "Задача добавляемая в HistoryManager не сохраняет предыдущую версию задачи и её данные");
    }

}