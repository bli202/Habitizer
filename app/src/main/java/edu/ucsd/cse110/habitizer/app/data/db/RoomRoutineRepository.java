package edu.ucsd.cse110.habitizer.app.data.db;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.app.util.LiveDataSubjectAdapter;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;
import edu.ucsd.cse110.observables.PlainMutableSubject;
import edu.ucsd.cse110.observables.Subject;

public class RoomRoutineRepository implements RoutineRepository{
    private final RoutineDao routineDao;
    private final TaskDao taskDao;

    public RoomRoutineRepository(RoutineDao routineDao, TaskDao taskDao) {
        this.routineDao = routineDao;
        this.taskDao = taskDao;
    }

    // ---------------- RoutineRepository Methods ----------------

    @Override
    public void addRoutine(Routine routine) {
        // Convert domain Routine to RoutineEntity and insert it.
        routineDao.append(RoutineEntity.fromRoutine(routine));
    }

    @Override
    public void removeRoutine(Routine routine) {
        routineDao.delete(routine.getId());
    }

    @Override
    public Subject<List<Routine>> getRoutineList() {
        LiveData<List<RoutineEntity>> entitiesLiveData = routineDao.findAllAsLiveData();
        LiveData<List<Routine>> routinesLiveData = Transformations.map(entitiesLiveData, entities ->
                entities.stream()
                        .map(RoutineEntity::toRoutine)
                        .collect(Collectors.toList())
        );
        return new LiveDataSubjectAdapter<>(routinesLiveData);
    }

    // ---------------- TaskRepository Methods ----------------

    @Override
    public void editTask(int routineId, String oldTaskName, String newTaskName) {
        // Check if the routine exists
        RoutineEntity routineEntity = routineDao.find(routineId);
        if (routineEntity == null) return;

        // Find existing task
        TaskEntity existingTask = taskDao.findTaskByIdName(routineId, oldTaskName);
        if (existingTask == null) return;

        // Ensure new task name is not already in use
        TaskEntity duplicateTask = taskDao.findTaskByIdName(routineId, newTaskName);
        if (duplicateTask != null) {
            throw new IllegalArgumentException("A task with this name already exists in the routine.");
        }

        // Update task name in the database
        taskDao.editTaskName(routineId, oldTaskName, newTaskName);
    }

    public void addTaskToRoutine(Routine routine, Task task) {
        RoutineEntity routineEntity = RoutineEntity.fromRoutine(routine);
        routineDao.addTaskToRoutine(routineEntity, task);
    }


    public int updateTaskTimeForRoutine(Routine routine, Task task) {
        // Update a task's time and completion status.
        RoutineEntity routineEntity = RoutineEntity.fromRoutine(routine);
        return routineDao.registerTaskTimeForRoutine(routineEntity, task);
    }

    public Subject<List<Task>> getTaskListForRoutine(Routine routine) {
        if (routine == null) {
            Log.e("RoomRoutineRepository", "Error: Routine is NULL in getTaskListForRoutine!");
            return new PlainMutableSubject<>(List.of()); // Return an empty list to avoid crashing
        }

        LiveData<List<TaskEntity>> taskEntitiesLiveData = taskDao.findAllByRoutineAsLiveData(routine.getId());
        LiveData<List<Task>> tasksLiveData = Transformations.map(taskEntitiesLiveData, entities ->
                entities.stream()
                        .map(TaskEntity::toTask)
                        .collect(Collectors.toList())
        );

        return new LiveDataSubjectAdapter<>(tasksLiveData);
    }



    public void removeTaskFromRoutine(Routine routine, Task task) {
        // Remove a task from the tasks table using TaskDao.
        TaskEntity taskEntity = taskDao.findTaskByIdName(routine.getId(), task.getName());
        if (taskEntity != null) {
            taskDao.delete(taskEntity);
        }
        // Optionally, update routine state here if necessary.
    }
}
