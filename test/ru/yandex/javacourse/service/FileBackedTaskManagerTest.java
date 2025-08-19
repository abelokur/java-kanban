package ru.yandex.javacourse.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.model.Epic;
import ru.yandex.javacourse.model.Subtask;
import ru.yandex.javacourse.model.Task;

import java.io.File;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    @Test
    @DisplayName("FileBackedTaskManager создаем две Task, два Epic, три Subtask - записываем в файл, затем загружаем, проверяем")
    void test_WriteRead_TasksEpicsSubtasks() {
        //given
        File file = new File("filename1.txt");

        FileBackedTaskManager taskManagerWrite = Stub.getFileBackedTaskManager(); // для записи файла
        FileBackedTaskManager taskManagerRead  = Stub.getFileBackedTaskManager(); // для чтения файла

        Task task  = new Task("10", "10");
        Task task1 = new Task("20", "20");

        // Записали таски
        taskManagerWrite.createTasks(task);
        taskManagerWrite.createTasks(task1);

        Epic epic  = new Epic("11", "22");

        Subtask subtask1 = new Subtask("11111", "111111");
        Subtask subtask2 = new Subtask("222222", "22222");
        Subtask subtask3 = new Subtask("333333", "33333");

        epic.addSubTask(subtask1);
        epic.addSubTask(subtask2);
        epic.addSubTask(subtask3);

        // Записали сабтаски
        taskManagerWrite.createSubtasks(subtask1);
        taskManagerWrite.createSubtasks(subtask2);
        taskManagerWrite.createSubtasks(subtask3);
        // записали эпик
        taskManagerWrite.createEpics(epic);

        // получим все объекты taskManagerWrite и с помощью переопределенного метода toString()
        HashMap<Integer, Epic>    hashMapEpic    = taskManagerWrite.getAllEpics();
        HashMap<Integer, Subtask> hashMapSubTask = taskManagerWrite.getAllSubTasks();
        HashMap<Integer, Task>    hashMapTask    = taskManagerWrite.getAllTasks();
        String allObjectsWrite = Stub.getAllObjects(hashMapEpic, hashMapSubTask, hashMapTask);

        //when
        taskManagerRead = FileBackedTaskManager.loadFromFile(file);

        // с помощью метода toString() получили все объекты taskManagerRead
        hashMapEpic    = taskManagerRead.getAllEpics();
        hashMapSubTask = taskManagerRead.getAllSubTasks();
        hashMapTask    = taskManagerRead.getAllTasks();

        String allObjectsRead = Stub.getAllObjects(hashMapEpic, hashMapSubTask, hashMapTask);

        //then
        assertEquals( allObjectsWrite, allObjectsRead, "Записанные в файл и вычитанные из него объекты (Task, Subtask, Epic) отличаются");
    }

    @Test
    @DisplayName("Запись и загрузка пустого файла")
    void test_WriteRead_Empty_File() {
        //given
        String fileName = "filenameEmpty.txt";
        Stub.setFileName(fileName);

        FileBackedTaskManager taskManagerWrite = Stub.getFileBackedTaskManager();
        taskManagerWrite.saveEmptyFile();

        // с помощью метода toString() получили все объекты taskManagerWrite
        HashMap<Integer, Epic>    hashMapEpic    = taskManagerWrite.getAllEpics();
        HashMap<Integer, Subtask> hashMapSubTask = taskManagerWrite.getAllSubTasks();
        HashMap<Integer, Task>    hashMapTask    = taskManagerWrite.getAllTasks();
        String allObjectsWrite = Stub.getAllObjects(hashMapEpic, hashMapSubTask, hashMapTask);

        FileBackedTaskManager taskManagerRead  = Stub.getFileBackedTaskManager(); // для чтения файла

        //when
        taskManagerRead = FileBackedTaskManager.loadFromFile(Stub.getFileName());

        // с помощью метода toString() получили все объекты taskManagerRead
        hashMapEpic    = taskManagerRead.getAllEpics();
        hashMapSubTask = taskManagerRead.getAllSubTasks();
        hashMapTask    = taskManagerRead.getAllTasks();
        String allObjectsRead = Stub.getAllObjects(hashMapEpic, hashMapSubTask, hashMapTask);

        //then
        assertEquals( allObjectsWrite, allObjectsRead, "Ошибка записи и загрузки пустого файла");

    }

    @Test
    @DisplayName("InMemoryTaskManager добавляет задачу и находит её по id")
    void test_AddTask_And_GetByID() {
        //given
        FileBackedTaskManager inMemoryTaskManager = Stub.getFileBackedTaskManager();

        Task task = InMemoryTaskManagerTest.Stub.getTask("Test add and get Task", "DESCRIPTION");
        inMemoryTaskManager.createTasks(task);

        int taskID = task.getId();

        //when
        Task TaskById = inMemoryTaskManager.getTask(taskID);

        //then
        assertEquals( taskID, TaskById.getId(), "Не получили Задание по id.");
    }

    @Test
    @DisplayName("FileBackedTaskManager добавляет Подзадачу и находит её по id")
    void test_AddSubtask_And_GetByID() {
        //given
        FileBackedTaskManager inMemoryTaskManager = Stub.getFileBackedTaskManager();

        Subtask subtask = InMemoryTaskManagerTest.Stub.getSubtask("Test add and get Subtask", "DESCRIPTION");

        Epic epic = new Epic("Epic", "Epic для subtask");
        epic.addSubTask(subtask);

        inMemoryTaskManager.createSubtasks(subtask);

        int subTaskID = subtask.getId();

        //when
        Subtask subtaskById = inMemoryTaskManager.getSubtask(subTaskID);

        //then
        assertEquals(subTaskID , subtaskById.getId(), "Не получили Подзадачу по id.");
    }

    @Test
    @DisplayName("FileBackedTaskManager добавляет Эпик и находит его по id")
    void test_AddEpic_And_GetByIDd() {
        //given
        FileBackedTaskManager inMemoryTaskManager = Stub.getFileBackedTaskManager();

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
        FileBackedTaskManager inMemoryTaskManager = Stub.getFileBackedTaskManager();
        Task taskGenId = InMemoryTaskManagerTest.Stub.getTask("Test generate ID", "DESCRIPTION");
        Task taskSetID = InMemoryTaskManagerTest.Stub.getTask("Test set ID", "DESCRIPTION");

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
        FileBackedTaskManager inMemoryTaskManager = Stub.getFileBackedTaskManager();

        Task task = InMemoryTaskManagerTest.Stub.getTask("Task does not change", "DESCRIPTION");

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
    @DisplayName("FileBackedTaskManager Внутри эпиков не должно оставаться неактуальных id подзадач")
    void test_AddEpic_None_Actual_Subtasks() {
        //given
        FileBackedTaskManager inMemoryTaskManager = Stub.getFileBackedTaskManager();

        Epic epic = new Epic("Test add and get Epic", "DESCRIPTION_EPIC");
        inMemoryTaskManager.createEpics(epic);

        Subtask subtask = InMemoryTaskManagerTest.Stub.getSubtask("Test add and get Subtask", "DESCRIPTION");

        epic.addSubTask(subtask);

        inMemoryTaskManager.createSubtasks(subtask);

        //when
        epic.removeSubTask(subtask.getId());
        boolean subTaskListIsEmpty = epic.getSubTaskList().isEmpty();

        //then
        assertTrue(subTaskListIsEmpty, "Внутри эпика осталась неактуальная задача");

    }

    @Test
    @DisplayName("FileBackedTaskManager Внутри эпиков не должно оставаться неактуальных id подзадач")
    void test_Setters_Get_Subtask() {
        //given
        FileBackedTaskManager inMemoryTaskManager = Stub.getFileBackedTaskManager();

        Epic epic = new Epic("Test add and get Epic", "DESCRIPTION_EPIC");
        inMemoryTaskManager.createEpics(epic);

        Subtask subtask = InMemoryTaskManagerTest.Stub.getSubtask("Test add and get Subtask", "DESCRIPTION");

        epic.addSubTask(subtask);

        inMemoryTaskManager.createSubtasks(subtask);

        //when
        int subtaskId = subtask.getId();
        subtask.setId(subtaskId + 1001); // При вызове сеттеров необходимо проверять добавлена ли задача в Эпик

        epic.removeSubTask(subtask.getId());

        boolean isEmptySubTaskList = !epic.getSubTaskList().isEmpty(); // отрицание чтобы тест не "падал"

        //then
        assertTrue(isEmptySubTaskList, "Список подзадач должен быть пустой");

    }

    static class Stub {
        private static File fileName = new File("filename1.txt");

        public static void setFileName(String file) {
            fileName = new File(file);
        }

        public static FileBackedTaskManager getFileBackedTaskManager() {
            return new FileBackedTaskManager(fileName);
        }

        public static File getFileName() {
            return fileName;
        }

        public static String getAllObjects(HashMap<Integer, Epic> hashMapEpic, HashMap<Integer, Subtask> hashMapSubTask, HashMap<Integer, Task>    hashMapTask) {

            StringBuilder allObjects = new StringBuilder();

            for (Integer taskF : hashMapTask.keySet()) {
                allObjects.append(hashMapTask.get(taskF));
            }

            for (Integer subtaskF : hashMapSubTask.keySet()) {
                allObjects.append(hashMapSubTask.get(subtaskF));
            }

            for (Integer epicF : hashMapEpic.keySet()) {
                allObjects.append(hashMapEpic.get(epicF));
            }
            return allObjects.toString();
        }

    }
}
