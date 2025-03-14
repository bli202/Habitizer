package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.domain.Task;

@Dao
public interface RoutineDao {

    @Transaction
    @Query("DELETE from routines")
    void clearAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(RoutineEntity routine);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertTask(TaskEntity task);

    @Query("SELECT * FROM routines WHERE id = :id")
    RoutineEntity find(int id);
    
    @Query("SELECT * FROM tasks WHERE routineId = :routineId")
    List<TaskEntity> findTasksForRoutine(int routineId);
    
    @Query("SELECT * FROM routines")
    List<RoutineEntity> findAll();

    /**
     * Updates the name of a routine.
     *
     * @param id The ID of the routine to update
     * @param newName The new name for the routine
     */
    @Query("UPDATE routines SET name = :newName WHERE id = :id")
    void updateRoutineName(int id, String newName);

    @Query("SELECT * FROM routines WHERE id = :id")
    LiveData<RoutineEntity> findAsLiveData(int id);

    @Query("SELECT * FROM routines")
    LiveData<List<RoutineEntity>> findAllAsLiveData();
    
    @Transaction
    default int append(RoutineEntity routine) {
        insert(routine);
        return Math.toIntExact(insert(routine));
    }
    
    @Transaction
    default int addTaskToRoutine(RoutineEntity routineEntity, Task task) {
        var routine = routineEntity.toRoutine(findTasksForRoutine(routineEntity.id));
        routine.addTask(task);
        insertTask(TaskEntity.fromTask(routine.getId(), task));
        return Math.toIntExact(insert(RoutineEntity.fromRoutine(routine)));
    }
    @Query("DELETE FROM routines WHERE id = :id")
    void delete(int id);
    
    @Query("UPDATE routines SET estimatedTime = :time WHERE id = :routineId")
    void setEstimatedTime(int routineId, int time);
    
    @Query("SELECT COUNT(*) FROM routines")
    int count();

    @Query("UPDATE routines SET ongoing = :b WHERE id = :routineId")
    void setOngoing(int routineId, boolean b);

    @Query("SELECT ongoing FROM routines WHERE id = :routineId")
    LiveData<Boolean> getOngoingAsLiveData(int routineId);
    
    @Query("UPDATE routines SET tasksDone = tasksDone + 1 WHERE id = :routineId")
    void incrementTasksDone(int routineId);
    
    @Query("UPDATE routines SET tasksDone = 0 WHERE id = :routineId")
    void resetTasksDone(int routineId);
    
    @Query("SELECT tasksDone FROM routines WHERE id = :routineId")
    int getTasksDone(int routineId);
}
