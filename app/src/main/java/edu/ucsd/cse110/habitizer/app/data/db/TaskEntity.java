package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Insert;
import androidx.room.PrimaryKey;

import edu.ucsd.cse110.habitizer.lib.domain.Task;

@Entity(
        tableName = "tasks",
        primaryKeys = {"routineId", "name"},
        foreignKeys = @ForeignKey(
                entity = RoutineEntity.class, // References RoutineEntity
                parentColumns = "id",         // Primary key in RoutineEntity
                childColumns = "routineId",   // Foreign key in TaskEntity
                onDelete = ForeignKey.CASCADE // Ensures tasks are deleted if routine is deleted
        )
)
public class TaskEntity {
    @ColumnInfo(name = "routineId", index = true)
    public @NonNull Integer routineId;

    @ColumnInfo(name = "name")
    public @NonNull String name;

    @ColumnInfo(name = "completed")
    public boolean completed;

    @ColumnInfo(name = "timeSpent")
    public int timeSpent;

    public static TaskEntity fromTask(@NonNull Task task, int routineId) {
        TaskEntity te = new TaskEntity();
        te.routineId = routineId;
        te.name = task.getName();
        te.completed = task.isCompleted();
        te.timeSpent = task.getTimeSpent();
        return te;
    }

    // Converts this TaskEntity back into a Task domain object.
    public @NonNull Task toTask() {
        Task task = new Task(name);
        // If the task is completed, mark it as such.
        if (completed) {
            task.completeTask();
        }
        task.setTime(timeSpent);
        return task;
    }
}
