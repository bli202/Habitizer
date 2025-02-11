package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.*;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.observables.*;
public class RoutineRepository {

    private final InMemoryDataSource dataSource;

    public RoutineRepository(InMemoryDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public PlainMutableSubject<Routine> find(int id) {
        return dataSource.getRoutineSubject(id);
    }

}
