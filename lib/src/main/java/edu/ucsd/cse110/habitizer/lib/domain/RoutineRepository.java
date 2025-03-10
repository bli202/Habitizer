package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.List;

import edu.ucsd.cse110.observables.Subject;

public interface RoutineRepository {
    void removeRoutine(Routine routine);

    void addRoutine(Routine routine);

    Subject<List<Routine>> getRoutineList();
    
    void setEstimatedTime(int routineId, int time);

    public void editRoutineName(int routineId, String newTaskName);

    void setOngoing(int routineId, boolean b);

    Subject<Boolean> getOngoing(int routineId);
    
    void incrementTasksDone(int routineId);
    
    void resetTasksDone(int routineId);
    
    int getTasksDone(int routineId);
}
