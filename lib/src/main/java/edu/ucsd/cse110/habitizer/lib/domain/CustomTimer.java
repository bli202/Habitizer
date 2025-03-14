package edu.ucsd.cse110.habitizer.lib.domain;

import java.time.Instant;
import java.time.Duration;

/**
 * Custom timer class with following methods:
 * - Start
 * - Pause
 * - Get Total Time
 * - Get Task Time (updates with every task time)
 * - Add some time
 */
public class CustomTimer {
    private long cumTime;
    private long taskTime;
    private boolean ongoing;
    private Instant startTime;
    private Instant taskStartTime;

    /**
     * CustomTimer Constructor to set total time to 0.
     * Must call start() to start actual timer.
     */
    public CustomTimer() {
        cumTime = 0;
        taskTime = 0;
        ongoing = false;
    }
    
    /**
     * Helper method to update the timer.
     * Method is called whenever a value needs to be accessed.
     */
    private void update() {
        if(!ongoing) return;
        cumTime += Duration.between(startTime, Instant.now()).toMillis();
        startTime = Instant.now();
        taskTime += Duration.between(taskStartTime, Instant.now()).toMillis();
        taskStartTime = Instant.now();
    }

    /**
     * Must be called to start timer or resume after pausing.
     */
    public void start() {
        if (ongoing) return;

        ongoing = true;
        startTime = Instant.now();
        taskStartTime = Instant.now();
    }

    /**
     * Pauses the timer.
     *
     * @return Total time so far in seconds.
     */
    public long pause() {
        if (!ongoing) return 0;
        update();
        ongoing = false;
        return cumTime / 1000;
    }

    /**
     * Get total time between now and when the timer was started,
     * ignoring paused time and adding manually added time.
     * @return Total time so far in seconds.
     */
    public long getTime() {
        update();
        return cumTime / 1000;
    }

    /**
     * Get task time. This method is called when a task is finished
     * and automatically (re)starts the timer for the next task.
     * If this method is called again, the returned value
     * will be the difference in seconds between this call and previous call.
     *
     * @return Difference in seconds between this call and previous call.
     * If this is the first call, then {@code getTaskTime()} = {@code getTime()}
     */
    public long getTaskTime() {
        update();
        taskStartTime = Instant.now();
        long returnVal = taskTime / 1000;
        taskTime = 0;
        return returnVal;
    }

    public long getTaskTimeNoReset() {
        update();
        taskStartTime = Instant.now();
        return taskTime / 1000;
    }

    /**
     * Adds specified time to timer.
     *
     * @param seconds to add to timer. Will add time even when paused.
     */
    public void addTime(long seconds) {
        update();
        cumTime += seconds * 1000;
        taskTime += seconds * 1000;
    }

    /**
     * Return whether or not the timer is ongoing.
     *
     * @return timer status
     */
    public boolean getOngoing() {
        return ongoing;
    }
    
    public long getCumTime() {
        return cumTime;
    }
    
    public boolean isOngoing() {
        return ongoing;
    }
    
    public Instant getStartTime() {
        return startTime;
    }
    
    public Instant getTaskStartTime() {
        return taskStartTime;
    }
    
    public void setCumTime(long cumTime) {
        this.cumTime = cumTime;
    }
    
    public void setTaskTime(long taskTime) {
        this.taskTime = taskTime;
    }
    
    public void setOngoing(boolean ongoing) {
        this.ongoing = ongoing;
    }
    
    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }
    
    public void setTaskStartTime(Instant taskStartTime) {
        this.taskStartTime = taskStartTime;
    }
}