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
        if (!routineSubject.containsKey(task.getName()) && !routine.containsKey(task.getName())) {
            routine.put(task.getName(), task);
            PlainMutableSubject<Task> subject = new PlainMutableSubject<>();
            subject.setValue(getTask(task.getName()));
            routineSubject.put(task.getName(), subject);
        }
        allRoutineSubject.setValue(getTasks());
    }

    public void editTask(Task task, String name) {
        if (!routine.containsKey(task.getName())) {
           task.setName(name);
        }
        else {
            throw new NullPointerException("has key");
        }
    }

    public final static List<Task> ROUTINE_1 = List.of(
            new Task("brush teeth"),
            new Task("shower"),
            new Task("eat breakfast"),
            new Task("skin care")
    );

    public final static List<Task> ROUTINE_2 = List.of(
            new Task("skin care"),
            new Task("brush teeth"),
            new Task("floss")

    );

    public static InMemoryDataSource fromDefault() {
        var data = new InMemoryDataSource();
        for (Task task : ROUTINE_1) {
            data.putTask(task);
        }
        return data;
    }

}



