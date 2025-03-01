package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.List;

import edu.ucsd.cse110.observables.Subject;

public interface TaskRepository {
    Integer count(String routineName);

    Subject<Routine> findRoutine(String name);

    Subject<List<Task>> findAll(String routineName);

    void save(String routineName, Task task);

    void remove(String routineName, String taskName);

    void edit(String routineName, String oldTaskName, String newTaskName);
}
