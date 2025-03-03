package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
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

    @ColumnInfo(name = "timer")
    public CustomTimer timer;

    RoutineEntity(int id, int estimatedTime, String name) {
        this.id = id;
        this.name = name;
        this.estimatedTime = estimatedTime;
        this.ongoing = false;
        this.timer = new CustomTimer();
        this.tasksDone = 0;
    }

    public static RoutineEntity fromRoutine(@NonNull Routine routine) {
        RoutineEntity re = new RoutineEntity(routine.getId(), routine.getEstimatedTime(), routine.getName());
        re.ongoing = routine.getOngoing();
        re.timer = routine.getTimer();
        re.tasksDone = routine.getTasksDone();
        return re;
    }

    public @NonNull Routine toRoutine() {
        Routine r = new Routine(id, estimatedTime, name);
        r.setOngoing(this.ongoing);
        r.setTimer(this.timer);
        r.setTasksDone(this.tasksDone);
        return r;
    }
}
