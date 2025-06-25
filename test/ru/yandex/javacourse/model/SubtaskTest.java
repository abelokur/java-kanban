package ru.yandex.javacourse.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    @Test
    void SubTasksEqualWithEqualID() {
        Subtask subtask = new Subtask("Test SubTasksEqualWithEqualID", "First Subtask");
        Subtask equalSubtask = new Subtask("Test SubTasksEqualWithEqualID", "Second Subtask");

        equalSubtask.setId(subtask.getId());

        assertEquals(subtask, equalSubtask, "Подзадачи не равны друг другу");
    }
}