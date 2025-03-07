package edu.ucsd.cse110.habitizer.lib.domain;

/**
 * This class represents an individual task
 * <p>
 * Responsibilities:
 * - Storing its own name and state of completion
 * - Tracking its own timer (Start time plus time spent)
 */
public class Task {
    private String name;

    private int order;
    private boolean completed;
    private int timeSpent;
    private final int id;
    private static int idCounter = 0;
    
    /**
     * Task instantiates name as unique identifier for task.
     *
     * @param name Name for task.
     */
    public Task(String name, int order) {
        this.id = ++idCounter;
        this.name = name;
        this.completed = false;
        this.timeSpent = 0;
        this.order = order;
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
     * Returns time spent on task in seconds
     */
    public int getTimeSpent() {
        return timeSpent;
    }
    
    
    public int getId() {
        return id;
    }
    
    /**
     * Returns time spent on task in minutes
     */
    public int getTimeSpentMinutes() {
        return (int) Math.ceil(timeSpent / 60.0);
    }
    
    /**
     * Sets timeSpent
     *
     * @param timeSpent the number of seconds to set timeSpent to
     */
    public void setTime(int timeSpent) {
        this.timeSpent = timeSpent;
    }
    
    /**
     * Gets name of task
     *
     * @return name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets name of task
     *
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {return order;}
    
}
