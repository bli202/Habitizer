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
    List<Long> insert(List<RoutineEntity> routines);

    @Query("SELECT * FROM routines WHERE id = :id")
    RoutineEntity find(int id);

    @Query("SELECT * FROM routines")
    List<RoutineEntity> findAll();

    @Query("SELECT * FROM routines WHERE id = :id")
    LiveData<RoutineEntity> findAsLiveData(int id);

    @Query("SELECT * FROM routines")
    LiveData<List<RoutineEntity>> findAllAsLiveData();

    @Transaction
    default int append(RoutineEntity routine) {
        return Math.toIntExact(insert(routine));
    }

    @Transaction
    default int addTaskToRoutine(RoutineEntity routineEntity, Task task) {
        var routine = routineEntity.toRoutine();
        routine.addTask(task);
        return Math.toIntExact(insert(RoutineEntity.fromRoutine(routine)));
    }

    @Query("DELETE FROM routines WHERE id = :id")
    void delete(int id);
}
