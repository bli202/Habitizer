package edu.ucsd.cse110.habitizer.app.data.db;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;
import edu.ucsd.cse110.observables.Subject;

public class RoomRoutineRepository implements TaskRepository {
    private final RoutineDao routineDao;

    public RoomRoutineRepository(RoutineDao routineDao) {
        this.routineDao = routineDao;
    }

    @Override
    public Subject<Routine> find(int id) {

    }
}
