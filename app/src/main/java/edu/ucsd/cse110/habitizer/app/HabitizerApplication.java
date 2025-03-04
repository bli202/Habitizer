package edu.ucsd.cse110.habitizer.app;

import android.app.Application;
import android.util.Log;

import androidx.room.Room;

import java.util.List;

import edu.ucsd.cse110.habitizer.app.data.db.HabitizerDatabase;
import edu.ucsd.cse110.habitizer.app.data.db.RoomRoutineRepository;
import edu.ucsd.cse110.habitizer.app.data.db.RoutineEntity;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;

public class HabitizerApplication extends Application {
    private RoutineRepository routineRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Room database
        var database = Room.databaseBuilder(
                        getApplicationContext(),
                        HabitizerDatabase.class,
                        "habitizer-database"
                )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        this.routineRepository = new RoomRoutineRepository(database.routineDao(), database.taskDao());

        // Handle first-run logic
        var sharedPreferences = getSharedPreferences("habitizer_prefs", MODE_PRIVATE);
        var isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);

        if (isFirstRun) {
            Log.d("HabitizerApplication", "First run detected, inserting default routines...");

            List<RoutineEntity> existingRoutines = database.routineDao().findAll();

            if (existingRoutines.isEmpty()) {
                Log.d("HabitizerApplication", "No routines found in database. Adding default routines...");
                for (Routine defaultRoutine : InMemoryDataSource.DEFAULT_ROUTINES) {
                    routineRepository.addRoutine(defaultRoutine);
                    Log.d("HabitizerApplication", "Inserted routine: " + defaultRoutine.getName());
                }
            } else {
                Log.d("HabitizerApplication", "Database already has " + existingRoutines.size() + " routines. Skipping default insertion.");
            }

            sharedPreferences.edit().putBoolean("isFirstRun", false).apply();
        }
    }


    public RoutineRepository getRoutineRepository() {
        return routineRepository;
    }
}
