package edu.ucsd.cse110.habitizer.app.data.db;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;
import edu.ucsd.cse110.observables.Subject;

public class RoomTaskRepository implements TaskRepository {
    @Override
    public Integer count(int routineId) {
        return 0;
    }
    
    @Override
    public Subject<Routine> findRoutine(int routineId) {
        return null;
    }
    
    @Override
    public Subject<List<Task>> findAll(int routineId) {
        return null;
    }
    
    @Override
    public void save(int routineId, Task task) {
    
    }
    
    @Override
    public void remove(int routineId, String taskName) {
    
    }
    
    @Override
    public void edit(int routineId, String oldTaskName, String newTaskName) {
    
    }
}
