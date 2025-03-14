package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.observables.Subject;

@SuppressWarnings("unused")
public class SimpleTaskRepository implements TaskRepository {
    private final InMemoryDataSource dataSource;
    
    public SimpleTaskRepository(InMemoryDataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    /**
     * Returns the number of tasks in the specified routine.
     */
    @Override
    public Integer count(int routineId) {
        return dataSource.getTasksForRoutine(routineId).size();
    }
    
    /**
     * Gets a routine subject by its name
     *
     * @param id the id of the routine to retrieve
     * @return the specified routine subject
     */
    @Override
    public Subject<Routine> findRoutine(int id) {
        return dataSource.getRoutineSubject(id);
    }
    
    /**
     * Retrieves an observable subject for the list of tasks in a specific routine.
     */
    @Override
    public Subject<List<Task>> findAllTasksForRoutineSubject(int routineId) {
        return dataSource.getRoutineTasksSubject(routineId);
    }
    
    @Override
    public List<Task> findAllTasksForRoutine(int routineId) {
        return List.of();
    }
    
    /**
     * Saves (creates or updates) the given task within the specified routine.
     */
    @Override
    public void save(int routineId, Task task) {
        dataSource.putTask(routineId, task);
    }
    
    /**
     * Removes the task with the given name from the specified routine.
     */
    @Override
    public void remove(int routineId, String taskName) {
        dataSource.removeTask(routineId, taskName);
    }
    
    @Override
    public void remove(int routineId, int taskId) {
        dataSource.removeTaskById(routineId, taskId);
    }
    
    /**
     * Edits a task within the specified routine by replacing the old task name with the new one.
     */
    @Override
    public void edit(int routineId, String oldTaskName, String newTaskName) {
        dataSource.editTask(routineId, oldTaskName, newTaskName);
    }
    
    @Override
    public void moveUp(int routineId, int order) {
    
    }
    
    @Override
    public void moveDown(int routineId, int order) {
    
    }
    
    @Override
    public void setCompleted(int routineId, String taskName, boolean completed) {
    
    }
    
    @Override
    public boolean getCompleted(int routineId, String taskName) {
        return false;
    }

    @Override
    public int getTime(int routineId, String taskName) {
        return 0;
    }

    @Override
    public void setTime(int routineId, String taskName, int time) {

    }

    @Override
    public void clear(){}
}
