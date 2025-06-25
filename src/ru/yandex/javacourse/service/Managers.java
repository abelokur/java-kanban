package ru.yandex.javacourse.service;

public class Managers {
    static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
    public TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
}
