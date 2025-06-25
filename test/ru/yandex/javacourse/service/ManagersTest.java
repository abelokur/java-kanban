package ru.yandex.javacourse.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ManagersTest {

    @Test
    void getDefault() {
        Managers manager = new Managers();
        TaskManager taskManager = manager.getDefault();
        System.out.println(taskManager);

        assertEquals("TaskManager{taskList={}, subtaskList={}, epicList={}}", taskManager.toString(), "TaskManager не проинициализирован");
    }
}