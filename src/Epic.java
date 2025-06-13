import java.util.HashMap;

public class Epic extends Task{

    HashMap<Integer, Subtask> subTaskList  = new HashMap<>();
    public Epic(String name, String description) {
        super(name, description);
    }

    public void addSubTask(Subtask newSubtask) {
        subTaskList.put(newSubtask.getId(), newSubtask);
        newSubtask.epic = this;
        this.setStatus();
    }

    public void updateSubTask(Subtask updateSubtask) {
        addSubTask(updateSubtask);
    }

    public void setStatus() {
        this.status = getStatus();
    }

    public Status getStatus() {

        int subTaskSize = subTaskList.size();

        if (subTaskSize == 0) {
            return Status.NEW;
        }

        int statusNew = 0;
        int statusDone = 0;

        for (int subTaskId: subTaskList.keySet()) {

            Subtask objSubTask = subTaskList.get(subTaskId);

            switch (objSubTask.status) {
                case NEW :
                    statusNew++;
                    break;
                case DONE :
                    statusDone++;
                    break;
            }
        }

        if (statusNew == subTaskSize) {
            return Status.NEW;
        }

        if (statusDone == subTaskSize) {
            return Status.DONE;
        }

        return Status.IN_PROGRESS;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", subTasks=" + subTaskList +
                '}';
    }
}
