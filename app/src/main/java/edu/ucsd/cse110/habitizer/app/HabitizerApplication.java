package edu.ucsd.cse110.habitizer.app;

import android.app.Application;
import android.util.Log;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;

public class HabitizerApplication extends Application {
    private InMemoryDataSource dataSource;
    private TaskRepository taskRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize data source and repository
        this.dataSource = InMemoryDataSource.fromDefault();
        this.taskRepository = new TaskRepository(dataSource);
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }
}
