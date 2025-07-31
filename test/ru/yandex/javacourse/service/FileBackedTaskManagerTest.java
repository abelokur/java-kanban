package ru.yandex.javacourse.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.model.Epic;
import ru.yandex.javacourse.model.Subtask;
import ru.yandex.javacourse.model.Task;

import java.io.File;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {
    @Test
    @DisplayName("FileBackedTaskManager создаем две Task, два Epic, три Subtask - записываем в файл, затем загружаем, проверяем")
    void test_WriteRead_TasksEpicsSubtasks() {
        //given
        File file = new File("filename1.txt");

        FileBackedTaskManager taskManagerWrite = new FileBackedTaskManager(file); // для записи файла
        FileBackedTaskManager taskManagerRead  = new FileBackedTaskManager(file); // для чтения файла

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

        // с помощью метода toString() получили все объекты taskManagerWrite
        HashMap<Integer, Epic>    hashMapEpic    = taskManagerWrite.getAllEpics();
        HashMap<Integer, Subtask> hashMapSubTask = taskManagerWrite.getAllSubTasks();
        HashMap<Integer, Task>    hashMapTask    = taskManagerWrite.getAllTasks();

        StringBuilder allObjectsWrite = new StringBuilder();
        StringBuilder allObjectsRead = new StringBuilder();

        for (Integer taskF : hashMapTask.keySet()) {
            allObjectsWrite.append(hashMapTask.get(taskF).toString());
        }

        for (Integer subtaskF : hashMapSubTask.keySet()) {
            allObjectsWrite.append(hashMapSubTask.get(subtaskF).toString());
        }

        for (Integer epicF : hashMapEpic.keySet()) {
            allObjectsWrite.append(hashMapEpic.get(epicF));
        }

        //when
        taskManagerRead = FileBackedTaskManager.loadFromFile(file);

        // с помощью метода toString() получили все объекты taskManagerRead
        hashMapEpic    = taskManagerRead.getAllEpics();
        hashMapSubTask = taskManagerRead.getAllSubTasks();
        hashMapTask    = taskManagerRead.getAllTasks();

        for (Integer taskF : hashMapTask.keySet()) {
            allObjectsRead.append(hashMapTask.get(taskF).toString());
        }

        for (Integer subtaskF : hashMapSubTask.keySet()) {
            allObjectsRead.append(hashMapSubTask.get(subtaskF).toString());
        }

        for (Integer epicF : hashMapEpic.keySet()) {
            allObjectsRead.append(hashMapEpic.get(epicF));
        }

        //then
        assertEquals( allObjectsWrite.toString(), allObjectsRead.toString(), "Записанные в файл и вычитанные из него объекты (Task, Subtask, Epic) отличаются");
    }
}
