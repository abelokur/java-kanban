package ru.yandex.javacourse.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    @Test
    @DisplayName("Эпики равны друг другу, если равен их id")
    void test_Epics_Equal_With_EqualID() {
        //given
        Epic epic = new Epic("Test EpicsEqualWithEqualID", "First Epic");
        Epic equalEpic = new Epic("Test EpicsEqualWithEqualID", "Second Epic");

        //when
        equalEpic.setId(epic.getId());

        //then
        assertEquals(epic, equalEpic, "Эпики не равны друг другу");
    }

    @Test
    @DisplayName("Расчёта статуса Epic - Все подзадачи со статусом NEW")
    void test_Epics_Statues_NEW() {
        //given
        Epic epic = new Epic("Test Epic", "EpicsWithAllStatusesNEW");

        Subtask subtaskFirst = new Subtask("First Subtask", "SubTasksWithStatusNEW");
        Subtask SubtaskSecond = new Subtask("Second Subtask", "SubTasksWithStatusNEW");

        epic.addSubTask(subtaskFirst);
        epic.addSubTask(SubtaskSecond);

        //when
        Status status = epic.getStatus();

        //then
        assertEquals(Status.NEW, status, "Статус Epic не NEW");

    }

    @Test
    @DisplayName("Расчёта статуса Epic - Все подзадачи со статусом DONE")
    void test_Epics_Statues_DONE() {
        //given
        Epic epic = new Epic("Test Epic", "EpicsWithAllStatusesDONE");

        Subtask subtaskFirst = new Subtask("First Subtask", "SubTasksWithStatusDONE");
        Subtask SubtaskSecond = new Subtask("Second Subtask", "SubTasksWithStatusDONE");

        subtaskFirst.setStatus(Status.DONE);
        SubtaskSecond.setStatus(Status.DONE);

        epic.addSubTask(subtaskFirst);
        epic.addSubTask(SubtaskSecond);

        //when
        Status status = epic.getStatus();

        //then
        assertEquals(Status.DONE, status, "Статус Epic не DONE");

    }

    @Test
    @DisplayName("Расчёта статуса Epic - Подзадачи со статусом NEW и DONE")
    void test_Epics_Statues_NEW_DONE() {
        //given
        Epic epic = new Epic("Test Epic", "EpicsWithStatusesNEW_DONE");

        Subtask subtaskFirst = new Subtask("First Subtask", "SubTasksWithStatusNEW");
        Subtask SubtaskSecond = new Subtask("Second Subtask", "SubTasksWithStatusDONE");

        subtaskFirst.setStatus(Status.NEW);
        SubtaskSecond.setStatus(Status.DONE);

        epic.addSubTask(subtaskFirst);
        epic.addSubTask(SubtaskSecond);

        //when
        Status status = epic.getStatus();

        //then
        assertEquals(Status.IN_PROGRESS, status, "Статус Epic не IN_PROGRESS");

    }

    @Test
    @DisplayName("Расчёта статуса Epic - Все подзадачи со статусом IN_PROGRESS")
    void test_Epics_Statues_IN_PROGRESS() {
        //given
        Epic epic = new Epic("Test Epic", "EpicsWithStatusesIN_PROGRESS");

        Subtask subtaskFirst = new Subtask("First Subtask", "SubTasksWithStatusIN_PROGRESS");
        Subtask SubtaskSecond = new Subtask("Second Subtask", "SubTasksWithStatusIN_PROGRESS");

        subtaskFirst.setStatus(Status.IN_PROGRESS);
        SubtaskSecond.setStatus(Status.IN_PROGRESS);

        epic.addSubTask(subtaskFirst);
        epic.addSubTask(SubtaskSecond);

        //when
        Status status = epic.getStatus();

        //then
        assertEquals(Status.IN_PROGRESS, status, "Статус Epic не IN_PROGRESS");

    }
}