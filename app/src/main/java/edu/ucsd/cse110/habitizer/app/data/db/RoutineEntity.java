package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.lib.domain.CustomTimer;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

@Entity(
        tableName = "routines",
        foreignKeys = {
                @ForeignKey(entity = CustomTimerEntity.class, parentColumns = "routineId", childColumns = "id")
        },
        indices = {@Index(value = "id", unique = true)}
)
public class RoutineEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id = null;
    
    @ColumnInfo(name = "estimatedTime")
    public Integer estimatedTime;
    
    @ColumnInfo(name = "ongoing")
    public Boolean ongoing;
    
    @ColumnInfo(name = "tasksDone")
    public Integer tasksDone;
    
    @ColumnInfo(name = "name")
    public String name;
    
    public RoutineEntity(int id, int estimatedTime, String name) {
        this.id = id;
        this.name = name;
        this.estimatedTime = estimatedTime;
        this.ongoing = false;
        this.tasksDone = 0;
    }
    
    public static RoutineEntity fromRoutine(@NonNull Routine routine) {
        return new RoutineEntity(routine.getId(), routine.getEstimatedTime(), routine.getName());
    }
    
    public @NonNull Routine toRoutine(List<TaskEntity> taskEntities, CustomTimerEntity timerEntity) {
        Routine r = new Routine(id, estimatedTime, name);
        for (TaskEntity te : taskEntities) {
            r.addTask(te.toTask());
        }
        r.setOngoing(this.ongoing);
        r.setTimer(timerEntity.toCustomTimer());
        r.setTasksDone(this.tasksDone);
        return r;
    }
}
