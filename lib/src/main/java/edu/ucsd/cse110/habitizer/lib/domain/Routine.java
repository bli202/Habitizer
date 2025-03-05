package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.ArrayList;
import java.util.List;

public class Routine {
    private List<Task> taskList;
    private int estimatedTime;
    private boolean ongoing;
    private int tasksDone;
    private final String name;
    private CustomTimer timer;
    private final int id;
    private static int idCounter = 0;

    /**
     * Routine Constructor
     *
     * @param name the name of the routine
     * @param estimatedTime the total estimated time user picks to display
     */
    public Routine(int estimatedTime, String name) {
        this.id = idCounter++;
        this.name = name;
        this.taskList = new ArrayList<>();
        this.estimatedTime = estimatedTime;
        this.ongoing = false;
        this.timer = new CustomTimer();
        this.tasksDone = 0;
    }

    public Routine(int id, int estimatedTime, String name) {
        this.id = id;
        this.name = name;
        this.taskList = new ArrayList<>();
        this.estimatedTime = estimatedTime;
        this.ongoing = false;
        this.timer = new CustomTimer();
        this.tasksDone = 0;
    }

    public int getId() {
        return id;
    }

    public String toString() {
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
    
    public boolean removeTask(String name) {
        if (ongoing || taskList.isEmpty()) return false;
        
        // remove task from List
        for (Task t : taskList) {
            if (t.getName().equals(name)) {
                return taskList.remove(t);
            }
        }
        return false;
    }
    
    public boolean removeTask(int taskId) {
        if (ongoing || taskList.isEmpty()) return false;
        
        // remove task from List
        for (Task t : taskList) {
            if (t.getId() == (taskId)) {
                return taskList.remove(t);
            }
        }
        return false;
    }

    /**
     * Starts routine; starts timer and updates all tasks to be not completed
     *
     * @throws IllegalArgumentException if routine has no tasks
     */
    public void startRoutine() {
        if (ongoing) return;

        if (taskList.isEmpty()) {
            // This should print an error message on-screen
            throw new IllegalArgumentException("Cannot start a routine with no tasks");
        }

        for(Task t : taskList) {
            if(t.isCompleted()) t.toggleCompletion();
        }
        this.timer = new CustomTimer();
        this.timer.start();
        this.tasksDone = 0;
        this.ongoing = true;
    }


    /**
     * Ends routine; stops timer
     */
    public void endRoutine() {
        if (ongoing) {
            timer.pause();
            ongoing = false;
        }
    }

    /**
     * Pauses routine timer (switches to mock timer) for testing purposes
     */
    public void pauseRoutineTimer() {
        if (ongoing) {
            if (timer.getOngoing()) {
                timer.pause();
            }
            else {
                timer.start();
            }
        }
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
     * @param t Task to check off
     * @return the time taken for the current task, in minutes
     */
    public int checkOffTask(Task t) {
        t.completeTask();
        tasksDone++;
        long taskTime = timer.getTaskTime();
        int taskTimeMinutes = (int) Math.ceil(taskTime / 60.0);
        t.setTime((int) taskTime);
        if (tasksDone == taskList.size()) {
            endRoutine();
        }
        return taskTimeMinutes;
    }

    public int getTaskTime() {
        return (int) timer.getTaskTimeNoReset();
    }


    // Getters for Routine

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
        if(ongoing) {
            return ((int) timer.getTime()) / 60;
        } else {
            return (int) Math.ceil(timer.getTime() / 60.0);
        }
    }

    /**
     * Returns the estimated time on the duration (set by user)
     */
    public int getEstimatedTime() {
        return estimatedTime;
    }

    /**
     * Sets the estimated time on the duration
     */
    public void setEstimatedTime(int time) {
        estimatedTime = time;
    }

    /**
     * Returns if the routine is started or not
     */
    public boolean getOngoing() {
        return ongoing;
    }

    public void setOngoing(boolean ongoing) {
        this.ongoing = ongoing;
    }

    /**
     * Return the num of tasks done so far
     */
    public int getTasksDone() {
        return tasksDone;
    }

    /**
     * Returns name of routine
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the number of tasks in the routine
     */
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

    /**
     * Returns the timer object associated with this routine
     */
    public CustomTimer getTimer() {
        return timer;
    }

    public void setTimer(CustomTimer t) {
        this.timer = t;
    }

    public void setTasksDone(int i) {
        this.tasksDone = i;
    }
}
