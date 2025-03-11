package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {RoutineEntity.class, TaskEntity.class, CustomTimerEntity.class}, version = 1)
public abstract class HabitizerDatabase extends RoomDatabase {
    public abstract RoutineDao routineDao();
    
    public abstract TaskDao taskDao();
    
    public abstract CustomTimerDao customTimerDao();
}
