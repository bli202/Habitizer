package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.app.util.LiveDataSubjectAdapter;
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
        routineDao.deleteTimer(routine.getId());
    }
    
    @Override
    public void addRoutine(Routine routine) {
        var routineEntity = new RoutineEntity(routine.getId(), routine.getEstimatedTime(), routine.getName());
        var customTimerEntity = new CustomTimerEntity(routine.getId(), 0, 0, false, 0, 0);
        routineDao.append(routineEntity, customTimerEntity);
    }
    
    @Override
    public Subject<List<Routine>> getRoutineList() {
        // Fetch all RoutineEntity objects from the database
        List<RoutineEntity> routineEntities = routineDao.findAll();
        
        // Convert RoutineEntity objects to Routine objects
        List<Routine> routines = routineEntities.stream()
                .map(routineEntity -> {
                    List<TaskEntity> taskEntities = routineDao.findTasksForRoutine(routineEntity.id);
                    CustomTimerEntity timerEntity = routineDao.findTimerForRoutine(routineEntity.id);
                    return routineEntity.toRoutine(taskEntities, timerEntity);
                })
                .collect(Collectors.toList());
        
        // Wrap the list of Routine objects in a Subject and return
        MutableLiveData<List<Routine>> liveData = new MutableLiveData<>(routines);
        return new LiveDataSubjectAdapter<>(liveData);
    }
    
    @Override
    public void setEstimatedTime(int routineId, int time) {
        routineDao.setEstimatedTime(routineId, time);
    }

    @Override
    public void editRoutineName(int routineId, String newRoutineName) {
        routineDao.updateRoutineName(routineId, newRoutineName);
    }
    }
