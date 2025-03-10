package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import edu.ucsd.cse110.habitizer.lib.domain.Task;

@Entity(
        tableName = "tasks",
        primaryKeys = {"routineId", "taskName"},
        foreignKeys = @ForeignKey(
                entity = RoutineEntity.class, // References RoutineEntity
                parentColumns = "id",         // Primary key in RoutineEntity
                childColumns = "routineId",   // Foreign key in TaskEntity
                onDelete = ForeignKey.CASCADE // Ensures tasks are deleted if routine is deleted
        ),
//        indices = {@Index(value = "sortOrder", unique = true)}
        indices = {@Index(value = {"routineId", "sortOrder"}, unique = true)}
)
public class TaskEntity {

    @ColumnInfo(name = "id")
    public Integer id = null;

    @ColumnInfo(name = "sortOrder")
    public @NonNull Integer sortOrder;

    @ColumnInfo(name = "routineId")
    public @NonNull Integer routineId;

    @ColumnInfo(name = "taskName")
    public @NonNull String taskName;
    
    @ColumnInfo(name = "completed")
    public Boolean completed;

    @ColumnInfo(name = "taskTime")
    public Integer taskTime;
    
    public TaskEntity(@NonNull Integer routineId, @NonNull String taskName, @NonNull Integer sortOrder) {
        this.routineId = routineId;
        this.taskName = taskName;
        this.completed = false;
        this.sortOrder = sortOrder;
    }
    
    // Method to convert TaskEntity to Task
    public Task toTask() {
        return new Task(taskName, sortOrder);
    }
    
    // Method to create TaskEntity from Task
    public static TaskEntity fromTask(int id, Task task) {
        TaskEntity te = new TaskEntity(id, task.getName(), task.getOrder());
        te.completed = task.isCompleted();
        return te;
    }

}
