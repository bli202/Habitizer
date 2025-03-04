package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.lib.domain.CustomTimer;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;


@Entity(tableName = "routines")
public class RoutineEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id = null;
    @ColumnInfo(name = "estimatedTime")
    public int estimatedTime;
    @ColumnInfo(name = "ongoing")
    public boolean ongoing;
    @ColumnInfo(name = "tasksDone")
    public int tasksDone;
    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "cumTime")      // Total elapsed time in milliseconds.
    public long cumTime;

    @ColumnInfo(name = "taskTime")     // Time for the current task in milliseconds.
    public long taskTime;

    RoutineEntity(int id, int estimatedTime, String name) {
        this.id = id;
        this.name = name;
        this.estimatedTime = estimatedTime;
        this.ongoing = false;
        this.cumTime = 0;
        this.taskTime = 0;
        this.tasksDone = 0;
    }

    public static RoutineEntity fromRoutine(@NonNull Routine routine) {
        RoutineEntity re = new RoutineEntity(routine.getId(), routine.getEstimatedTime(), routine.getName());
        re.ongoing = routine.getOngoing();
        re.cumTime = routine.getTimer().getTime() * 1000;
        re.taskTime = routine.getTimer().getTaskTimeNoReset() * 1000;
        re.tasksDone = routine.getTasksDone();
        return re;
    }

    public @NonNull Routine toRoutine() {
        Routine r = new Routine(id, estimatedTime, name);
        r.setOngoing(this.ongoing);
        // Create a new timer and restore the primitive values.
        CustomTimer timer = new CustomTimer();
        timer.addTime(this.cumTime / 1000); // addTime expects seconds
        // If you need to also set taskTime explicitly, you could create a method in CustomTimer for that.
        r.setTimer(timer);
        r.setTasksDone(this.tasksDone);
        return r;
    }


    // Insert a TaskEntity directly in RoutineDao

}

