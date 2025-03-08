package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

@Entity(
        tableName = "tasks",
        primaryKeys = {"routineId", "taskName"},
        foreignKeys = @ForeignKey(
                entity = RoutineEntity.class, // References RoutineEntity
                parentColumns = "id",         // Primary key in RoutineEntity
                childColumns = "routineId",   // Foreign key in TaskEntity
                onDelete = ForeignKey.CASCADE // Ensures tasks are deleted if routine is deleted
        )
)
public class TaskEntity {

//    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id = null;

    @ColumnInfo(name = "routineId")
    public @NonNull Integer routineId;

//    @PrimaryKey
    @ColumnInfo(name = "taskName")
    public @NonNull String taskName;
    
    @ColumnInfo(name = "completed")
    public Boolean completed;
    
    public TaskEntity(@NonNull Integer routineId, @NonNull String taskName) {
        this.routineId = routineId;
        this.taskName = taskName;
        this.completed = false;
    }
    
    // Method to convert TaskEntity to Task
    public Task toTask() {
        return new Task(taskName);
    }
    
    // Method to create TaskEntity from Task
    public static TaskEntity fromTask(int id, Task task) {
        TaskEntity te = new TaskEntity(id, task.getName());
        te.completed = task.isCompleted();
        return te;
    }
}
