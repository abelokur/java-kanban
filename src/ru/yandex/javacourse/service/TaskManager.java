package ru.yandex.javacourse.service;

import java.util.HashMap;
import ru.yandex.javacourse.model.*;

public class TaskManager {
    private static int id;
    private HashMap<Integer, Task> taskList = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskList = new HashMap<>();
    private HashMap<Integer, Epic> epicList = new HashMap<>();

    public static int getId() {
        return id++;
    }

    // 2.a.Получение списка всех задач.
    public HashMap<Integer, Task> getAllTasks() {
        return new HashMap<Integer, Task>(taskList);
    }

    public HashMap<Integer, Subtask> getAllSubTasks() {
        return new HashMap<Integer, Subtask>(subtaskList);
    }

    public HashMap<Integer, Epic> getAllEpics() {
        return new HashMap<Integer, Epic>(epicList);
    }

    // 2.b. Удаление всех задач.
    public void removeTasks() {
        taskList.clear();
    }

    public void removeSubtasks() {
        subtaskList.clear();
        for (Integer epicKey : epicList.keySet()) {
            Epic epicObject = epicList.get(epicKey);
            epicObject.setSubTaskList();
            epicObject.setStatus();
        }
    }

    public void removeEpics() {
        epicList.clear();
        subtaskList.clear();
    }

    public void removeAll() {
        removeTasks();
        removeSubtasks();
        removeEpics();
    }

    //2.c. Получение по идентификатору
    public Task getTask(int id) {
        return taskList.get(id);
    }

    public Subtask getSubtask(int id) {
        return subtaskList.get(id);
    }

    public Epic getEpic(int id) {
        return epicList.get(id);
    }

    // 2.d. Создание. Сам объект должен передаваться в качестве параметра.
    public void createTasks(Task task) {
        taskList.put(task.getId(), task);
    }

    public void createSubtasks(Subtask subtask) {
        subtaskList.put(subtask.getId(), subtask);
    }

    public void createEpics(Epic epic) {
        epicList.put(epic.getId(), epic);
    }

    //2. e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public void updateTask(Task task) {
        taskList.put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask) {
        subtaskList.put(subtask.getId(), subtask);
    }

    public void updateEpic(Epic epic) {
        epicList.put(epic.getId(), epic);
    }

    // 2.f. Удаление по идентификатору.
    public void removeTask(int id) {
        taskList.remove(id);
    }

    public void removeSubtask(int id) {

        Subtask subtask = subtaskList.get(id);
        Epic epic = subtask.getEpic();
        HashMap<Integer, Subtask> epicSubTaskList = epic.getSubTaskList();

        if (epicSubTaskList != null) {
            for (Integer subTaskId : epicSubTaskList.keySet()) {
                if (epicSubTaskList.get(subTaskId).getId() == id) {
                    epic.removeSubTask(subTaskId);
                }
            }
        }

        subtaskList.remove(id);
    }

    public void removeEpic(int id) {
        Epic epic = epicList.get(id);
        if (epic != null) {
            for (Integer subTaskId : epic.getSubTaskList().keySet()) {
                subtaskList.remove(subTaskId);
            }
        }
        epicList.remove(id);
    }

    // 3.a. Получение списка всех подзадач определённого эпика
    public String getAllSubtasks(Epic epic) {
        return epic.toString();
    }

    @Override
    public String toString() {
        return "TaskManager{" +
                "taskList=" + taskList +
                ", subtaskList=" + subtaskList +
                ", epicList=" + epicList +
                '}';
    }
}
