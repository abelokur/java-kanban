package ru.yandex.javacourse.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.model.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class TaskManagerTest<T extends TaskManager> {
    @Test
    @DisplayName("InMemoryTaskManager добавляет задачу и находит её по id")
    void test_AddTask_And_GetByID_() {
        //given
        InMemoryTaskManager inMemoryTaskManager = InMemoryTaskManagerTest.Stub.getInMemoryTaskManager();

        Task task = InMemoryTaskManagerTest.Stub.getTask("Test add and get Task", "DESCRIPTION");
        inMemoryTaskManager.createTasks(task);

        int taskID = task.getId();

        //when
        Task TaskById = inMemoryTaskManager.getTask(taskID);

        //then
        assertEquals( taskID, TaskById.getId(), "Не получили Задание по id.");
    }
}
