package ru.yandex.javacourse.service;

import ru.yandex.javacourse.model.Epic;
import ru.yandex.javacourse.model.Subtask;
import ru.yandex.javacourse.model.Task;

import java.util.HashMap;

public interface TaskManager {

    // 2.a.Получение списка всех задач.
    HashMap<Integer, Task> getAllTasks();

    HashMap<Integer, Subtask> getAllSubTasks();

    HashMap<Integer, Epic> getAllEpics();

    // 2.b. Удаление всех задач.
    void removeTasks();

    void removeSubtasks();

    void removeEpics();

    void removeAll();

    //2.c. Получение по идентификатору
    Task getTask(int id);

    Subtask getSubtask(int id);

    Epic getEpic(int id);

    // 2.d. Создание. Сам объект должен передаваться в качестве параметра.
    void createTasks(Task task);

    void createSubtasks(Subtask subtask);

    void createEpics(Epic epic);

    //2. e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    // 2.f. Удаление по идентификатору.
    void removeTask(int id);

    void removeSubtask(int id);

    void removeEpic(int id);

    // 3.a. Получение списка всех подзадач определённого эпика
    String getAllSubtasks(Epic epic);
}
