package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.app.util.LiveDataSubjectAdapter;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;
import edu.ucsd.cse110.observables.MutableSubject;
import edu.ucsd.cse110.observables.PlainMutableSubject;
import edu.ucsd.cse110.observables.Subject;

public class RoomTaskRepository implements TaskRepository {
    private final TaskDao taskDao;
    
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
    public Subject<List<Task>> findAll(int routineId) {
        List<TaskEntity> taskEntities = taskDao.findAllByRoutineId(routineId);
        List<Task> tasks = taskEntities.stream()
                .map(TaskEntity::toTask)
                .collect(Collectors.toList());
        
        MutableLiveData<List<Task>> liveData = new MutableLiveData<>(tasks);
        return new LiveDataSubjectAdapter<>(liveData);
    }
    
    @Override
    public void save(int routineId, Task task) {
        TaskEntity taskEntity = TaskEntity.fromTask(routineId, task);
        taskDao.insert(taskEntity);
    }
    
    @Override
    public void remove(int routineId, String taskName) {
        taskDao.deleteByRoutineIdAndTaskName(routineId, taskName);
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
}
