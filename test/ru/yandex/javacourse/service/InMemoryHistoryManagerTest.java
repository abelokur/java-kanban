package ru.yandex.javacourse.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.model.Subtask;
import ru.yandex.javacourse.model.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    @Test
    @DisplayName("задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных")
    void test_Save_In_History() {
        //given
        InMemoryTaskManager inMemoryTaskManager = Stub.getInMemoryTaskManager();

        Task task1 = new Task("Task1", "DESCRIPTION");

        inMemoryTaskManager.createTasks(task1);
        inMemoryTaskManager.getTask(task1.getId());

        //when
        List<Task> history = inMemoryTaskManager.getDefaultHistory();

        //then
        assertEquals(Stub.getTaskManagerString(), history.get(0).toString(), "Задача добавляемая в HistoryManager не сохраняет предыдущую версию задачи и её данные");
    }
    static class Stub {
        public static InMemoryTaskManager getInMemoryTaskManager() {
            return new InMemoryTaskManager();
        }
        public static String getTaskManagerString() {
            return "Task{name='Task1', description='DESCRIPTION', id=0, status=NEW}";
        }
    }

}