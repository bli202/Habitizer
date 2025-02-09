package edu.ucsd.cse110.habitizer.lib.domain;

import java.time.Duration;

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

    private int timeSpent;
    public Task(String name) {
        this.name = name;
        this.completed = false;
    }
    public void completeTask() {
        completed = true;
    }

    public boolean isCompleted() {
        return completed;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public void setTime(int timeSpent) {
        this.timeSpent = timeSpent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
