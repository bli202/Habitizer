package edu.ucsd.cse110.habitizer.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.util.Log;

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
    private final PlainMutableSubject<List<Task>> orderedTasks;
    private final PlainMutableSubject<Task> firstTask;
    private final PlainMutableSubject<Boolean> completed;
    private final PlainMutableSubject<String> taskName;

    private static final PlainMutableSubject<Routine> curRoutine = new PlainMutableSubject<>(InMemoryDataSource.DEFAULT_ROUTINE_MORNING);

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

        this.orderedTasks = new PlainMutableSubject<>();
        this.firstTask = new PlainMutableSubject<>();
        this.completed = new PlainMutableSubject<>(false);
        this.taskName = new PlainMutableSubject<>();

        // Observe tasks for the specified routine.
        taskRepository.findAll(curRoutine.getValue().getName()).observe(tasks -> {
            if (tasks == null) return; // Not ready yet, ignore.

            // Create a new ordered list (you can add a Comparator if needed).
            List<Task> newOrderedTasks = new ArrayList<>(tasks);
            orderedTasks.setValue(newOrderedTasks);

            // Optionally update firstTask observable.
            if (!newOrderedTasks.isEmpty()) {
                firstTask.setValue(newOrderedTasks.get(0));
            }
        });
    }

    public Subject<List<Task>> getOrderedTasks() {
        return orderedTasks;
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
        taskRepository.remove(getCurRoutine().getValue().getName(), name);
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
