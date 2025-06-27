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
}