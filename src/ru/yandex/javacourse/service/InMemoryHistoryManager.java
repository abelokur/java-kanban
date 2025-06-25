package ru.yandex.javacourse.service;

import ru.yandex.javacourse.model.HistoryManager;
import ru.yandex.javacourse.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> historyList = new ArrayList<>();
    static final int HISTORY_LIST_LENGTH = 10;

    @Override
    public List<Task> getDefaultHistory() {
        return historyList;
    }


    @Override
    public void add(Task object) {
        if (historyList.size() >= HISTORY_LIST_LENGTH ) {
            historyList.remove(0);
        }
        historyList.add(object);
    }
}
