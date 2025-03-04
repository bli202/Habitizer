package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.List;

import edu.ucsd.cse110.observables.Subject;

public interface RoutineRepository {
    void removeRoutine(Routine routine);

    void addRoutine(Routine routine);

    Subject<List<Routine>> getRoutineList();

    void removeTaskFromRoutine(Routine value, Task task);

    Subject<List<Task>> getTaskListForRoutine(Routine morningRoutine);

    void addTaskToRoutine(Routine routine, Task task);

    void editTask(int id, String oldName, String newName);
}
