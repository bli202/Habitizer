package edu.ucsd.cse110.habitizer.lib.domain;
import java.time.Duration;
import java.time.Instant;

/*
 * This class represents an individual task
 *
 * Responsibilities:
 * - Storing its own name and state of completion
 * - Tracking its own timer (Start time plus time spent)
 *
 */
public class Task {
    private String name;
    private boolean completed;

    // elapsed time in minutes
    private Instant startTime;
    private Duration timeSpent;

    public Task(String name) {
        this.name = name;
        this.completed = false;
        this.timeSpent = Duration.ZERO;
        this.startTime = null;
    }

    /*
     * Starts the task's own timer
     */
    public void startTask() {
        if (startTime == null) {
            startTime = Instant.now();
        }
    }

    public void completeTask() {
        if (!completed) {
            if (startTime == null) startTask();

            timeSpent = Duration.between(startTime, Instant.now());
            completed = true;
        }
    }

    public boolean isCompleted() {
        return completed;
    }

    public Duration getTimeSpent() {
        return timeSpent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
