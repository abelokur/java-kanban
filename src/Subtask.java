public class Subtask extends Task{
    Epic epic;
    public Subtask(String name, String description, Epic epic) {
        super(name, description);
        this.epic = epic;
    }

    public Subtask(String name, String description) {
        super(name, description);
    }

    public void updateSubtask(Subtask subtask) {
        this.name = subtask.name;
        this.description = subtask.description;
        this.status = subtask.status;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
