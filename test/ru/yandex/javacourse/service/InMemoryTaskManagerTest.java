package ru.yandex.javacourse.service;

import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.model.Epic;
import ru.yandex.javacourse.model.Subtask;
import ru.yandex.javacourse.model.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    @Test
    void addTaskAndGetByID() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Task task = new Task("Test add and get Task", "DESCRIPTION");
        inMemoryTaskManager.createTasks(task);

        int taskID = task.getId();

        Task TaskById = inMemoryTaskManager.getTask(taskID);

        assertEquals( taskID, TaskById.getId(), "Не получили Задание по id.");
    }

    @Test
    void addSubtaskAndGetByID() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Subtask subtask = new Subtask("Test add and get Subtask", "DESCRIPTION");
        inMemoryTaskManager.createSubtasks(subtask);

        int subTaskID = subtask.getId();

        Subtask subtaskById = inMemoryTaskManager.getSubtask(subTaskID);

        assertEquals(subTaskID , subtaskById.getId(), "Не получили Подзадачу по id.");
    }

    @Test
    void addEpicAndGetByIDd() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Epic epic = new Epic("Test add and get Epic", "DESCRIPTION_EPIC");
        inMemoryTaskManager.createEpics(epic);

        int epicID = epic.getId();

        Epic epicById = inMemoryTaskManager.getEpic(epicID);

        assertEquals(epicID, epicById.getId(), "Не получили Эпик по id.");
    }

    @Test
    void getIdAnSetIdDontConflict() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task taskGenId = new Task("Test generate ID", "DESCRIPTION");
        Task taskSetID = new Task("Test set ID", "DESCRIPTION");

        taskSetID.setId(taskGenId.getId() + 1);

        inMemoryTaskManager.createTasks(taskGenId);
        inMemoryTaskManager.createTasks(taskSetID);

        boolean diffId = (taskGenId.getId() == taskSetID.getId());

        assertFalse(diffId, "Заданный и сгенерированный id одинаковые.");
    }

    @Test
    void taskDoesNotChangeAfterAddToManager() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Task task = new Task("Task does not change", "DESCRIPTION");

        inMemoryTaskManager.createTasks(task);

        Task taskDoesNotChange =  inMemoryTaskManager.getTask(task.getId());

        assertEquals(task.getId(), taskDoesNotChange.getId(), "При добавлении Задачи в менеджер поменялся ID.");

        assertEquals(task.getDescription(), taskDoesNotChange.getDescription(), "При добавлении Задачи в менеджер поменялось Описание (Description)");

        assertEquals(task.getName(), taskDoesNotChange.getName(), "При добавлении Задачи в менеджер поменялось Имя (Name)");

        assertEquals(task.getStatus(), taskDoesNotChange.getStatus(), "При добавлении Задачи в менеджер поменялся Статус (Status)");
    }


}