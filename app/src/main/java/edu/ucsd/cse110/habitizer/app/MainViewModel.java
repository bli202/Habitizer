package edu.ucsd.cse110.habitizer.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.util.Log;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.List;

import java.util.Objects;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;
import edu.ucsd.cse110.observables.PlainMutableSubject;
import edu.ucsd.cse110.observables.Subject;

public class MainViewModel extends ViewModel {

    private static final String TAG = "MainViewModel";

    // Domain state (Model) and current routine context.
    private static TaskRepository taskRepository;
    private final RoutineRepository routineRepository;

    private final PlainMutableSubject<Integer> estimatedTime;

    private final PlainMutableSubject<Boolean> completed;
    private static final PlainMutableSubject<Routine> currentRoutine = new PlainMutableSubject<>();

    public static final ViewModelInitializer<MainViewModel> initializer = new ViewModelInitializer<>(
            MainViewModel.class,
            creationExtras -> {
                var app = (HabitizerApplication) creationExtras.get(APPLICATION_KEY);
                assert app != null;
                // Here we are initializing the view model for a specific routine.
                return new MainViewModel(app.getTaskRepository(), app.getRoutineRepository());
            });

    public MainViewModel(TaskRepository taskRepository, RoutineRepository routineRepository) {
        this.taskRepository = taskRepository;
        this.routineRepository = routineRepository;

        // Creating observable subjects.
        this.completed = new PlainMutableSubject<>(false);
        this.estimatedTime = new PlainMutableSubject<>();
    }

    @SuppressWarnings("unused")
    public Subject<Integer> getEstimatedTime() {
        return estimatedTime;
    }

    /**
     * Adds a task to the current routine.
     */
    public void addTaskToCurrentRoutine(Task task) {
        Objects.requireNonNull(currentRoutine.getValue()).addTask(task);
        taskRepository.save(Objects.requireNonNull(getCurrentRoutine().getValue()).getId(), task);
    }

    /**
     * Edits an existing task in the current routine.
     */
    public void editTaskInCurrentRoutine(String oldName, String newName) {
        Objects.requireNonNull(currentRoutine.getValue()).editTask(oldName, newName);
        taskRepository.edit(Objects.requireNonNull(getCurrentRoutine().getValue()).getId(), oldName, newName);
    }

    /**
     * Edit an existing routine name
     */
    public void editRoutineName(String newName) {
        Routine currentRoutine = getCurrentRoutine().getValue();
        assert currentRoutine != null;
        Log.d(TAG, "routine to edit: " + currentRoutine.getName());
        // Update in database
        routineRepository.editRoutineName(currentRoutine.getId(), newName);
        currentRoutine.setName(newName);
        MainViewModel.currentRoutine.setValue(currentRoutine);
    }

    /**
     * Removes a task from the current routine.
     */
    public void removeTaskByName(String name) {
        Log.d(TAG, "Task being removed: " + name);
        Objects.requireNonNull(currentRoutine.getValue()).removeTask(name);
        taskRepository.remove(Objects.requireNonNull(getCurrentRoutine().getValue()).getId(), name);
        Log.d(TAG, "Number of Tasks: " + getCurrentRoutine().getValue().getNumTasks());
    }

//    public void removeTaskById(int taskId) {
//        Log.d(TAG, "Task being removed: " + taskId);
//        Objects.requireNonNull(currentRoutine.getValue()).removeTask(taskId);
//        taskRepository.remove(Objects.requireNonNull(getCurrentRoutine().getValue()).getId(), taskId);
//        Log.d(TAG, "Number of Tasks: " + getCurrentRoutine().getValue().getNumTasks());
//    }

    public static void switchRoutine(Routine routine) {
        currentRoutine.setValue(routine);
    }

    public void startCurrentRoutine() {
        Objects.requireNonNull(currentRoutine.getValue()).startRoutine();
        completed.setValue(false);
    }

    public static Subject<Routine> getCurrentRoutine() {
        return currentRoutine;
    }

    public Subject<List<Task>> getCurrentRoutineTasks() {
        return taskRepository.findAllTasksForRoutine(Objects.requireNonNull(getCurrentRoutine().getValue()).getId());
    }

    public List<Routine> getRoutines() {
        return routineRepository.getRoutineList().getValue();
    }

    public Subject<List<Routine>> getRoutinesSubjects() {
        return routineRepository.getRoutineList();
    }

    public void endCurrentRoutine() {
        Objects.requireNonNull(currentRoutine.getValue()).endRoutine();
        completed.setValue(true);
    }

    public Subject<Boolean> getCompleted() {
        return completed;
    }

    public void addNewRoutine(Routine routine) {
        routineRepository.addRoutine(routine);
    }

    public void setCurrentRoutineEstimatedTime(int time) {
        routineRepository.setEstimatedTime(Objects.requireNonNull(currentRoutine.getValue()).getId(), time);
    }

    public void removeRoutine(Routine routine) {
        routineRepository.removeRoutine(routine);
    }

    public static void moveUp(int routineId, int order) {
        taskRepository.moveUp(routineId, order);
    }

    public static void moveDown(int routineId, int order) {
        taskRepository.moveDown(routineId, order);
    }
}