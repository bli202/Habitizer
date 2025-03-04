package edu.ucsd.cse110.habitizer.app;

import android.app.Application;
import android.util.Log;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.SimpleRoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.SimpleTaskRepository;
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
        InMemoryDataSource dataSource = InMemoryDataSource.fromDefault();
        this.taskRepository = new SimpleTaskRepository(dataSource);
        this.routineRepository = new SimpleRoutineRepository(dataSource);
        Log.d(TAG, TAG +" taskRepository from InMemoryDataSource");

    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }
    
    public RoutineRepository getRoutineRepository() {
        return routineRepository;
    }
}
