package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.observables.Subject;

public class SimpleRoutineRepository implements RoutineRepository {

    private List<Routine> routineList;
    private final InMemoryDataSource datasource;

    public SimpleRoutineRepository(InMemoryDataSource datasource) {
        this.datasource = datasource;
        this.routineList = new ArrayList<>();
    }

    @Override
    public void removeRoutine(Routine routine) {
        if (routine.getOngoing()) {
            throw new IllegalArgumentException("Cannot remove routine while it is ongoing");
        }
        datasource.removeRoutine(routine.getId());
    }

    @Override
    public void addRoutine(Routine routine) {
        for (var r : routineList) {
            if (routine.getId() == r.getId()) {
                throw new IllegalArgumentException("Routine is already added");
            }
        }
        System.out.println(datasource.getRoutines());
        datasource.putRoutine(routine);
        System.out.println(datasource.getRoutines());
    }

    @Override
    public Subject<List<Routine>> getRoutineList() {
        return datasource.getAllRoutinesSubject();
    }
    
    @Override
    public void setEstimatedTime(int routineId, int time) {
        datasource.setEstimatedTime(routineId, time);
    }
    
    /**
     * Gets a routine subject by its name
     * @param id the name of the routine to retrieve
     * @return the specified routine subject
     */
    public Subject<Routine> findRoutine(int id) {
        return datasource.getRoutineSubject(id);
    }

}
