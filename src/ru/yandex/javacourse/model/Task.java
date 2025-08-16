package ru.yandex.javacourse.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import ru.yandex.javacourse.service.InMemoryTaskManager;

import static java.time.temporal.ChronoUnit.SECONDS;

public class Task {
    private String name;
    private String description;
    private int id;
    private Status status;
    private Duration duration = Duration.ofMinutes(0);
    private LocalDateTime startTime;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.id = InMemoryTaskManager.getId();
        this.status = Status.NEW;
    }

    public boolean isStartTime() {
        return this.getStartTime() != null;
    }

    public boolean isOverlapping(Task task) {
        boolean isOverlapping = true;
        if (Duration.between(this.getEndTime(), task.getStartTime()).get(SECONDS) >= 0 ||
            Duration.between(task.getEndTime(), this.getStartTime()).get(SECONDS) >= 0 ) {
            isOverlapping = false;
        }
        return isOverlapping;
    }

    public LocalDateTime getEndTime() {
        LocalDateTime returnValue = null;
        try {
            returnValue = startTime.plus(duration);
        } catch (NullPointerException e) {
            returnValue = null;
        }
        return returnValue;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
