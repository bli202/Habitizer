package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import java.time.Instant;

public class Routine {
    private List<Task> taskList;
//    private Instant startTime;
//    private long elapsedTime;
    private final int estimatedTime;
//    private Instant cumTaskTime;
    private boolean ongoing;
    private int tasksDone;
    private final String name;
    private CustomTimer timer;

    /**
     * Routine Constructor
     *
     * @param name  The nname of the routine (Hardcoded to monring and night)
     * @param estimatedTime the total estimated time user picks to display
     */
    public Routine(int estimatedTime, String name) {
        this.name = name;
        this.taskList = new ArrayList<>();
        this.estimatedTime = estimatedTime;
//        this.elapsedTime = Duration.ZERO;
//        this.cumTaskTime = Instant.now();
        this.ongoing = false;
        this.timer = new CustomTimer();
        this.tasksDone = 0;
    }

    public String toString() {
        return name;
    }

    public String getTitle() {
        return name;
    }

    public int getDuration() {
        return estimatedTime;
    }

    /**
     * Adds a task to the routine
     *
     * @param task Task to add.
     * @return true if the task was added, false if the
     * routine is already started
     */
    public boolean addTask(Task task) {

        if (ongoing) return false;

        for (Task t : taskList) {
            if (t.getName().equals(task.getName())) {
                // This should display an error message on-screen
                throw new IllegalArgumentException("Cannot have two tasks with the same name in Routine");
            }
        }

        return taskList.add(task);
    }

    /**
     * Removes selected task
     *
     * @param task Task to remove.
     * @return true if task was removed, false otherwise.
     */
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
            // This should print an error message on-screen
            throw new IllegalArgumentException("Cannot start a routine with no tasks");
        }

//        this.startTime = Instant.now();
//        this.elapsedTime = Duration.ZERO;
//        this.cumTaskTime = Instant.now();
        for(Task t : taskList) {
            if(t.isCompleted()) t.toggleCompletion();
        }
        this.timer = new CustomTimer();
        this.timer.start();
        this.tasksDone = 0;
        this.ongoing = true;
    }

    public void endRoutine() {
        if (ongoing) {
//            elapsedTime = Duration.between(startTime, Instant.now());
            timer.pause();
            ongoing = false;
        }
    }

    /**
     * Pauses routine timer (switches to mock timer) for testing purposes
     */
    public void pauseRoutineTimer() {
        timer.pause();
    }

    /**
     * Manually adds time to timer for testing purposes - argument
     * should always be 30 as per Canvas assignment description
     *
     * @param sec Number of seconds to add to timer
     */
    public void manualAddTime(long sec) {
        timer.addTime(sec);
    }

    /**
     * Marks a task done and checks it off by its name
     * This method's function:
     * - Finds task in list
     * - Finds the diff between cumulative task time and current time
     * - Update the cumulative task time and progress
     * - Auto end routine when all tasks are done
     *
     * @param taskName Name of the task to check off
     * @return true if task is checked off, false otherwise
     */
    public int checkOffTask(Task t) {
//        if (!ongoing) return false;

        // Finding task to check off
//        for (Task t : taskList) {
//            if (t.getName().equals(task.getName()) && !t.isCompleted()) {
        t.completeTask();
                tasksDone++;
//                int taskTime = (int) Duration.between(cumTaskTime, Instant.now()).getSeconds() * 1000;
                long taskTime = timer.getTaskTime();
//                long cumTaskTime = timer.getTime();
                int taskTimeMinutes = (int) Math.ceil(taskTime / 60.0);
                t.setTime((int) taskTime);
                if (tasksDone == taskList.size()) {
                    endRoutine();
                }
                return (int) taskTime;
//            }
//        }
//        return false;
    }


    //Getters for Routine

    /**
     * Returns list of tasks in the routine
     */
    public List<Task> getTaskList() {
        return taskList;
    }

    /**
     * Returns overall elapsed time of the routine in minutes (rounded down)
     * If the routine is ongoing, calculates the duration from start to now
     */
    public int getElapsedTime() {
//        if (ongoing && startTime != null) {
//            return Duration.between(startTime, Instant.now());
//        }
//        return elapsedTime;
        return ((int) timer.getTime()) / 60;
    }

    /**
     * Returns the estimated time on the duration (set by user)
     */
    public int getEstimatedTime() {
        return estimatedTime;
    }

    /*
     * *** All Commented Methods Delegated to CustomTimer ***
     *
     * Returns the cumulative time spent on all tasks
     */
//    public int getCumTaskTime() {
//        return (int) timer.getTime();
//    }

    /**
     * Returns if the routine is started or not
     */
    public boolean getongoing() {
        return ongoing;
    }

//    public void setOngoing(boolean b) {
//        this.ongoing = b;
//    }

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
//    public Instant getStartTime() {
//        return startTime;
//    }

    public int getNumTasks() {
        return taskList.size();
    }

    /**
     * Edits a task name - duplicate names not allowed
     *
     * @param task The task to be renamed
     * @param name The new name
     * @return True if task was successfully renamed; false if routine is ongoing
     */
    public boolean editTask(Task task, String name) {
        if (ongoing) return false;

        for (Task t : taskList) {
            if (t != task && t.getName().equals(name)) {
                // This should display an error message on-screen
                throw new IllegalArgumentException("Cannot have two tasks with the same name in Routine");
            }
        }

        task.setName(name);
        return true;
    }

    /**
     * Get cumulative elapsed time in seconds - for testing purposes only;
     * frontend should only ever use minutes, not seconds
     *
     * @return Cumulative elapsed time in seconds
     */
    public int getElapsedTimeSecs() {
        return (int) timer.getTime();
    }

    public CustomTimer getTimer() {
        return timer;
    }
}
