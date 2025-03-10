package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.List;

import edu.ucsd.cse110.observables.Subject;

public interface TaskRepository {

    /**
     * Returns the number of tasks in the specified routine.
     */
    @SuppressWarnings("unused")
    Integer count(int routineId);

    /**
     * Gets a routine subject by its name
     * @param routineId the id of the routine to retrieve
     * @return the specified routine subject
     */
    @SuppressWarnings("unused")
    Subject<Routine> findRoutine(int routineId);

    /**
     * Retrieves an observable subject for the list of tasks in a specific routine.
     */
    Subject<List<Task>> findAllTasksForRoutineSubject(int routineId);
    
    List<Task> findAllTasksForRoutine(int routineId);

    /**
     * Saves (creates or updates) the given task within the specified routine.
     */
    void save(int routineId, Task task);

    /**
     * Removes the task with the given name from the specified routine.
     */
    void remove(int routineId, String taskName);
    
    void remove(int routineId, int taskName);

    /**
     * Edits a task within the specified routine by replacing the old task name with the new one.
     */
    void edit(int routineId, String oldTaskName, String newTaskName);

    void moveUp(int routineId, int order);

    void moveDown(int routineId, int order);
    
    public void setCompleted(int routineId, String taskName, boolean completed);
    
    public boolean getCompleted(int routineId, String taskName);

    int getTime(int routineId, String taskName);

    void setTime(int routineId, String taskName);
}
