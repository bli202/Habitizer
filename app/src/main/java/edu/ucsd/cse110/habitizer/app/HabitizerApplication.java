package edu.ucsd.cse110.habitizer.app;

import android.app.Application;
import android.util.Log;

import androidx.room.Room;

import edu.ucsd.cse110.habitizer.app.data.db.HabitizerDatabase;
import edu.ucsd.cse110.habitizer.app.data.db.RoomRoutineRepository;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;

public class HabitizerApplication extends Application {
    private TaskRepository taskRepository;
    private RoutineRepository routineRepository;
    
    @Override
    public void onCreate() {
        super.onCreate();
        String TAG = "HabitizerApplication";
        Log.d(TAG, TAG + "onCreate being called");
        
        
        // Initialize data source and repository
        // TODO must change these next three lines.
        var database = Room.databaseBuilder(
                        getApplicationContext(),
                        HabitizerDatabase.class,
                        "habitizer-database"
                ).allowMainThreadQueries()
                .build();
        
        this.routineRepository = new RoomRoutineRepository(database.routineDao());
        
        // Populate the database with some initial data on the first run.
        var sharedPreferences = getSharedPreferences("habitizer", MODE_PRIVATE);
        var isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        
        if (isFirstRun && database.routineDao().count() == 0) {
            routineRepository.addRoutine(InMemoryDataSource.MORNING_ROUTINE);
            routineRepository.addRoutine(InMemoryDataSource.EVENING_ROUTINE);
            
            sharedPreferences.edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
            Log.d(TAG, TAG + " taskRepository from InMemoryDataSource");
        }
    }
    
    public TaskRepository getTaskRepository() {
        return taskRepository;
    }
    
    public RoutineRepository getRoutineRepository() {
        return routineRepository;
    }
}
