package edu.ucsd.cse110.habitizer.app.lib;

import java.util.List;
import java.time.Duration;
import java.time.Instant;

public class Routine {
    private List<Task> taskList;
    private Duration elapsedTime;
    private final Duration estimatedTime;
    private Duration cumTaskTime;
    private boolean ongoing;
    private int tasksDone;

    private String name;
    private Instant startTime;
    public Routine(List<Task> taskList, Duration estimatedTime) {
        this.taskList = taskList;
        this.estimatedTime = estimatedTime;
        this.elapsedTime = Duration.ZERO;
        this.cumTaskTime = Duration.ZERO;
        this.ongoing = false;
        this.tasksDone = 0;
    }

    /**
     * Starts the routine by initializing elapsed time tracking and resetting progress metrics
     * Additional clean up tasks
     */
    public void startRoutine() {
        if (!ongoing) {
            startTime = Instant.now();
            cumTaskTime = Duration.ZERO;
            tasksDone = 0;
            ongoing = true;
        }
    }

    /**
     * Ends the routine and finalizes elapsed time
     * End routine needs to exit task view, as well as other items
     */
    public void endRoutine() {
        if (ongoing) {
            elapsedTime = Duration.between(startTime, Instant.now());
            ongoing = false;
        }
    }

    /**
     * Marks a task as complete if it exists in the routine and updates progress
     * @param task The task to be checked off
     * @return boolean indicating if the task was successfully checked off
     */
    public boolean checkoffTask(Task task) {
        if (!ongoing) {
            return false;
        }

        // Find the task in the list and mark it as complete if it exists and isn't already completed
        for (Task t : taskList) {
            if (t.equals(task) && !t.isCompleted()) {
                t.setCompleted(true);
                tasksDone++;
                cumTaskTime = cumTaskTime.plus(t.getTimeSpent());

                // If all tasks are done, end the routine
                if (tasksDone == taskList.size()) {
                    endRoutine();
                }
                return true;
            }
        }
        return false;
    }

    // Getters
    public List<Task> getTaskList() {
        return taskList;
    }

    public Duration getElapsedTime() {
        if (ongoing) {
            return Duration.between(startTime, Instant.now());
        }
        return elapsedTime;
    }

    public Duration getEstimatedTime() {
        return estimatedTime;
    }

    public Duration getCumTaskTime() {
        return cumTaskTime;
    }

    public boolean isOngoing() {
        return ongoing;
    }

    public int getTasksDone() {
        return tasksDone;
    }
}