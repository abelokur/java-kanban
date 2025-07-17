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

    @Test
    @DisplayName("при обращении к задаче, предыдущая запись в Истории о задаче удаляется")
    void test_Save_In_History_Previous_Remove_() {
        //given
        InMemoryTaskManager inMemoryTaskManager = Stub.getInMemoryTaskManager();

        Task task1 = new Task("Task1", "DESCRIPTION");
        Task task2 = new Task("Task2", "DESCRIPTION");
        Task task3 = new Task("Task3", "DESCRIPTION");

        inMemoryTaskManager.createTasks(task1);
        inMemoryTaskManager.createTasks(task2);
        inMemoryTaskManager.createTasks(task3);

        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.getTask(task2.getId());
        inMemoryTaskManager.getTask(task3.getId());
        inMemoryTaskManager.getTask(task2.getId());

        //when
        List<Task> history = inMemoryTaskManager.getDefaultHistory();

        //then
        assertEquals(Stub.getHistoryManagerList(), history.toString(), "Если задачу посещали ранее, то в истории НЕ ОСТАЕТСЯ только последний её просмотр");
    }

    @Test
    @DisplayName("История посещений неограниченна по размеру")
    void test_Save_In_History_Unlimited_Quantity_Tasks() {
        //given
        InMemoryTaskManager inMemoryTaskManager = Stub.getInMemoryTaskManager();

        final int QUANTITY = 20;
        Task[] tasks = new Task[QUANTITY];

        Task task;
        for (int i = 0; i < QUANTITY; i++) {
            task = new Task("Task" + i, "DESCRIPTION");
            inMemoryTaskManager.createTasks(task);
            inMemoryTaskManager.getTask(task.getId());
        }

        //when
        List<Task> history = inMemoryTaskManager.getDefaultHistory();
        int historyManagerSize = InMemoryHistoryManager.LinkedListClass.size;

        //then
        assertEquals(QUANTITY, historyManagerSize, "История посещений ОГРАНИЧЕНА по размеру");
    }

    static class Stub {
        public static InMemoryTaskManager getInMemoryTaskManager() {
            return new InMemoryTaskManager();
        }
        public static String getTaskManagerString() {
            return "Task{name='Task1', description='DESCRIPTION', id=0, status=NEW}";
        }
        public static String getHistoryManagerList() {
            return "[Task{name='Task1', description='DESCRIPTION', id=0, status=NEW}, Task{name='Task3', description='DESCRIPTION', id=2, status=NEW}, Task{name='Task2', description='DESCRIPTION', id=1, status=NEW}]";
        }
    }
}