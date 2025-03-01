package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.observables.Subject;

public class SimpleTaskRepository implements TaskRepository {
    private final InMemoryDataSource dataSource;

    public SimpleTaskRepository(InMemoryDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Returns the number of tasks in the specified routine.
     */
    @Override
    public Integer count(String routineName) {
        return dataSource.getTasksForRoutine(routineName).size();
    }

    /**
     * Gets a routine subject by its name
     * @param name the name of the routine to retrieve
     * @return the specified routine subject
     */
    @Override
    public Subject<Routine> findRoutine(String name) {
        return dataSource.getRoutineSubject(name);
    }

    /**
     * Retrieves an observable subject for the list of tasks in a specific routine.
     */
    @Override
    public Subject<List<Task>> findAll(String routineName) {
        return dataSource.getRoutineTasksSubject(routineName);
    }

    /**
     * Saves (creates or updates) the given task within the specified routine.
     */
    @Override
    public void save(String routineName, Task task) {
        dataSource.putTask(routineName, task);
    }

    /**
     * Removes the task with the given name from the specified routine.
     */
    @Override
    public void remove(String routineName, String taskName) {
        dataSource.removeTask(routineName, taskName);
    }

    /**
     * Edits a task within the specified routine by replacing the old task name with the new one.
     */
    @Override
    public void edit(String routineName, String oldTaskName, String newTaskName) {
        dataSource.editTask(routineName, oldTaskName, newTaskName);
    }
}
