package edu.ucsd.cse110.habitizer.lib.data;

import java.util.*;
import edu.ucsd.cse110.observables.*;



import edu.ucsd.cse110.habitizer.lib.domain.Routine;

public class InMemoryDataSource {

    private int nextId = 0;

    // Map of routine id to Routine object.
    private final Map<Integer, Routine> routines = new HashMap<>();

    private final Map<Integer, PlainMutableSubject<Routine>> routineSubjects
            = new HashMap<>();

    private final PlainMutableSubject<List<Routine>> allRoutineSubject = new PlainMutableSubject<>();

    public InMemoryDataSource() {

    }

    public final static List<Routine> DEFAULT_ROUTINES = List.of(
            new Routine(45, "Morning"),
            new Routine(50, "Night")
    );

    public static InMemoryDataSource fromDefault() {
        var data = new InMemoryDataSource();
        for (Routine routine : DEFAULT_ROUTINES) {
            data.putRoutines(routine);
        }
        return data;
    }

    public List<Routine> getRoutines() {
        return List.copyOf(routines.values());
    }

    public Routine getRoutine(int id) {
        return routines.get(id);
    }

    public PlainMutableSubject<Routine> getRoutineSubject(int id) {
        if (!routineSubjects.containsKey(id)) {
            PlainMutableSubject<Routine> subject = new PlainMutableSubject<>();
            subject.setValue(getRoutine(id));
            routineSubjects.put(id, subject);
        }

        return routineSubjects.get(id);
    }

    public MutableSubject<List<Routine>> getAllRoutinesSubjects() {
        return allRoutineSubject;
    }

    public void putRoutines(Routine routine) {
        int id = nextId++;
        routines.put(id, routine);

        if (routineSubjects.containsKey(id)) {
            routineSubjects.get(id).setValue(routine);
        }

        allRoutineSubject.setValue(getRoutines());
    }

}