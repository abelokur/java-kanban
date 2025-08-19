package ru.yandex.javacourse.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    @DisplayName("Задачи пересекаются")
    void test_Tasks_Is_Overlapping() {

        //given
        Task task1 = new Task("First Task", "Interval is overlapping with Second Task");
        Task task2 = new Task("Second Task", "Interval is overlapping with First Task");
        DateTimeFormatter formater = Stub.formater;

        //when
        task1.setStartTime(LocalDateTime.parse("16.08.2025 08:25", formater));
        task1.setDuration(Duration.ofHours(1));

        task2.setStartTime(LocalDateTime.parse("16.08.2025 09:05", formater));
        task2.setDuration(Duration.ofHours(1));

        //then
        assertTrue(task1.isOverlapping(task2), "Задачи не пересекаются");

    }

    @Test
    @DisplayName("Задачи не пересекаются - следуют одна за другой")
    void test_Tasks_Is_Not_Overlapping_Follow() {

        //given
        Task task1 = new Task("First Task", "Interval is not overlapping with Second Task");
        Task task2 = new Task("Second Task", "Interval is not overlapping with First Task");
        DateTimeFormatter formater = Stub.formater;

        //when
        task1.setStartTime(LocalDateTime.parse("16.08.2025 08:25", formater));
        task1.setDuration(Duration.ofHours(1));

        task2.setStartTime(LocalDateTime.parse("16.08.2025 09:25", formater));
        task2.setDuration(Duration.ofHours(1));

        //then
        assertTrue(!task1.isOverlapping(task2), "Задачи пересекаются");

    }

    @Test
    @DisplayName("Задачи не пересекаются - следуют одна за другой")
    void test_Tasks_Is_Not_Overlapping() {

        //given
        Task task1 = new Task("First Task", "Interval is not overlapping with Second Task");
        Task task2 = new Task("Second Task", "Interval is not overlapping with First Task");
        DateTimeFormatter formater = Stub.formater;

        //when
        task1.setStartTime(LocalDateTime.parse("16.08.2025 08:25", formater));
        task1.setDuration(Duration.ofHours(1));

        task2.setStartTime(LocalDateTime.parse("16.08.2025 19:25", formater));
        task2.setDuration(Duration.ofHours(1));

        //then
        assertTrue(!task1.isOverlapping(task2), "Задачи пересекаются");

    }

    static class Stub {
        private static DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    }

}