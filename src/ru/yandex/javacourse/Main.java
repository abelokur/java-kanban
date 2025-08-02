package ru.yandex.javacourse;

import ru.yandex.javacourse.model.*;
import ru.yandex.javacourse.service.InMemoryTaskManager;
import ru.yandex.javacourse.service.InMemoryHistoryManager.LinkedListClass;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println("Поехали!");

            Task task = new Task("10", "10");
            System.out.println(task.getId());
            Task task1 = new Task("20", "20");
            System.out.println(task1.getId());
            InMemoryTaskManager taskManager = new InMemoryTaskManager();
            System.out.println("111: " + taskManager);


            Epic epic = new Epic("11", "22");
            System.out.println(epic);

            System.out.println("---------");
            epic.addSubTask(new Subtask("1", "1"));
            epic.addSubTask(new Subtask("2", "2"));
            epic.addSubTask(new Subtask("3", "3"));
            System.out.println(epic);

            System.out.println("/////////");
            taskManager.createEpics(epic);
            System.out.println("/////////");
            System.out.println(taskManager);

            System.out.println("====================");
            taskManager.createTasks(task);
            taskManager.createTasks(task1);
            System.out.println(taskManager);

            System.out.println("///////////////////");
            System.out.println(taskManager.getAllSubtasks(epic));

            System.out.println("3333333333333333333333333");
            Epic epic1 = new Epic("111111111", "2222222222");
            epic1.addSubTask(new Subtask("11111", "111111"));
            epic1.addSubTask(new Subtask("222222", "22222"));
            epic1.addSubTask(new Subtask("333333", "33333"));
            taskManager.createEpics(epic1);
            System.out.println(taskManager);
            taskManager.removeEpic(epic1.getId());
            System.out.println(taskManager);


        System.out.println("Задачи:");
        for (Integer taskObject : taskManager.getAllTasks().keySet()) {

            System.out.println(taskManager.getTask(taskObject));
        }
        System.out.println("Эпики:");
        for (Integer taskObject : taskManager.getAllEpics().keySet()) {

            Epic epicObject = taskManager.getEpic(taskObject);

            System.out.println(epicObject);

            for (Integer index : epicObject.getSubTaskList().keySet()) {
                System.out.println(epicObject.getSubTaskList().get(index));
            }

        }
        System.out.println("Подзадачи:");
        for (Integer subTaskId : taskManager.getAllSubTasks().keySet()) {
            System.out.println(taskManager.getAllSubTasks().get(subTaskId));
        }

        System.out.println("История:");
        for (Task taskHistory : taskManager.getDefaultHistory()) {
            System.out.println(taskHistory);
        }


        LinkedListClass<Integer> linkedListClass = new LinkedListClass<Integer>();

        linkedListClass.linkLast(new Task("task1", "task1"));
        linkedListClass.linkLast(new Task("task2", "task2"));
        linkedListClass.linkLast(new Task("task3", "task3"));

        ArrayList<Task> nodeList = linkedListClass.getTasks();
        System.out.println(nodeList);
    }
}
