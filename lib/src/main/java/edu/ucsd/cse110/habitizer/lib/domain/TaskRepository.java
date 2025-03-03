package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.observables.Subject;

public class TaskRepository {
    private final InMemoryDataSource dataSource;

    public TaskRepository(InMemoryDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Returns the number of tasks in the specified routine.
     */
    public Integer count(int routineId) {
        return dataSource.getTasksForRoutine(routineId).size();
    }

    /**
     * Gets a routine subject by its name
     * @param routineId the id of the routine to retrieve
     * @return the specified routine subject
     */
    public Subject<Routine> findRoutine(int routineId) {
        return dataSource.getRoutineSubject(routineId);
    }

    /**
     * Retrieves an observable subject for the list of tasks in a specific routine.
     */
    public Subject<List<Task>> findAll(int routineId) {
        return dataSource.getRoutineTasksSubject(routineId);
    }

    /**
     * Saves (creates or updates) the given task within the specified routine.
     */
    public void save(int routineId, Task task) {
        dataSource.putTask(routineId, task);
    }

    /**
     * Removes the task with the given name from the specified routine.
     */
    public void remove(int routineId, String taskName) {
        dataSource.removeTask(routineId, taskName);
    }

    /**
     * Edits a task within the specified routine by replacing the old task name with the new one.
     */
    public void edit(int routineId, String oldTaskName, String newTaskName) {
        dataSource.editTask(routineId, oldTaskName, newTaskName);
    }
}
