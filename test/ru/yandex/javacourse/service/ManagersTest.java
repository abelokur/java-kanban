package ru.yandex.javacourse.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ManagersTest {

    @Test
    @DisplayName("Класс Managers всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров")
    void test_GetDefault() {
        //given
        Managers manager = Stub.getManagers();

        //when
        TaskManager taskManager = manager.getDefault();

        //then
        assertEquals(Stub.getTaskManagerString(), taskManager.toString(), "TaskManager не проинициализирован");
    }

    static class Stub {
        public static Managers getManagers() {
            return new Managers();
        }
        public static String getTaskManagerString() {
            return "TaskManager{taskList={}, subtaskList={}, epicList={}}";
        }
    }
}