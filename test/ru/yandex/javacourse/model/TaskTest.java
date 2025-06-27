package ru.yandex.javacourse.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {
    @Test
    @DisplayName("Задачи равны друг другу, если равен их id")
    void test_Tasks_Equal_With_EqualID() {

        //given
        Task task = new Task("Test TasksEqualWithEqualID - First Task", "Task.id = Task.id");
        Task equalTask = new Task("Test TasksEqualWithEqualID - Second Task", "Task.id = Task.id");

        //when
        equalTask.setId(task.getId());

        //then
        assertEquals(task, equalTask, "Задачи не равны друг другу.");
    }
}