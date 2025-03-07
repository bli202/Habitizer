package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TaskEntity task);
    
    @Query("SELECT * FROM tasks WHERE routineId = :routineId")
    List<TaskEntity> findAllByRoutineId(int routineId);
    
    @Query("SELECT * FROM tasks WHERE routineId = :routineId")
    LiveData<List<TaskEntity>> findAllByRoutineIdAsLiveData(int routineId);
    
    @Query("DELETE FROM tasks WHERE routineId = :routineId AND taskName = :taskName")
    void deleteByRoutineIdAndTaskName(int routineId, String taskName);
    
    @Update
    void update(TaskEntity task);
    
    @Query("DELETE FROM tasks WHERE routineId = :routineId AND id = :taskId")
    void deleteByRoutineIdAndTaskId(int routineId, int taskId);

    @Query("SELECT * FROM tasks WHERE taskName = :taskName AND routineId = :routineId")
    TaskEntity getTask(int routineId, String taskName);
}
