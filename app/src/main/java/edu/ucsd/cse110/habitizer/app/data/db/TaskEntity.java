package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

@Entity(tableName = "tasks")
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id = null;
    
    @ColumnInfo(name = "routineId")
    public Integer routineId;
    
    @ColumnInfo(name = "taskName")
    public String taskName;
    
    @ColumnInfo(name = "completed")
    public Boolean completed;
    
    public TaskEntity(Integer routineId, String taskName) {
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
