package edu.ucsd.cse110.habitizer.app.data.db;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;
import edu.ucsd.cse110.observables.PlainMediatorSubject;
import edu.ucsd.cse110.observables.PlainMutableSubject;
import edu.ucsd.cse110.observables.Subject;

public class RoomRoutineRepository implements RoutineRepository {
    private final RoutineDao routineDao;

    public RoomRoutineRepository(RoutineDao routineDao) {
        this.routineDao = routineDao;
    }
    
    @Override
    public void removeRoutine(Routine routine) {
        routineDao.delete(routine.getId());
    }
    
    @Override
    public void addRoutine(Routine routine) {
        routineDao.append(new RoutineEntity(routine.getId(), routine.getEstimatedTime(), routine.getName()));
    }
    
    @Override
    public void setEstimatedTime(int routineId, int time) {
        routineDao.setEstimatedTime(routineId, time);
    }
}
