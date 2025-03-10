package edu.ucsd.cse110.habitizer.app.data.db;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.room.Query;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.app.util.LiveDataSubjectAdapter;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;
import edu.ucsd.cse110.observables.Subject;

public class RoomTaskRepository implements TaskRepository {
    private final TaskDao taskDao;
    private String TAG = "RoomTaskRepository";
    
    public RoomTaskRepository(TaskDao taskDao) {
        this.taskDao = taskDao;
    }
    
    @Override
    public Integer count(int routineId) {
        return taskDao.findAllByRoutineId(routineId).size();
    }
    
    @Override
    public Subject<Routine> findRoutine(int routineId) {
        // Implement this method if you have a RoutineDao to get Routine by routineId
        return null;
    }
    
    @Override
    public Subject<List<Task>> findAllTasksForRoutineSubject(int routineId) {
        LiveData<List<TaskEntity>> taskEntities = taskDao.findAllByRoutineIdAsLiveData(routineId);
        LiveData<List<Task>> tasks = Transformations.map(taskEntities, entities -> entities.stream()
            .map(TaskEntity::toTask)
            .collect(Collectors.toList()));
        return new LiveDataSubjectAdapter<>(tasks);
    }
    public List<Task> findAllAsList(int routineId) {
        List<TaskEntity> taskEntities = taskDao.findAllByRoutineId(routineId);
        return taskEntities.stream()
                .map(TaskEntity::toTask)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Task> findAllTasksForRoutine(int routineId) {
        return taskDao.findAllByRoutineId(routineId).stream().map(TaskEntity::toTask).collect(Collectors.toList());
        
    }
    
    @Override
    public void setCompleted(int routineId, String taskName, boolean completed) {
        taskDao.setCompleted(routineId, taskName, completed);
    }
    
    @Override
    public boolean getCompleted(int routineId, String taskName) {
        return taskDao.getCompleted(routineId, taskName);
    }
    
    @Override
    public void save(int routineId, Task task) {
        TaskEntity taskEntity = TaskEntity.fromTask(routineId, task);
        taskDao.append(taskEntity);
    }
    
    @Override
    public void remove(int routineId, String taskName) {
        int order = taskDao.find(routineId, taskName).sortOrder;
        taskDao.deleteByRoutineIdAndTaskName(routineId, taskName);
        for (var t: findAllAsList(routineId)) {
            if (t.getOrder() > order) {
                moveTaskUp(routineId, t.getOrder());
            }
        }
    }
    
    @Override
    public void remove(int routineId, int taskId) {
        taskDao.deleteByRoutineIdAndTaskId(routineId, taskId);
    }

    
    @Override
    public void edit(int routineId, String oldTaskName, String newTaskName) {
        List<TaskEntity> taskEntities = taskDao.findAllByRoutineId(routineId);
        for (TaskEntity taskEntity : taskEntities) {
            if (taskEntity.taskName.equals(oldTaskName)) {
                taskEntity.taskName = newTaskName;
                taskDao.editTask(routineId, oldTaskName, newTaskName);
                break;
            }
        }
    }

    public void moveUp(int routineId, int order) {
        if (order != 1) {
            swapTasks(routineId, order - 1, order);
        }
    }

    public void moveDown(int routineId, int order) {
        if (order != taskDao.getMaxSortOrder(routineId)) {
            swapTasks(routineId, order, order + 1);
        }
    }

    public void swapTasks(int routineId, int sortOrder1, int sortOrder2) {
        taskDao.updateSortOrder(routineId, sortOrder1, -1);
        taskDao.updateSortOrder(routineId, sortOrder2, sortOrder1);
        taskDao.updateSortOrder(routineId, -1, sortOrder2);
    }

    public void moveTaskUp(int routineId, int sortOrder) {
        taskDao.moveTaskUp(routineId, sortOrder);
    }

}
