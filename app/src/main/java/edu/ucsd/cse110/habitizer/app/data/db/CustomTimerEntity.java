package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.Instant;

import edu.ucsd.cse110.habitizer.lib.domain.CustomTimer;

@Entity(
        tableName = "timers"
)
public class CustomTimerEntity {
    @PrimaryKey()
    @ColumnInfo(name = "id")
    public Integer id = 0;
    
    @ColumnInfo(name = "ongoing")
    public boolean ongoing;

    @ColumnInfo(name = "cumulativeTime")
    public long cumulativeTime;

    @ColumnInfo(name = "taskTime")
    public long taskTime;

    @ColumnInfo(name = "startTime")
    public long startTime;

    @ColumnInfo(name = "taskStartTime")
    public long taskStartTime;

    public CustomTimerEntity(long cumulativeTime, long taskTime, boolean ongoing, long startTime, long taskStartTime) {
        this.cumulativeTime = cumulativeTime;
        this.taskTime = taskTime;
        this.ongoing = ongoing;
        this.startTime = startTime;
        this.taskStartTime = taskStartTime;
    }

    // Method to convert CustomTimerEntity to CustomTimer
    public CustomTimer toCustomTimer() {
        CustomTimer timer = new CustomTimer();
        timer.setCumTime(cumulativeTime);
        timer.setTaskTime(taskTime);
        timer.setOngoing(ongoing);
        timer.setStartTime(Instant.ofEpochMilli(startTime));
        timer.setTaskStartTime(Instant.ofEpochMilli(taskStartTime));
        return timer;
    }

    // Method to create CustomTimerEntity from CustomTimer
    public static CustomTimerEntity fromCustomTimer(CustomTimer timer) {
        return new CustomTimerEntity(
                timer.getCumTime(),
                timer.getTaskTimeNoReset(),
                timer.getOngoing(),
                timer.getStartTime().toEpochMilli(),
                timer.getTaskStartTime().toEpochMilli()
        );
    }
}
