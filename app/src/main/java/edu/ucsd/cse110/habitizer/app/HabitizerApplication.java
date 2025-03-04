package edu.ucsd.cse110.habitizer.app;

import android.app.Application;
import android.util.Log;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.SimpleRoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.SimpleTaskRepository;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;

public class HabitizerApplication extends Application {
    private InMemoryDataSource dataSource;
    private TaskRepository taskRepository;
    private RoutineRepository routineRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("HabitizerApplication", "HabitizerApplication onCreate being called");


        // Initialize data source and repository
        this.dataSource = InMemoryDataSource.fromDefault();
        this.taskRepository = new SimpleTaskRepository(dataSource);
        this.routineRepository = new SimpleRoutineRepository(dataSource);
        Log.d("HabitizerApplication", "HabitizerApplication taskRepository from InMemoryDataSource");

    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }
    
    public RoutineRepository getRoutineRepository() {
        return routineRepository;
    }
}
