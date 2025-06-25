package ru.yandex.javacourse.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    @Test
    void EpicsEqualWithEqualID() {
        Epic epic = new Epic("Test EpicsEqualWithEqualID", "First Epic");
        Epic equalEpic = new Epic("Test EpicsEqualWithEqualID", "Second Epic");

        equalEpic.setId(epic.getId());

        assertEquals(epic, equalEpic, "Эпики не равны друг другу");
    }
}