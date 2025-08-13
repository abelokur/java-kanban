package ru.yandex.javacourse.service;

import ru.yandex.javacourse.exception.ManagerSaveException;
import ru.yandex.javacourse.model.*;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.HashMap;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    File fileTaskManager;
    final static DateTimeFormatter startTimeFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public FileBackedTaskManager() {
    }

    public FileBackedTaskManager(File fileTaskManager) {
        this.fileTaskManager = fileTaskManager;
    }

    public void setFileName(File fileTaskManager) {
        this.fileTaskManager = fileTaskManager;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        int maxId = 0; // получить максимальное id при загрузке файла

        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();
        fileBackedTaskManager.setFileName(file);
        try (FileReader fileReader = new FileReader(fileBackedTaskManager.fileTaskManager.getName());
             BufferedReader br = new BufferedReader(fileReader)) {

            br.readLine(); // получим первую строку файла, она нам не нужна

            while (br.ready()) {

                String line = br.readLine();
                String[] lineArray = line.split(",");

                maxId = Math.max(maxId, Integer.parseInt(lineArray[0]));

                int idObject = Integer.parseInt(lineArray[0]);
                TypeTask typeObject = TypeTask.valueOf(lineArray[1]);
                String nameObject = lineArray[2];
                Status statusObject = Status.valueOf(lineArray[3]);
                String descriptionObject = lineArray[4];

                int epicObjectId = getEpicObjectId(lineArray);

                switch (typeObject) {
                    case TypeTask.TASK:
                        loadTask(fileBackedTaskManager, idObject, nameObject, statusObject, descriptionObject);
                        break;
                    case TypeTask.SUBTASK:
                        loadSubtask(fileBackedTaskManager, idObject, nameObject, statusObject, descriptionObject, epicObjectId);
                        break;
                    case TypeTask.EPIC:
                        loadEpic(fileBackedTaskManager, idObject, nameObject, statusObject, descriptionObject);
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        setMaxId(maxId);

        return fileBackedTaskManager;
    }

    private static void loadTask(FileBackedTaskManager fileBackedTaskManager, int idObject, String nameObject, Status statusObject, String descriptionObject) {
        Task task = new Task(nameObject, descriptionObject);

        task.setId(idObject);
        task.setStatus(statusObject);

        fileBackedTaskManager.createTasks(task);
    }

    private static void loadSubtask(FileBackedTaskManager fileBackedTaskManager, int idObject, String nameObject, Status statusObject, String descriptionObject, int epicObjectId) {
        Subtask subtask = new Subtask(nameObject, descriptionObject);

        subtask.setId(idObject);
        subtask.setStatus(statusObject);
        subtask.setEpic(fileBackedTaskManager.getEpic(epicObjectId));

        // в файл эпики всегда записываем до подзадач, соответственно эпики создаются раньше подзадач
        Epic addEpic = fileBackedTaskManager.getEpic(epicObjectId);
        addEpic.addSubTask(subtask);

        fileBackedTaskManager.createSubtasks(subtask);
    }

    private static void loadEpic(FileBackedTaskManager fileBackedTaskManager, int idObject, String nameObject, Status statusObject, String descriptionObject) {
        Epic epic = new Epic(nameObject, descriptionObject);

        epic.setId(idObject);
        epic.setStatus(statusObject);

        fileBackedTaskManager.createEpics(epic);
    }

    private static void setMaxId(int maxId) {
        for (int i = 0; i <= maxId; i++) {
            InMemoryTaskManager.getId();
        }
    }

    private static int getEpicObjectId(String[] lineArray) {
        int epicObjectId = -1;
        if (lineArray.length == 6) {
            epicObjectId = Integer.parseInt(lineArray[5]);
        }
        return epicObjectId;
    }

    private void save() {
        String head = "id,type,name,status,description,epic,duration,starttime\n";

        HashMap<Integer, Task> allTasks = super.getAllTasks();
        HashMap<Integer, Subtask> allSubTasks = super.getAllSubTasks();
        HashMap<Integer, Epic> allEpics = super.getAllEpics();

        try (Writer fileWriter = new FileWriter(fileTaskManager.getName())) {

            fileWriter.write(head);

            for (Integer eachTask : allTasks.keySet()) {
                Task task = allTasks.get(eachTask);
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
            final ManagerSaveException userIOException = new ManagerSaveException("Ошибка записи файла: ", fileTaskManager);
            System.out.println(userIOException.getDetailMessage());
        }
    }

    String toString(Task task) {
        String strDuration = (task.getDuration() == null) ? "" : "" + task.getDuration().toMinutes();
        String strStartTime = (task.getStartTime() ==  null) ? "" : "" + task.getStartTime().format(FileBackedTaskManager.startTimeFormat);
        return task.getId() + "," + TypeTask.TASK + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() + "," + strDuration/*task.getDuration().toMinutes()*/ + "," + strStartTime/*task.getStartTime()*/ +"\n";
    }

    String toString(Subtask subtask) {
        String strDuration = (subtask.getDuration() == null) ? "" : "" + subtask.getDuration().toMinutes();
        String strStartTime = (subtask.getStartTime() ==  null) ? "" : "" + subtask.getStartTime().format(FileBackedTaskManager.startTimeFormat);
        return subtask.getId() + "," + TypeTask.SUBTASK + "," + subtask.getName() + "," + subtask.getStatus() + "," + subtask.getDescription() + "," + subtask.getEpic().getId() + "," + strDuration + "," + strStartTime + "\n";
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

    public void saveEmptyFile() {
        save();
    }
}
