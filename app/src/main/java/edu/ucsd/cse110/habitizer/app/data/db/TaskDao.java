package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(TaskEntity task);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(List<TaskEntity> tasks);

    @Query("SELECT * FROM tasks WHERE routineId = :id AND name = :name")
    TaskEntity findTaskByIdName(int id, String name);

    @Query("SELECT * FROM tasks WHERE routineId = :routineId")
    List<TaskEntity> findAllByRoutine(int routineId);

    @Query("SELECT * FROM tasks WHERE routineId = :routineId")
    LiveData<List<TaskEntity>> findAllByRoutineAsLiveData(int routineId);

    @Delete
    void delete(TaskEntity task);

    @Query("UPDATE tasks SET name = :newTaskName WHERE routineId = :routineId AND name = :oldTaskName")
    int editTaskName(int routineId, String oldTaskName, String newTaskName);

    @Query("DELETE FROM tasks WHERE routineId = :routineId")
    void deleteAllByRoutine(int routineId);

    @Query("DELETE FROM tasks WHERE routineId = :routineId")
    void deleteTasksByRoutine(int routineId);

    @Query("UPDATE tasks SET timeSpent = :timeSpent, completed = :completed WHERE routineId = :routineId AND name = :taskName")
    int updateTaskTimeAndStatus(int routineId, String taskName, int timeSpent, boolean completed);

}
