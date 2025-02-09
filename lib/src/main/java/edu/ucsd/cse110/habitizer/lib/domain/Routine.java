package edu.ucsd.cse110.habitizer.lib.domain;

import java.lang.reflect.GenericDeclaration;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import java.time.Instant;

public class Routine {
    private List<Task> taskList;
    private Instant startTime;
    private Duration elapsedTime;
    private final Duration estimatedTime;
    private Duration cumTaskTime;
    private boolean ongoing;
    private int tasksDone;
    private final String name;

    /**
     * Routine Constructor
     *
     * @param name  The nname of the routine (Hardcoded to monring and night)
     * @param estimatedTime the total estimated time user picks to display
     */
    public Routine(Duration estimatedTime, String name) {
        this.name = name;
        this.taskList = new ArrayList<>();
        this.estimatedTime = estimatedTime;
        this.elapsedTime = Duration.ZERO;
        this.cumTaskTime = Duration.ZERO;
        this.ongoing = false;
        this.tasksDone = 0;
    }

    /**
     * Adds a task to the routine
     *
     * @param task
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
        this.cumTaskTime = Duration.ZERO;
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
     * - Start task timer if it hasn't started
     * - Complete the task and stops its timer
     * - Update the cumulative task time and progress
     * - Auto end routine when all tasks are done
     *
     * @param taskName Name of the task to check off
     * @return true if task is checked off, false otherwise
     */
    public boolean checkOffTask(String taskName) {
        if (!ongoing) return false;

        // Finding task to check off
        for (Task t : taskList) {
            if (t.getName().equals(taskName) && !t.isCompleted()) {
                t.completeTask();
                tasksDone++;
                var taskTime = elapsedTime.minus(cumTaskTime);
                cumTaskTime = elapsedTime;
                int minutes = (int) taskTime.toMinutes();
                t.setTime(minutes);
                if (tasksDone == taskList.size()) {
                    endRoutine();
                }
                return true;
            }
        }
        return false;
    }


    //Getters for Routine

    /*
     * Returns list of tasks in the routine
     */
    public List<Task> getTaskList() {
        return taskList;
    }

    /*
     * Returns overall elapsed time of the routine
     * If the routine is ongoing, calculates the duration from start to now
     */
    public Duration getElapsedTime() {
        if (ongoing && startTime != null) {
            return Duration.between(startTime, Instant.now());
        }
        return elapsedTime;
    }

    /*
     * Returns the estimated time on the duration (set by user)
     */
    public Duration getEstimatedTime() {
        return estimatedTime;
    }

    /*
     * Returns the cumulative time spent on all tasks
     */
    public Duration getCumTaskTime() {
        return cumTaskTime;
    }

    /*
     * Indicates if the routine is started or not
     */
    public boolean isOnGoing() {
        return ongoing;
    }

    /*
     * Return the num of tasks done
     */
    public int getTasksDone() {
        return tasksDone;
    }

    /*
     * Returns name of task (In this case either morning or night)
     */
    public String getName() {
        return name;
    }
}