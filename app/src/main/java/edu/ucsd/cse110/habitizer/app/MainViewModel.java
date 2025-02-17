package edu.ucsd.cse110.habitizer.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.ArrayList;
import java.util.List;

import java.util.Objects;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;
import edu.ucsd.cse110.observables.PlainMutableSubject;
import edu.ucsd.cse110.observables.Subject;

public class MainViewModel extends ViewModel {

    private static final String LOG_TAG = "MainViewModel";

    // Domain state (Model) and current routine context.
    private final TaskRepository taskRepository;

    // UI state observables.
    private final PlainMutableSubject<List<Task>> morningTasks;
    private final PlainMutableSubject<List<Task>> eveningTasks;
    private final PlainMutableSubject<Integer> estimatedTime;

    private final PlainMutableSubject<Task> firstTask;
    private final PlainMutableSubject<Boolean> completed;
    private final PlainMutableSubject<String> taskName;
    private static final PlainMutableSubject<Routine> curRoutine = new PlainMutableSubject<>(InMemoryDataSource.MORNING_ROUTINE);

    public static final ViewModelInitializer<MainViewModel> initializer = new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (HabitizerApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        // Here we are initializing the view model for a specific routine.
                        return new MainViewModel(app.getTaskRepository());
                    });

    public MainViewModel(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        //this.routineName = routineName;

        Log.d(LOG_TAG, "MainViewModel constructor");

        this.morningTasks = new PlainMutableSubject<>();
        this.firstTask = new PlainMutableSubject<>();
        this.completed = new PlainMutableSubject<>(false);
        this.taskName = new PlainMutableSubject<>();
        this.eveningTasks = new PlainMutableSubject<>();
        this.estimatedTime = new PlainMutableSubject<>();

        // Observe tasks for the specified routine.


        taskRepository.findAll(InMemoryDataSource.MORNING_ROUTINE.getName())
                .observe(tasks -> {
            if (tasks == null) return; // Not ready yet, ignore.
            // Create a new ordered list (you can add a Comparator if needed).
            List<Task> morningTasks = new ArrayList<>(tasks);
            this.morningTasks.setValue(morningTasks);
            Log.d("MainViewModel", "Number of Tasks in morningTasks: " + this.morningTasks.getValue().size());

            Log.d("MainViewModel", "Number of Tasks in curRoutine: " + getCurRoutine().getValue().getNumTasks());

            // Optionally update firstTask observable.
            if (!morningTasks.isEmpty()) {
                firstTask.setValue(morningTasks.get(0));
            }
                });
        taskRepository.findAll(InMemoryDataSource.EVENING_ROUTINE.getName()).observe(tasks -> {
            if (tasks == null) return;


            List<Task> eveningTasks = new ArrayList<>(tasks);
            this.eveningTasks.setValue(eveningTasks);

            if (!eveningTasks.isEmpty()) {
                firstTask.setValue(eveningTasks.get(0));
            }
        });
    }

    public Subject<List<Task>> getMorningTasks() {
        return morningTasks;
    }

    public Subject<List<Task>> getEveningTasks() {
        return eveningTasks;
    }

    public Subject<Integer> getEstimatedTime() {
        return estimatedTime;
    }

    /**
     * Adds a task to the current routine.
     */
    public void append(Task task) {
        taskRepository.save(getCurRoutine().getValue().getName(), task);
    }

    /**
     * Edits an existing task in the current routine.
     */
    public void edit(String oldName, String newName) {
        taskRepository.edit(getCurRoutine().getValue().getName(), oldName, newName);
    }

    /**
     * Removes a task from the current routine.
     */
    public void remove(String name) {
        Log.d("MainViewModel", "Task being removed: " + name);
        taskRepository.remove(getCurRoutine().getValue().getName(), name);
        curRoutine.setValue(getCurRoutine().getValue());
        Log.d("MainViewModel", "Number of Tasks: " + getCurRoutine().getValue().getNumTasks());
    }

    public static void switchRoutine(Routine routine) {
        curRoutine.setValue(routine);
    }

    public void startTime() {
        curRoutine.getValue().startRoutine();
        completed.setValue(false);
    }

    public Subject<Routine> getCurRoutine() {
        return curRoutine;
    }

    public void endRoutine() {
        curRoutine.getValue().endRoutine();
        completed.setValue(true);
    }

    public Subject<Boolean> getCompleted() {
        return completed;
    }
}
