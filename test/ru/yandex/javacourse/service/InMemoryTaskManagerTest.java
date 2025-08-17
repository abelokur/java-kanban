package ru.yandex.javacourse.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.model.Epic;
import ru.yandex.javacourse.model.Subtask;
import ru.yandex.javacourse.model.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    @Test
    @DisplayName("InMemoryTaskManager добавляет задачу и находит её по id")
    void test_AddTask_And_GetByID() {
        //given
        InMemoryTaskManager inMemoryTaskManager = Stub.getInMemoryTaskManager();

        Task task = Stub.getTask("Test add and get Task", "DESCRIPTION");
        inMemoryTaskManager.createTasks(task);

        int taskID = task.getId();

        //when
        Task TaskById = inMemoryTaskManager.getTask(taskID);

        //then
        assertEquals( taskID, TaskById.getId(), "Не получили Задание по id.");
    }

    @Test
    @DisplayName("InMemoryTaskManager добавляет Подзадачу и находит её по id")
    void test_AddSubtask_And_GetByID() {
        //given
        InMemoryTaskManager inMemoryTaskManager = Stub.getInMemoryTaskManager();

        Subtask subtask = Stub.getSubtask("Test add and get Subtask", "DESCRIPTION");
        inMemoryTaskManager.createSubtasks(subtask);

        int subTaskID = subtask.getId();

        //when
        Subtask subtaskById = inMemoryTaskManager.getSubtask(subTaskID);

        //then
        assertEquals(subTaskID , subtaskById.getId(), "Не получили Подзадачу по id.");
    }

    @Test
    @DisplayName("InMemoryTaskManager добавляет Эпик и находит его по id")
    void test_AddEpic_And_GetByIDd() {
        //given
        InMemoryTaskManager inMemoryTaskManager = Stub.getInMemoryTaskManager();

        Epic epic = new Epic("Test add and get Epic", "DESCRIPTION_EPIC");
        inMemoryTaskManager.createEpics(epic);

        int epicID = epic.getId();

        //when
        Epic epicById = inMemoryTaskManager.getEpic(epicID);

        //then
        assertEquals(epicID, epicById.getId(), "Не получили Эпик по id.");
    }

    @Test
    @DisplayName("Задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера")
    void test_GetID_And_SetId_DontConflict() {
        //given
        InMemoryTaskManager inMemoryTaskManager = Stub.getInMemoryTaskManager();
        Task taskGenId = Stub.getTask("Test generate ID", "DESCRIPTION");
        Task taskSetID = Stub.getTask("Test set ID", "DESCRIPTION");

        //when
        taskSetID.setId(taskGenId.getId() + 1);

        inMemoryTaskManager.createTasks(taskGenId);
        inMemoryTaskManager.createTasks(taskSetID);

        //then
        boolean diffId = (taskGenId.getId() == taskSetID.getId());

        assertFalse(diffId, "Заданный и сгенерированный id одинаковые.");
    }

    @Test
    @DisplayName("Проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер")
    void test_Task_DoesNotChange_After_AddToManager() {
        //given
        InMemoryTaskManager inMemoryTaskManager = Stub.getInMemoryTaskManager();

        Task task = Stub.getTask("Task does not change", "DESCRIPTION");

        inMemoryTaskManager.createTasks(task);

        //when
        Task taskDoesNotChange =  inMemoryTaskManager.getTask(task.getId());

        //then
        assertEquals(task.getId(), taskDoesNotChange.getId(), "При добавлении Задачи в менеджер поменялся ID.");

        assertEquals(task.getDescription(), taskDoesNotChange.getDescription(), "При добавлении Задачи в менеджер поменялось Описание (Description)");

        assertEquals(task.getName(), taskDoesNotChange.getName(), "При добавлении Задачи в менеджер поменялось Имя (Name)");

        assertEquals(task.getStatus(), taskDoesNotChange.getStatus(), "При добавлении Задачи в менеджер поменялся Статус (Status)");
    }

    @Test
    @DisplayName("InMemoryTaskManager Внутри эпиков не должно оставаться неактуальных id подзадач")
    void test_AddEpic_None_Actual_Subtasks() {
        //given
        InMemoryTaskManager inMemoryTaskManager = Stub.getInMemoryTaskManager();

        Epic epic = new Epic("Test add and get Epic", "DESCRIPTION_EPIC");
        inMemoryTaskManager.createEpics(epic);

        Subtask subtask = Stub.getSubtask("Test add and get Subtask", "DESCRIPTION");
        inMemoryTaskManager.createSubtasks(subtask);

        epic.addSubTask(subtask);

        //when
        epic.removeSubTask(subtask.getId());
        boolean subTaskListIsEmpty = epic.getSubTaskList().isEmpty();

        //then
        assertTrue(subTaskListIsEmpty, "Внутри эпика осталась неактуальная задача");

    }

    @Test
    @DisplayName("InMemoryTaskManager Внутри эпиков не должно оставаться неактуальных id подзадач")
    void test_Setters_Get_Subtask() {
        //given
        InMemoryTaskManager inMemoryTaskManager = Stub.getInMemoryTaskManager();

        Epic epic = new Epic("Test add and get Epic", "DESCRIPTION_EPIC");
        inMemoryTaskManager.createEpics(epic);

        Subtask subtask = Stub.getSubtask("Test add and get Subtask", "DESCRIPTION");
        inMemoryTaskManager.createSubtasks(subtask);

        epic.addSubTask(subtask);

        //when
        int subtaskId = subtask.getId();
        subtask.setId(subtaskId + 1001); // При вызове сеттеров необходимо проверять добавлена ли задача в Эпик

        epic.removeSubTask(subtask.getId());

        boolean isEmptySubTaskList = !epic.getSubTaskList().isEmpty(); // отрицание чтобы тест не "падал"

        //then
        assertTrue(isEmptySubTaskList, "Список подзадач должен быть пустой");

    }

    @Test
    @DisplayName("InMemoryTaskManager сортировка не пересчитывается каждый раз, а поддерживается при добавлении/удалении")
    void test_Get_Prioritized_Tasks() {
        //given
        final DateTimeFormatter startTimeFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        InMemoryTaskManager inMemoryTaskManager = Stub.getInMemoryTaskManager();

        Epic epic = new Epic("Test add and get Epic", "DESCRIPTION_EPIC");
        inMemoryTaskManager.createEpics(epic);

        Subtask subtask1 = Stub.getSubtask("Test add and get Subtask", "DESCRIPTION");
        subtask1.setStartTime(LocalDateTime.parse("17.08.2025 22:00", startTimeFormat));
        inMemoryTaskManager.createSubtasks(subtask1);

        epic.addSubTask(subtask1);

        Subtask subtask2 = Stub.getSubtask("Test add and get Subtask", "DESCRIPTION");
        subtask2.setStartTime(LocalDateTime.parse("17.08.2025 20:00", startTimeFormat));
        inMemoryTaskManager.createSubtasks(subtask2);

        epic.addSubTask(subtask2);

        //when
        List<Task> prioritizedTasks = inMemoryTaskManager.getPrioritizedTasks();
        prioritizedTasks = inMemoryTaskManager.getPrioritizedTasks(); // сортировка не пересчитывается каждый раз

        Subtask subtask3 = Stub.getSubtask("Test add and get Subtask", "DESCRIPTION");
        subtask3.setStartTime(LocalDateTime.parse("17.08.2025 21:00", startTimeFormat));
        inMemoryTaskManager.createSubtasks(subtask3);

        List<Task> prioritizedTasksAfterAddSubTask = inMemoryTaskManager.getPrioritizedTasks();

        inMemoryTaskManager.removeSubtask(subtask3.getId());
        List<Task> prioritizedTasksAfterRemoveSubTask = inMemoryTaskManager.getPrioritizedTasks();

        //then
        assertEquals(Stub.getPrioritizedTasksNoSorted(), prioritizedTasks.toString(),  "Есть сортировка");
            assertEquals(Stub.getPrioritizedTasksSorted(), prioritizedTasksAfterAddSubTask.toString(),  "Нет сортировки");
        assertEquals(Stub.getPrioritizedTasksNoSorted(), prioritizedTasksAfterRemoveSubTask.toString(),  "Нет сортировки");


    }

    static class Stub {
        public static InMemoryTaskManager getInMemoryTaskManager() {
            return new InMemoryTaskManager();
        }
        public static Task getTask(String name, String description) {
            return new Task(name, description);
        }
        public static Subtask getSubtask(String name, String description) {
            return new Subtask(name, description);
        }
        public static String getPrioritizedTasksNoSorted() {return "[Subtask{name='Test add and get Subtask', description='DESCRIPTION', id=2, status=NEW, duration=PT0S, startTime=2025-08-17T20:00}, Subtask{name='Test add and get Subtask', description='DESCRIPTION', id=1, status=NEW, duration=PT0S, startTime=2025-08-17T22:00}]";}
        public static String getPrioritizedTasksSorted() {return "[Subtask{name='Test add and get Subtask', description='DESCRIPTION', id=2, status=NEW, duration=PT0S, startTime=2025-08-17T20:00}, Subtask{name='Test add and get Subtask', description='DESCRIPTION', id=3, status=NEW, duration=PT0S, startTime=2025-08-17T21:00}, Subtask{name='Test add and get Subtask', description='DESCRIPTION', id=1, status=NEW, duration=PT0S, startTime=2025-08-17T22:00}]";}
    }
}