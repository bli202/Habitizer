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
    public Integer count(String routineName) {
        return dataSource.getTasksForRoutine(routineName).size();
    }

    /**
     * Retrieves an observable subject for a specific task by name.
     * Note: This assumes that task names are unique across routines.
     */
//    public Subject<Task> findTask(String routineName, String taskName) {
//        // Optionally, you could verify that the task exists in the specified routine.
//        List<Task> taskL = dataSource.getTasksForRoutine(routineName);
//
//        for (Task task : taskL) {
//            if (task.getName().equals(taskName)) {
//                return dataSource.getTaskSubject(taskName);
//            }
//        }
//        return null;
//    }

    public Subject<Routine> findRoutine(String name) {
        return dataSource.getRoutineSubject(name);
    }

    /**
     * Retrieves an observable subject for the list of tasks in a specific routine.
     */
    public Subject<List<Task>> findAll(String routineName) {
        return dataSource.getRoutineTasksSubject(routineName);
    }

    /**
     * Saves (creates or updates) the given task within the specified routine.
     */
    public void save(String routineName, Task task) {
        dataSource.putTask(routineName, task);
    }

    /**
     * Removes the task with the given name from the specified routine.
     */
    public void remove(String routineName, String taskName) {
        dataSource.removeTask(routineName, taskName);
    }

    /**
     * Edits a task within the specified routine by replacing the old task name with the new one.
     */
    public void edit(String routineName, String oldTaskName, String newTaskName) {
        dataSource.editTask(routineName, oldTaskName, newTaskName);
    }
}
