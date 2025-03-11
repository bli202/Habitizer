package edu.ucsd.cse110.habitizer.app;

import android.app.Application;
import android.util.Log;

import androidx.room.Room;

import edu.ucsd.cse110.habitizer.app.data.db.HabitizerDatabase;
import edu.ucsd.cse110.habitizer.app.data.db.RoomRoutineRepository;
import edu.ucsd.cse110.habitizer.app.data.db.RoomTaskRepository;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;

public class HabitizerApplication extends Application {
    private TaskRepository taskRepository;
    private RoutineRepository routineRepository;

    private String TAG = "HabitizerApplication";
    
    @Override
    public void onCreate() {
        super.onCreate();
        String TAG = "HabitizerApplication";
        Log.d(TAG, TAG + "onCreate being called");
        
        var database = Room.databaseBuilder(
                        getApplicationContext(),
                        HabitizerDatabase.class,
                        "habitizer-database"
                ).allowMainThreadQueries()
                .build();
        
        this.routineRepository = new RoomRoutineRepository(database.routineDao());
        this.taskRepository = new RoomTaskRepository(database.taskDao());
        
        // Populate the database with some initial data on the first run.
        var sharedPreferences = getSharedPreferences("habitizer", MODE_PRIVATE);
        var isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        
        
        if (isFirstRun && database.routineDao().count() == 0) {
            populateRepositories();
            
            sharedPreferences.edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
            Log.d(TAG, TAG + " taskRepository from InMemoryDataSource");
        }
    }
    
    private void populateRepositories() {
        InMemoryDataSource.fromDefault();
        this.routineRepository.addRoutine(InMemoryDataSource.MORNING_ROUTINE);
        this.routineRepository.addRoutine(InMemoryDataSource.EVENING_ROUTINE);
        
        // MORNING ROUTINE
        this.taskRepository.save(InMemoryDataSource.MORNING_ROUTINE.getId(), InMemoryDataSource.SHOWER);
        this.taskRepository.save(InMemoryDataSource.MORNING_ROUTINE.getId(), InMemoryDataSource.BRUSH_TEETH);
        this.taskRepository.save(InMemoryDataSource.MORNING_ROUTINE.getId(), InMemoryDataSource.DRESS);
        this.taskRepository.save(InMemoryDataSource.MORNING_ROUTINE.getId(), InMemoryDataSource.MAKE_COFFEE);
        this.taskRepository.save(InMemoryDataSource.MORNING_ROUTINE.getId(), InMemoryDataSource.MAKE_LUNCH);
        this.taskRepository.save(InMemoryDataSource.MORNING_ROUTINE.getId(), InMemoryDataSource.DINNER_PREP);
        this.taskRepository.save(InMemoryDataSource.MORNING_ROUTINE.getId(), InMemoryDataSource.PACK_BAG);
        // EVENING ROUTINES
        this.taskRepository.save(InMemoryDataSource.EVENING_ROUTINE.getId(), InMemoryDataSource.CHARGE_DEVICES);
        this.taskRepository.save(InMemoryDataSource.EVENING_ROUTINE.getId(), InMemoryDataSource.PREPARE_DINNER);
        this.taskRepository.save(InMemoryDataSource.EVENING_ROUTINE.getId(), InMemoryDataSource.EAT_DINNER);
        this.taskRepository.save(InMemoryDataSource.EVENING_ROUTINE.getId(), InMemoryDataSource.WASH_DISHES);
        this.taskRepository.save(InMemoryDataSource.EVENING_ROUTINE.getId(), InMemoryDataSource.PACK_BAG_EVENING);
    }

    public void setDataSource(TaskRepository taskRepository, RoutineRepository routineRepository){
        this.taskRepository = taskRepository;
        this.routineRepository = routineRepository;
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }
    
    public RoutineRepository getRoutineRepository() {
        return routineRepository;
    }
}