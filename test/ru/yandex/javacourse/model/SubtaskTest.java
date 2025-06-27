package ru.yandex.javacourse.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    @Test
    @DisplayName("Подзадачи равны друг другу, если равен их id")
    void test_SubTasks_Equal_With_EqualID() {

        //given
        Subtask subtask = new Subtask("Test SubTasksEqualWithEqualID", "First Subtask");
        Subtask equalSubtask = new Subtask("Test SubTasksEqualWithEqualID", "Second Subtask");

        //when
        equalSubtask.setId(subtask.getId());

        //then
        assertEquals(subtask, equalSubtask, "Подзадачи не равны друг другу");
    }
}