package ru.yandex.javacourse.model;

import java.util.List;

public interface HistoryManager {
    void add(Task task);
    List<Task> getDefaultHistory();
}
