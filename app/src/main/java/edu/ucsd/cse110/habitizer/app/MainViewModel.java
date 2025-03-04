package edu.ucsd.cse110.habitizer.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.util.Log;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.observables.PlainMutableSubject;
import edu.ucsd.cse110.observables.Subject;

public class MainViewModel extends ViewModel {

    private static final String LOG_TAG = "MainViewModel";

    // Domain state (Model) and current routine context.
    private final RoutineRepository routineRepository;

    // UI state observables.
    private final Subject<List<Task>> morningTasks;
    private final Subject<List<Task>> eveningTasks;
    private final PlainMutableSubject<Integer> estimatedTime;
    private final PlainMutableSubject<Task> firstTask;
    private final PlainMutableSubject<Boolean> completed;
    private static PlainMutableSubject<Routine> curRoutine = null;

    public static final ViewModelInitializer<MainViewModel> initializer = new ViewModelInitializer<>(
            MainViewModel.class,
            creationExtras -> {
                var app = (HabitizerApplication) creationExtras.get(APPLICATION_KEY);
                assert app != null;
                return new MainViewModel(app.getRoutineRepository());
            }
    );


    public MainViewModel(RoutineRepository routineRepository) {
        this.routineRepository = routineRepository;
        Log.d(LOG_TAG, "MainViewModel constructor");

        this.firstTask = new PlainMutableSubject<>();
        this.completed = new PlainMutableSubject<>(false);
        this.estimatedTime = new PlainMutableSubject<>();



        // Observe routines and tasks
        this.eveningTasks = routineRepository.getTaskListForRoutine(getRoutineByName("Evening Routine"));

        Routine morningRoutine = getRoutineByName("Morning Routine");
        if (morningRoutine != null) {
            this.morningTasks = routineRepository.getTaskListForRoutine(morningRoutine);
        } else {
            Log.e(LOG_TAG, "Error: 'Morning Routine' not found in repository!");
            this.morningTasks = new PlainMutableSubject<>(List.of()); // Empty tasks list
        }

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

    public RoutineRepository getRoutineRepository() {
        return routineRepository;
    }

    /**
     * Adds a task to the current routine.
     */
    public void append(Task task) {
        Routine routine = getCurRoutine().getValue();
        if (routine != null) {
            routineRepository.addTaskToRoutine(routine, task);
        }
    }

    /**
     * Removes a task from the current routine.
     */
    public void remove(String name) {
        Log.d("MainViewModel", "Task being removed: " + name);
        Routine routine = getCurRoutine().getValue();
        if (routine != null) {
            routineRepository.removeTaskFromRoutine(routine, new Task(name));
            curRoutine.setValue(routine);
            Log.d("MainViewModel", "Number of Tasks: " + routine.getNumTasks());
        }
    }

    /**
     * Edits an existing routine.
     */
    public void editRoutine(String newName, int newEstimatedTime) {
        Routine routine = getCurRoutine().getValue();
        if (routine != null) {
            Log.d("MainViewModel", "Editing routine: " + routine.getName());

            // Update routine properties
            Routine updatedRoutine = new Routine(routine.getId(), newEstimatedTime, newName);
            updatedRoutine.setTasksDone(routine.getTasksDone());
            updatedRoutine.setOngoing(routine.getOngoing());
            updatedRoutine.setTimer(routine.getTimer());

            // Save to repository
            routineRepository.removeRoutine(routine);
            routineRepository.addRoutine(updatedRoutine);
            curRoutine.setValue(updatedRoutine);
        }
    }

    public void editTask(String oldName, String newName) {
        Routine routine = getCurRoutine().getValue();
        if (routine != null) {
            Log.d("MainViewModel", "Editing task: " + oldName + " -> " + newName);

            // Call repository to edit task
            routineRepository.editTask(routine.getId(), oldName, newName);

            // Refresh routine to reflect updated task name
            curRoutine.setValue(routine);
        }
    }


    /**
     * Switches the current routine.
     */
    public static void switchRoutine(Routine routine) {
        curRoutine.setValue(routine);
    }

    public void startTime() {
        Routine routine = getCurRoutine().getValue();
        if (routine != null) {
            routine.startRoutine();
            completed.setValue(false);
        }
    }

    public Subject<Routine> getCurRoutine() {
        return curRoutine;
    }

    public void endRoutine() {
        Routine routine = getCurRoutine().getValue();
        if (routine != null) {
            routine.endRoutine();
            completed.setValue(true);
        }
    }

    public Subject<Boolean> getCompleted() {
        return completed;
    }

    /**
     * Helper method to get a routine by name.
     */
    private Routine getRoutineByName(String name) {
        List<Routine> routines = routineRepository.getRoutineList().getValue();

        if (routines == null || routines.isEmpty()) {
            Log.e("MainViewModel", "Routine list is NULL or EMPTY!");
            return null; // Prevent crash
        }

        for (Routine routine : routines) {
            if (routine.getName().equalsIgnoreCase(name)) {
                return routine;
            }
        }

        Log.e("MainViewModel", "Routine '" + name + "' not found in repository!");
        return null;
    }
}
