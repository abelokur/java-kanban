package ru.yandex.javacourse.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {
    @Test
    void TasksEqualWithEqualID() {

        Task task = new Task("Test TasksEqualWithEqualID - First Task", "Task.id = Task.id");
        Task equalTask = new Task("Test TasksEqualWithEqualID - Second Task", "Task.id = Task.id");

        equalTask.setId(task.getId());

        assertEquals(task, equalTask, "Задачи не равны друг другу.");
    }
}