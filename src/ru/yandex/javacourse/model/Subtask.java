package ru.yandex.javacourse.model;

public class Subtask extends Task {
    private Epic epic;

    public Subtask(String name, String description, Epic epic) {
        super(name, description);
        this.epic = epic;
    }

    public Subtask(String name, String description) {
        super(name, description);
    }

    public void updateSubtask(Subtask subtask) {
        this.setName(subtask.getName());
        this.setDescription(subtask.getDescription());
        this.setStatus(subtask.getStatus());
        this.setEpic(subtask.getEpic());
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", id=" + this.getId() +
                ", status=" + this.getStatus() +
                ", duration=" + this.getDuration() +
                ", startTime=" + this.getStartTime() +
                '}';
    }
}
