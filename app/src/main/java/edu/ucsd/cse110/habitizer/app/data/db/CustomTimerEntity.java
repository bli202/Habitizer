package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.Instant;

import edu.ucsd.cse110.habitizer.lib.domain.CustomTimer;

@Entity(
        tableName = "timers",
        indices = {@Index(value = "routineId", unique = true)}
)
public class CustomTimerEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id = null;

    @ColumnInfo(name = "routineId")
    public Integer routineId;

    @ColumnInfo(name = "cumTime")
    public long cumTime;

    @ColumnInfo(name = "taskTime")
    public long taskTime;

    @ColumnInfo(name = "ongoing")
    public boolean ongoing;

    @ColumnInfo(name = "startTime")
    public long startTime;

    @ColumnInfo(name = "taskStartTime")
    public long taskStartTime;

    public CustomTimerEntity(Integer routineId, long cumTime, long taskTime, boolean ongoing, long startTime, long taskStartTime) {
        this.routineId = routineId;
        this.cumTime = cumTime;
        this.taskTime = taskTime;
        this.ongoing = ongoing;
        this.startTime = startTime;
        this.taskStartTime = taskStartTime;
    }

    // Method to convert CustomTimerEntity to CustomTimer
    public CustomTimer toCustomTimer() {
        CustomTimer timer = new CustomTimer();
        timer.setCumTime(cumTime);
        timer.setTaskTime(taskTime);
        timer.setOngoing(ongoing);
        timer.setStartTime(Instant.ofEpochMilli(startTime));
        timer.setTaskStartTime(Instant.ofEpochMilli(taskStartTime));
        return timer;
    }

    // Method to create CustomTimerEntity from CustomTimer
    public static CustomTimerEntity fromCustomTimer(CustomTimer timer, Integer routineId) {
        return new CustomTimerEntity(
                routineId,
                timer.getCumTime(),
                timer.getTaskTimeNoReset(),
                timer.getOngoing(),
                timer.getStartTime().toEpochMilli(),
                timer.getTaskStartTime().toEpochMilli()
        );
    }
}
