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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(RoutineEntity routine);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertRoutines(List<RoutineEntity> routines);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertTask(TaskEntity task);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertTasks(List<TaskEntity> tasks);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertTimer(CustomTimerEntity timer);
    
    @Query("SELECT * FROM routines WHERE id = :id")
    RoutineEntity find(int id);
    
    @Query("SELECT * FROM tasks WHERE routineId = :routineId")
    List<TaskEntity> findTasksForRoutine(int routineId);
    
    @Query("SELECT * FROM timers WHERE routineId = :routineId")
    CustomTimerEntity findTimerForRoutine(int routineId);
    
    @Query("SELECT * FROM routines")
    List<RoutineEntity> findAll();
    
    @Query("SELECT * FROM routines WHERE id = :id")
    LiveData<RoutineEntity> findAsLiveData(int id);
    
    @Query("SELECT * FROM routines")
    LiveData<List<RoutineEntity>> findAllAsLiveData();
    
    @Transaction
    default int append(RoutineEntity routine, CustomTimerEntity timer) {
        insertTimer(timer);
        insert(routine);
        return Math.toIntExact(insert(routine));
    }
    
    @Transaction
    default int addTaskToRoutine(RoutineEntity routineEntity, Task task) {
        var routine = routineEntity.toRoutine(findTasksForRoutine(routineEntity.id), findTimerForRoutine(routineEntity.id));
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
}
