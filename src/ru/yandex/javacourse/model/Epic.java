package ru.yandex.javacourse.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

public class Epic extends Task {
    private HashMap<Integer, Subtask> subTaskList  = new HashMap<>();
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
    }

    public void setStartTime() {
        LocalDateTime minStartTime = null;
        for (Subtask subtask : subTaskList.values()) {
            LocalDateTime subStartTime = subtask.getStartTime();
            if (subStartTime != null) {
                if (minStartTime == null || subStartTime.isBefore(minStartTime)) {
                    minStartTime = subStartTime;
                }
            }
        }
        super.setStartTime(minStartTime);
    }

    public LocalDateTime getStartTime() {
        return super.getStartTime();
    }

    public Duration getDuration() {
        Duration total = Duration.ofMinutes(0);
        for (Subtask subtask : subTaskList.values()) {
            if (subtask.getDuration() != null) {
                total = total.plus(subtask.getDuration());
            }
        }
        return total;
    }

    public void setDuration(Duration duration) {
        super.setDuration(duration);
    }

    public void setEndTime() {
        LocalDateTime temp = null;
        for (Integer s : subTaskList.keySet()) {
            if (temp.isBefore(subTaskList.get(s).getEndTime())) {
                temp = subTaskList.get(s).getEndTime();
            }
        }
        this.endTime = temp;
    }

    @Override
    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    public void addSubTask(Subtask newSubtask) {
        subTaskList.put(newSubtask.getId(), newSubtask);
        newSubtask.setEpic(this);
        this.setStatus();
        this.setStartTime();
        this.setDuration(this.getDuration());
    }

    public void removeSubTask(int id) {
        subTaskList.remove(id);
        this.setStatus();
        this.setStartTime();
        this.setDuration(this.getDuration());
    }

    public HashMap<Integer, Subtask> getSubTaskList() {
        return new HashMap<Integer, Subtask>(subTaskList);
    }

    public void setSubTaskList(HashMap<Integer, Subtask> subTaskList) {
        Subtask subTask;
        this.subTaskList = subTaskList;

        for (Integer subTaskId : subTaskList.keySet()) {
            subTask = subTaskList.get(subTaskId);
            subTask.setEpic(this);
        }
    }

    public void setSubTaskList() {
        subTaskList.clear();
    }

    public void updateSubTask(Subtask updateSubtask) {
        addSubTask(updateSubtask);
    }

    public void setStatus() {
        this.setStatus(getStatus());
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

            switch (objSubTask.getStatus()) {
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
                "name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", id=" + this.getId() +
                ", status=" + this.getStatus() +
                ", duration=" + this.getDuration() +
                ", startTime=" + this.getStartTime() +
                ", subTasks=" + subTaskList +
                '}';
    }
}
