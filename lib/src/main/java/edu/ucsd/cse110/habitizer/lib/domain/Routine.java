package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import java.time.Instant;

public class Routine {
    private List<Task> taskList;
    private Instant startTime;
    private Duration elapsedTime;
    private final int estimatedTime;
    private Instant cumTaskTime;
    private boolean ongoing;
    private int tasksDone;
    private final String name;

    /**
     * Routine Constructor
     *
     * @param name  The name of the routine (Hardcoded to morning and night)
     * @param estimatedTime the total estimated time user picks to display
     */
    public Routine(int estimatedTime, String name) {
        this.name = name;
        this.taskList = new ArrayList<>();
        this.estimatedTime = estimatedTime;
        this.elapsedTime = Duration.ZERO;
        this.cumTaskTime = Instant.now();
        this.ongoing = false;
        this.tasksDone = 0;
    }

    /**
     * Adds a task to the routine
     *
     * @param task task obj we want to add
     * @return true if the task was added, false if the
     * routine is already started
     */
    public boolean addTask(Task task) {

        if (ongoing) return false;

        for (Task t : taskList) {
            if (t.getName().equals(task.getName())) {
                throw new IllegalArgumentException("Cannot have two tasks with the same name in Routine");
            }
        }

        return taskList.add(task);
    }

    public boolean removeTask(Task task) {
        if (ongoing || taskList.isEmpty()) return false;

        // remove task from List
        for (Task t : taskList) {
            if (t.getName().equals(task.getName())) {
                return taskList.remove(t);
            }
        }
        return false;
    }

    /**
     * Starts routine that is timers
     *
     * @throws IllegalArgumentException if routine has no tasks
     */
    public void startRoutine() {
        if (ongoing) return;

        if (taskList.isEmpty()) {
            throw new IllegalArgumentException("Cannot start a routine with no tasks");
        }

        this.startTime = Instant.now();
        this.elapsedTime = Duration.ZERO;
        this.cumTaskTime = Instant.now();
        this.tasksDone = 0;
        this.ongoing = true;
    }

    public void endRoutine() {
        if (ongoing) {
            elapsedTime = Duration.between(startTime, Instant.now());
            ongoing = false;
        }
    }

    /**
     * Marks a task done and checks it off by its name
     * This method's function:
     * - Finds task in list
     * - Finds the diff between cumulative task time and current time
     * - Update the cumulative task time and progress
     * - Auto end routine when all tasks are done
     *
     * @param task Name of the task to check off
     * @return true if task is checked off, false otherwise
     */
    public boolean checkOffTask(Task task) {
        if (!ongoing) return false;

        // Finding task to check off
        for (Task t : taskList) {
            if (t.getName().equals(task.getName()) && !t.isCompleted()) {
                t.completeTask();
                tasksDone++;
                int taskTime = (int) Duration.between(cumTaskTime, Instant.now()).getSeconds() * 1000;
                cumTaskTime = Instant.now();
                t.setTime(taskTime);
                if (tasksDone == taskList.size()) {
                    endRoutine();
                }
                return true;
            }
        }
        return false;
    }


    //Getters for Routine

    /**
     * Returns list of tasks in the routine
     */
    public List<Task> getTaskList() {
        return taskList;
    }

    /**
     * Returns overall elapsed time of the routine
     * If the routine is ongoing, calculates the duration from start to now
     */
    public Duration getElapsedTime() {
        if (ongoing && startTime != null) {
            return Duration.between(startTime, Instant.now());
        }
        return elapsedTime;
    }

    /**
     * Returns the estimated time on the duration (set by user)
     */
    public int getEstimatedTime() {
        return estimatedTime;
    }

    /**
     * Returns the cumulative time spent on all tasks
     */
    public Instant getCumTaskTime() {
        return cumTaskTime;
    }

    /**
     * Indicates if the routine is started or not
     */
    public boolean isOnGoing() {
        return ongoing;
    }

    /**
     * Return the num of tasks done
     */
    public int getTasksDone() {
        return tasksDone;
    }

    /**
     * Returns name of task (In this case either morning or night)
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the start time
     */
    public Instant getStartTime() {
        return startTime;
    }
}