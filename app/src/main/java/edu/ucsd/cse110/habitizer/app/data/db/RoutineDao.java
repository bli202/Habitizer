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
    // RoutineEntity operations
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

    // TaskEntity operations within RoutineDao

    // Insert a new TaskEntity row.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTask(TaskEntity taskEntity);

    // Adds a task to a routine by inserting the corresponding TaskEntity.
    @Transaction
    default int addTaskToRoutine(RoutineEntity routineEntity, Task task) {
        if (routineEntity.id == null) {
            throw new IllegalArgumentException("Routine must be inserted first and have a valid id");
        }
        // Create TaskEntity from Task domain object.
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.routineId = routineEntity.id;
        taskEntity.name = task.getName();
        taskEntity.completed = task.isCompleted();
        taskEntity.timeSpent = task.getTimeSpent();

        long taskInsertResult = insertTask(taskEntity);

        // Optionally update the routine's internal state.
        var routine = routineEntity.toRoutine();
        routine.addTask(task);  // Ensure your Routine domain object has addTask(Task)
        insert(RoutineEntity.fromRoutine(routine));

        return Math.toIntExact(taskInsertResult);
    }

    // Update the task's timeSpent and completed status for a given routineId and task name.
    @Query("UPDATE tasks SET timeSpent = :timeSpent, completed = :completed WHERE routineId = :routineId AND name = :taskName")
    int updateTaskTimeAndStatus(int routineId, String taskName, int timeSpent, boolean completed);

    // Updates an existing task row with the latest time and completion status.
    @Transaction
    default int registerTaskTimeForRoutine(RoutineEntity routineEntity, Task task) {
        if (routineEntity.id == null) {
            throw new IllegalArgumentException("Routine must be inserted first and have a valid id");
        }
        int rowsAffected = updateTaskTimeAndStatus(
                routineEntity.id,
                task.getName(),
                task.getTimeSpent(),
                task.isCompleted()
        );
        // Optionally update routine's state if needed.
        var routine = routineEntity.toRoutine();
        insert(RoutineEntity.fromRoutine(routine));
        return rowsAffected;
    }


    @Query("DELETE FROM routines WHERE id = :id")
    void delete(int id);
}
