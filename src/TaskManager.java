import java.util.HashMap;

public class TaskManager {
    private static int id;
    private HashMap<Integer, Task> taskList = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskList = new HashMap<>();
    private HashMap<Integer, Epic> epicList = new HashMap<>();

    public static int getId() {
        return id++;
    }

    // 2.a.Получение списка всех задач.
    @Override
    public String toString() {
        return "TaskManager{" +
                "taskList=" + taskList +
                ", subtaskList=" + subtaskList +
                ", epicList=" + epicList +
                '}';
    }

    // 2.b. Удаление всех задач.
    public void remoteTasks() {
        taskList = new HashMap<>();
    }

    public void remoteSubtasks() {
        subtaskList = new HashMap<>();
    }

    public void remoteEpics() {
        epicList = new HashMap<>();
    }

    public void remoteAll() {
        remoteTasks();
        remoteSubtasks();
        remoteEpics();
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
        subtaskList.put(subtask.id, subtask);
    }

    public void updateEpic(Epic epic) {
        epicList.put(epic.getId(), epic);
    }

    // 2.f. Удаление по идентификатору.
    public void removeTask(int id) {
        taskList.remove(id);
    }

    public void removeSubtask(int id) {
        subtaskList.remove(id);
    }

    public void removeEpic(int id) {
        epicList.remove(id);
    }

    // 3.a. Получение списка всех подзадач определённого эпика
    public String getAllSubtasks(Epic epic) {
        return epic.toString();
    }
}
