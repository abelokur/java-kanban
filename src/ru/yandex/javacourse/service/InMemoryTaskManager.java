package ru.yandex.javacourse.service;

import java.util.*;

import ru.yandex.javacourse.model.*;

public class InMemoryTaskManager implements TaskManager {
    private static int id;
    private HashMap<Integer, Task> taskList = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskList = new HashMap<>();
    private HashMap<Integer, Epic> epicList = new HashMap<>();
    private Set<Task> sortedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    InMemoryHistoryManager historyManager = Managers.getDefaultHistory();

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(sortedTasks);
    }

    public boolean isTaskOverlapping(Task taskChecking) {
        List<Task> listOfTasks = sortedTasks.stream().filter(taskChecking::isOverlapping).toList();
        if (listOfTasks.isEmpty()) {
            return false;
        }
        return true;
    }

    public List<Task> getDefaultHistory() {
        return historyManager.getDefaultHistory();
    }

    public static int getId() {
        return id++;
    }

    public int getHistoryManagerSize() {
        return historyManager.getSize();
    }

    public void setHistoryManagerSize(int size) {
        historyManager.setSize(size);
    }

    // 2.a.Получение списка всех задач.
    @Override
    public HashMap<Integer, Task> getAllTasks() {
        return new HashMap<Integer, Task>(taskList);
    }

    @Override
    public HashMap<Integer, Subtask> getAllSubTasks() {
        return new HashMap<Integer, Subtask>(subtaskList);
    }

    @Override
    public HashMap<Integer, Epic> getAllEpics() {
        return new HashMap<Integer, Epic>(epicList);
    }

    // 2.b. Удаление всех задач.
    @Override
    public void removeTasks() {
        for (Integer taskId : taskList.keySet()) {
            historyManager.remove(taskId);
        }
        taskList.clear();
    }

    @Override
    public void removeSubtasks() {
        for (Integer subTaskId : subtaskList.keySet()) {
            historyManager.remove(subTaskId);
        }
        subtaskList.clear();
        for (Integer epicKey : epicList.keySet()) {
            Epic epicObject = epicList.get(epicKey);
            epicObject.setSubTaskList();
            epicObject.setStatus();
        }
    }

    @Override
    public void removeEpics() {
        for (Integer epicId : epicList.keySet()) {
            historyManager.remove(epicId);
        }
        for (Integer subTaskId : subtaskList.keySet()) {
            historyManager.remove(subTaskId);
        }
        epicList.clear();
        subtaskList.clear();
    }

    @Override
    public void removeAll() {
        removeTasks();
        removeSubtasks();
        removeEpics();
    }

    //2.c. Получение по идентификатору
    @Override
    public Task getTask(int id) {
        historyManager.add(taskList.get(id));
        return taskList.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        historyManager.add(subtaskList.get(id));
        return subtaskList.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.add(epicList.get(id));
        return epicList.get(id);
    }

    // 2.d. Создание. Сам объект должен передаваться в качестве параметра.
    @Override
    public void createTasks(Task task) {
        taskList.put(task.getId(), task);
        if (!isTaskOverlapping(task) && task.isStartTime()) {
            sortedTasks.add(task);
        }
    }

    @Override
    public void createSubtasks(Subtask subtask) {
        subtaskList.put(subtask.getId(), subtask);
        if (!isTaskOverlapping(subtask) && subtask.isStartTime()) {
            sortedTasks.add(subtask);
        }
    }

    @Override
    public void createEpics(Epic epic) {
        epicList.put(epic.getId(), epic);
    }

    //2. e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    @Override
    public void updateTask(Task task) {
        taskList.put(task.getId(), task);
        if (!isTaskOverlapping(task) && task.isStartTime()) {
            sortedTasks.add(task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtaskList.put(subtask.getId(), subtask);
        if (!isTaskOverlapping(subtask) && subtask.isStartTime()) {
            sortedTasks.add(subtask);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        epicList.put(epic.getId(), epic);
    }

    // 2.f. Удаление по идентификатору.
    @Override
    public void removeTask(int id) {
        historyManager.remove(id);
        taskList.remove(id);
        sortedTasks.remove(taskList.get(id));
    }

    @Override
    public void removeSubtask(int id) {

        Subtask subtask = subtaskList.get(id);
        if (subtask != null) {
            Epic epic = subtask.getEpic();
            if (epic != null) {
                epic.removeSubTask(id);
                epic.setStatus();
            }
        }
        historyManager.remove(id);
        subtaskList.remove(id);
        sortedTasks.remove(subtask);
    }

    @Override
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
    @Override
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
