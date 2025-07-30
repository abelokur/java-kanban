package ru.yandex.javacourse.service;

import ru.yandex.javacourse.model.*;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    File fileTaskManager;

    public FileBackedTaskManager(File fileTaskManager) {
        this.fileTaskManager = fileTaskManager;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        int maxId = 0; // получить максимальное id при загрузке файла

        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        try (FileReader fileReader = new FileReader(fileBackedTaskManager.fileTaskManager.getName());
             BufferedReader br = new BufferedReader(fileReader)) {

            br.readLine(); // получим первую строку файла, она нам не нужна

            while (br.ready()) {


                String line = br.readLine();
                String[] lineArray = line.split(",");

                System.out.println(lineArray[0]);
                System.out.println(lineArray[1]);
                System.out.println(lineArray[2]);
                System.out.println(lineArray[3]);
                System.out.println(lineArray[4]);
                try {
                    System.out.println(lineArray[5]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    ;
                }

                maxId = Math.max(maxId, Integer.parseInt(lineArray[0]));

                switch (TypeTask.valueOf(lineArray[1])) {
                    case TypeTask.TASK:

                        Task task = new Task(lineArray[2], lineArray[4]);

                        task.setId(Integer.parseInt(lineArray[0]));
                        task.setStatus(Status.valueOf(lineArray[3]));

                        fileBackedTaskManager.createTasks(task);

                        break;

                    case TypeTask.SUBTASK:


                        Subtask subtask = new Subtask(lineArray[2], lineArray[4]);

                        subtask.setId(Integer.parseInt(lineArray[0]));
                        subtask.setStatus(Status.valueOf(lineArray[3]));
                        subtask.setEpic(fileBackedTaskManager.getEpic(Integer.parseInt(lineArray[5])));

                        fileBackedTaskManager.createSubtasks(subtask);

                        break;

                    case TypeTask.EPIC:

                        Epic epic = new Epic(lineArray[2], lineArray[4]);

                        epic.setId(Integer.parseInt(lineArray[0]));
                        epic.setStatus(Status.valueOf(lineArray[3]));

                        fileBackedTaskManager.createEpics(epic);

                        break;
                }
/*
                for (String str : lineArray) {
                    System.out.println(str);
                    //if ()
                }*/
                /*System.out.println(lineArray[0]);
                System.out.println(lineArray[1]);
                System.out.println(lineArray[2]);
                System.out.println(lineArray[3]);
                System.out.println(lineArray[4]);
                System.out.println(lineArray[5]);*/

                //System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Установка id
        for (int i = 0; i <= maxId; i++) {
            InMemoryTaskManager.getId();
        }

        return fileBackedTaskManager;
    }

    private void save() {
        String head = "id,type,name,status,description,epic\n";
        HashMap<Integer, Task> allTasks = super.getAllTasks();
        HashMap<Integer, Subtask> allSubTasks = super.getAllSubTasks();
        HashMap<Integer, Epic> allEpics = super.getAllEpics();

        //StringBuilder fileContents = new StringBuilder();

        try (Writer fileWriter = new FileWriter(fileTaskManager.getName())) {

            fileWriter.write(head);

            for (Integer eachTask : allTasks.keySet()) {
                Task task = allTasks.get(eachTask);
                //fileContents.append(toString(task));
                fileWriter.write(toString(task));
            }

            for (Integer eachEpic : allEpics.keySet()) {
                Epic epic = allEpics.get(eachEpic);
                fileWriter.write(toString(epic));
            }

            for (Integer eachSubTask : allSubTasks.keySet()) {
                Subtask subTask = allSubTasks.get(eachSubTask);
                fileWriter.write(toString(subTask));
            }

    } catch (IOException e) {
            try {
                throw new ManagerSaveException("Ошибка записи файла: ", fileTaskManager);
            } catch (ManagerSaveException exception) {
                System.out.println(exception.getDetailMessage());
            }

        }
/*
        try (Writer fileWriter = new FileWriter(fileTaskManager.getName())) {
            fileWriter.write(fileContents.toString());
            //Writer fileWriter1 = fileTaskManager.;
        }
            catch (IOException e) {
                e.printStackTrace();
            }*/
    }

    String toString(Task task) { //TypeTask.valueOf(task.getClass().toString())
        return task.getId() + "," + TypeTask.TASK + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() + ",\n";
    }

    String toString(Subtask subtask) {
        return subtask.getId() + "," + TypeTask.SUBTASK + "," + subtask.getName() + "," + subtask.getStatus() + "," + subtask.getDescription() + "," + subtask.getEpic().getId() + "\n";
    }

    String toString(Epic epic) {
        return epic.getId() + "," + TypeTask.EPIC + "," + epic.getName() + "," + epic.getStatus() + "," + epic.getDescription() + ",\n";
    }



    @Override
    public void removeTasks() {
        super.removeTasks();
        save();
    }

    @Override
    public void removeSubtasks() {
        super.removeSubtasks();
        save();
    }

    @Override
    public void removeEpics() {
        super.removeEpics();
        save();
    }

    @Override
    public void removeAll() {
        super.removeAll();
        save();
    }

    @Override
    public void createTasks(Task task) {
        super.createTasks(task);
        save();
    }

    @Override
    public void createSubtasks(Subtask subtask) {
        super.createSubtasks(subtask);
        save();
    }

    @Override
    public void createEpics(Epic epic) {
        super.createEpics(epic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
        save();
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }


    }
