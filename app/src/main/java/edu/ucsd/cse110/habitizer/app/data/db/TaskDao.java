package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(TaskEntity task);
    
    @Query("SELECT * FROM tasks WHERE routineId = :routineId")
    List<TaskEntity> findAllByRoutineId(int routineId);
    
    @Query("SELECT * FROM tasks WHERE routineId = :routineId")
    LiveData<List<TaskEntity>> findAllByRoutineIdAsLiveData(int routineId);
    
    @Query("DELETE FROM tasks WHERE routineId = :routineId AND taskName = :taskName")
    void deleteByRoutineIdAndTaskName(int routineId, String taskName);
    
    @Update
    void update(TaskEntity task);
}
