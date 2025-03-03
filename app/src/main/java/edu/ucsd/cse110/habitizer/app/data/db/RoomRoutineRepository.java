package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.app.util.LiveDataSubjectAdapter;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;
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
        routineDao.append(RoutineEntity.fromRoutine(routine));
    }

    @Override
    public Subject<List<Routine>> getRoutineList() {
        var entitiesLiveData = routineDao.findAllAsLiveData();
        var routineLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(RoutineEntity::toRoutine)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(routineLiveData);
    }
}
