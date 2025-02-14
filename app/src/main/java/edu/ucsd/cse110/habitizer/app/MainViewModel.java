package edu.ucsd.cse110.habitizer.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.util.Log;

import androidx.lifecycle.viewmodel.ViewModelInitializer;
import androidx.lifecycle.ViewModel;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;
import edu.ucsd.cse110.observables.PlainMutableSubject;
import edu.ucsd.cse110.observables.Subject;

public class MainViewModel extends ViewModel {

    private static final String LOG_TAG = "MainViewModel";

    // Domain state (true "Model" state)
    private final TaskRepository taskRepository;

    // UI state
    private final PlainMutableSubject<List<Task>> orderedTasks;
    private final Subject<Task> firstTask;
    private final Subject<Boolean> completed;
    private final Subject<String> taskName;

    // UI state


    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (HabitizerApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getTaskRepository());
                    });


    public MainViewModel(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        Log.d("MainViewModel", "MainViewModel constructor");

        this.orderedTasks = new PlainMutableSubject<>();
        this.firstTask = new PlainMutableSubject<>();
        this.completed = new PlainMutableSubject<>();
        this.taskName = new PlainMutableSubject<>();

        taskRepository.findAll().observe(tasks -> {
            if (tasks == null) return; // not ready yet, ignore

            var newOrderedTasks = tasks.stream()
                    .collect(Collectors.toList());

            orderedTasks.setValue(newOrderedTasks);
        });


    }

    public Subject<List<Task>> getOrderedTasks() {
        return orderedTasks;
    }

    public void append(Task task) {
        taskRepository.save(task);
    }

    public void edit(String taskname, String name) { taskRepository.edit(taskname, name);}

}
