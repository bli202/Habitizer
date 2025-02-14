package edu.ucsd.cse110.habitizer.lib.domain;

/**
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

    /**
     * Task instantiates name as unique identifier for task.
     *
     * @param name Name for task.
     */
    public Task(String name) {
        this.name = name;
        this.completed = false;
        this.timeSpent = 0;
    }

    /**
     * Complete this task.
     */
    public void completeTask() {
        completed = true;
    }

    public void toggleCompletion() {
        this.completed = !this.completed;
    }

    /**
     * Returns if the task is true.
     * 
     * @return true if task is done, 
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Returns if time spent on task.
     * 
     * @return timeSpent. 
     */
    public int getTimeSpent() {
        return timeSpent;
    }

    /**
     * Sets timeSpent.
     * 
     * @param timeSpent to set timeSpent too. 
     */
    public void setTime(int timeSpent) {
        this.timeSpent = timeSpent;
    }

    /**
     * Gets name.
     * 
     * @return name. 
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     * 
     * @param name New name to set too. 
     */
    public void setName(String name) {
        this.name = name;
    }

}
