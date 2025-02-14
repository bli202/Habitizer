package edu.ucsd.cse110.habitizer.lib.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.observables.MutableSubject;
import edu.ucsd.cse110.observables.PlainMediatorSubject;
import edu.ucsd.cse110.observables.PlainMutableSubject;
import edu.ucsd.cse110.observables.Subject;

public class InMemoryDataSource {
    private final Map<String, Task> routine
            = new HashMap<>();
    private final Map<String, PlainMutableSubject<Task>> routineSubject
            = new HashMap<>();
    private final PlainMutableSubject<List<Task>> allRoutineSubject
            = new PlainMutableSubject<>();

    public InMemoryDataSource() {
    }

    public List<Task> getTasks() {
        return List.copyOf(routine.values());
    }

    public Task getTask(String name) {
        return routine.get(name);
    }

    public Subject<Task> getTaskSubject(String name) {
        if (!routineSubject.containsKey(name)) {
            PlainMutableSubject<Task> subject = new PlainMutableSubject<>();
            subject.setValue(getTask(name));
            routineSubject.put(name, subject);
        }
        return routineSubject.get(name);
    }

    public Subject<List<Task>> getAllTasksSubject() {
        return allRoutineSubject;
    }

    public void putTask(Task task) {
        routine.put(task.getName(), task);
        if (routineSubject.containsKey(task.getName())) {
            routineSubject.get(task.getName()).setValue(task);
        }
        allRoutineSubject.setValue(getTasks());
    }

    public void removeTask(String name) {
        routine.remove(name);
        allRoutineSubject.setValue(getTasks());
    }

    public final static List<Task> ROUTINE_1 = List.of(
            new Task("brush teeth")
    );

    public static InMemoryDataSource fromDefault() {
        var data = new InMemoryDataSource();
        for (Task task : ROUTINE_1) {
            data.putTask(task);
        }
        return data;
    }

}



