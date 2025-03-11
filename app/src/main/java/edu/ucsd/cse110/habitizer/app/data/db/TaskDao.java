package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Transaction
    @Query("DELETE FROM tasks")
    void clearALL();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(TaskEntity task);

    @Transaction
    default int append(TaskEntity task) {
        var maxSortOrder = getMaxSortOrder(task.routineId);
        var newTask  = new TaskEntity(
                task.routineId, task.taskName, maxSortOrder + 1
        );
        return Math.toIntExact(insert(newTask));
    }
    
    @Query("SELECT * FROM tasks WHERE routineId = :routineId ORDER BY sortOrder")
    List<TaskEntity> findAllByRoutineId(int routineId);
    
    @Query("SELECT * FROM tasks WHERE routineId = :routineId ORDER BY sortOrder")
    LiveData<List<TaskEntity>> findAllByRoutineIdAsLiveData(int routineId);
    
    @Query("DELETE FROM tasks WHERE routineId = :routineId AND taskName = :taskName")
    void deleteByRoutineIdAndTaskName(int routineId, String taskName);
    
    @Update
    void update(TaskEntity task);

    @Query("UPDATE tasks SET taskName = :taskName WHERE routineId = :routineId AND taskName = :oldTaskName ")
    void editTask(int routineId, String oldTaskName, String taskName);
    
    @Query("DELETE FROM tasks WHERE routineId = :routineId AND id = :taskId")
    void deleteByRoutineIdAndTaskId(int routineId, int taskId);

    @Query("SELECT MIN(sortOrder) FROM tasks")
    int getMinSortOrder();

    @Query("SELECT MAX(sortOrder) FROM tasks WHERE routineId = :routineId")
    int getMaxSortOrder(int routineId);

    @Query("UPDATE tasks SET sortOrder = :newSortOrder WHERE sortOrder = :oldSortOrder AND routineId = :routineId")
    void updateSortOrder(int routineId, int oldSortOrder, int newSortOrder);


    @Query("UPDATE tasks SET sortOrder = :sortOrder - 1 " +
            "WHERE sortOrder = :sortOrder AND routineId = :routineId")
    void moveTaskUp(int routineId, int sortOrder);


    @Query("SELECT * FROM tasks WHERE routineId = :routineId AND taskName = :taskName")
    TaskEntity find(int routineId, String taskName);
    
    @Query("UPDATE tasks SET completed = :completed WHERE routineId = :routineId AND taskName = :taskName")
    void setCompleted(int routineId, String taskName, boolean completed);
    
    @Query("SELECT completed FROM tasks WHERE routineId = :routineId AND taskName = :taskName")
    boolean getCompleted(int routineId, String taskName);
}
