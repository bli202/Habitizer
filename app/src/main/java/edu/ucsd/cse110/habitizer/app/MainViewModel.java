package edu.ucsd.cse110.habitizer.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.util.Log;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.ArrayList;
import java.util.List;

import java.util.Objects;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;
import edu.ucsd.cse110.observables.PlainMutableSubject;
import edu.ucsd.cse110.observables.Subject;

public class MainViewModel extends ViewModel {

    private static final String LOG_TAG = "MainViewModel";

    // Domain state (Model) and current routine context.
    private final TaskRepository taskRepository;
    private final RoutineRepository routineRepository;
    
    private final PlainMutableSubject<List<Task>> currTaskList;
    private final PlainMutableSubject<Integer> estimatedTime;

    private final PlainMutableSubject<Task> firstTask;
    private final PlainMutableSubject<Boolean> completed;
    private static final PlainMutableSubject<Routine> curRoutine = new PlainMutableSubject<>(InMemoryDataSource.MORNING_ROUTINE);

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

        Log.d(LOG_TAG, "MainViewModel constructor");

        // Creating observable subjects.
        this.firstTask = new PlainMutableSubject<>();
        this.completed = new PlainMutableSubject<>(false);
        this.estimatedTime = new PlainMutableSubject<>();
        this.currTaskList = new PlainMutableSubject<>(Objects.requireNonNull(curRoutine.getValue()).getTaskList());

        // Observe tasks for the specified routine.
        taskRepository.findAll(Objects.requireNonNull(getCurRoutine().getValue()).getId()).observe(tasks -> {
            if (tasks == null) return;

            var curRoutineTasks = tasks.stream()
                    .toList();

            if (!curRoutineTasks.isEmpty()) {
                this.currTaskList.setValue(curRoutineTasks);
            }
        });
    }

    @SuppressWarnings("unused")
    public Subject<Integer> getEstimatedTime() {
        return estimatedTime;
    }

    /**
     * Adds a task to the current routine.
     */
    public void append(Task task) {
        taskRepository.save(Objects.requireNonNull(getCurRoutine().getValue()).getId(), task);
    }

    /**
     * Edits an existing task in the current routine.
     */
    public void edit(String oldName, String newName) {
        taskRepository.edit(Objects.requireNonNull(getCurRoutine().getValue()).getId(), oldName, newName);
    }

    /**
     * Removes a task from the current routine.
     */
    public void remove(String name) {
        Log.d("MainViewModel", "Task being removed: " + name);
        taskRepository.remove(Objects.requireNonNull(getCurRoutine().getValue()).getId(), name);
        Log.d("MainViewModel", "Number of Tasks: " + getCurRoutine().getValue().getNumTasks());
    }

    public static void switchRoutine(Routine routine) {
        curRoutine.setValue(routine);
    }

    public void startTime() {
        Objects.requireNonNull(curRoutine.getValue()).startRoutine();
        completed.setValue(false);
    }

    public static Subject<Routine> getCurRoutine() {
        return curRoutine;
    }

    public Subject<List<Task>> getCurTasks() {
        return taskRepository.findAll(Objects.requireNonNull(curRoutine.getValue()).getId());
    }
    
    public List<Routine> getRoutines() {
        return routineRepository.getRoutineList().getValue();
    }

    public void endCurRoutine() {
        Objects.requireNonNull(curRoutine.getValue()).endRoutine();
        completed.setValue(true);
    }

    public Subject<Boolean> getCompleted() {
        return completed;
    }
    
    public void putRoutine(Routine routine) {
        routineRepository.addRoutine(routine);
    }
    
    public void setCurRoutineEstimatedTime(int time) {
        routineRepository.setEstimatedTime(Objects.requireNonNull(curRoutine.getValue()).getId(), time);
    }
}
