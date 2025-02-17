package edu.ucsd.cse110.habitizer.lib.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.observables.PlainMutableSubject;
import edu.ucsd.cse110.observables.Subject;

public class InMemoryDataSource {
    // Mapping from routine name to Routine object.
    private final Map<String, Routine> routines = new HashMap<>();

    // Global mapping from task name to Task object.
    private final Map<String, Task> tasks = new HashMap<>();

    // Mapping from routine name to the list of tasks belonging to that routine.
    private final Map<String, List<Task>> routineTasks = new HashMap<>();

    // Observable subjects for individual routines.
    private final Map<String, PlainMutableSubject<Routine>> routineSubjects = new HashMap<>();
    // Observable subject for the list of all routines.
    private final PlainMutableSubject<List<Routine>> allRoutinesSubject = new PlainMutableSubject<>();

    // Observable subjects for individual tasks.
    private final Map<String, PlainMutableSubject<Task>> taskSubjects = new HashMap<>();
    // Observable subjects for the list of tasks in a specific routine.
    private final Map<String, PlainMutableSubject<List<Task>>> routineTasksSubjects = new HashMap<>();

    public InMemoryDataSource() {
        allRoutinesSubject.setValue(new ArrayList<>());
    }

    /**
     * Returns a copy of all routines.
     */
    public List<Routine> getRoutines() {
        return new ArrayList<>(routines.values());
    }

    /**
     * Returns the Routine object with the given name.
     */
    public Routine getRoutine(String routineName) {
        return routines.get(routineName);
    }

    /**
     * Returns an observable subject for a specific routine.
     */
    public Subject<Routine> getRoutineSubject(String routineName) {
        if (!routineSubjects.containsKey(routineName)) {
            PlainMutableSubject<Routine> subject = new PlainMutableSubject<>();
            subject.setValue(getRoutine(routineName));
            routineSubjects.put(routineName, subject);
        }
        return routineSubjects.get(routineName);
    }

    /**
     * Returns an observable subject for all routines.
     */
    public Subject<List<Routine>> getAllRoutinesSubject() {
        return allRoutinesSubject;
    }

    public Subject<Task> getTaskSubject(String taskName) {
        return taskSubjects.get(taskName);
    }
    /**
     * Inserts or updates a routine.
     * Initializes its associated tasks list if not already present.
     */
    public void putRoutine(Routine routine) {
        String routineName = routine.getTitle();
        routines.put(routineName, routine);
        if (!routineTasks.containsKey(routineName)) {
            routineTasks.put(routineName, new ArrayList<>());
        }
        if (routineSubjects.containsKey(routineName)) {
            routineSubjects.get(routineName).setValue(routine);
        }
        allRoutinesSubject.setValue(getRoutines());
    }

    /**
     * Returns a copy of the tasks in the specified routine.
     */
    public List<Task> getTasksForRoutine(String routineName) {
        List<Task> tasksList = routineTasks.get(routineName);
        if (tasksList == null) {
            tasksList = new ArrayList<>();
            routineTasks.put(routineName, tasksList);
        }
        return new ArrayList<>(tasksList);
    }

    /**
     * Returns an observable subject for the list of tasks for a routine.
     */
    public Subject<List<Task>> getRoutineTasksSubject(String routineName) {
        if (!routineTasksSubjects.containsKey(routineName)) {
            PlainMutableSubject<List<Task>> subject = new PlainMutableSubject<>();
            subject.setValue(getTasksForRoutine(routineName));
            routineTasksSubjects.put(routineName, subject);
        }
        return routineTasksSubjects.get(routineName);
    }

    /**
     * Inserts or updates a task within a specific routine.
     * Updates both the global tasks mapping and the routine's task list.
     */
    public void putTask(String routineName, Task task) {

        // Update global mapping.
        tasks.put(task.getName(), task);
        // Update the routine's task list.
        List<Task> tasksList = routineTasks.get(routineName);
        Routine routine = routines.get(routineName);
        routine.addTask(task);
        if (tasksList == null) {
            tasksList = new ArrayList<>();
            routineTasks.put(routineName, tasksList);
        }
        boolean updated = false;
        for (int i = 0; i < tasksList.size(); i++) {
            if (tasksList.get(i).getName().equals(task.getName())) {
                tasksList.set(i, task);
                updated = true;
                break;
            }
        }
        if (!updated) {
            tasksList.add(task);
        }

        // Update observable subject for this routine's tasks.
        if (routineTasksSubjects.containsKey(routineName)) {
            routineTasksSubjects.get(routineName).setValue(getTasksForRoutine(routineName));
        }
    }

    /**
     * Removes a task from a specific routine.
     */
    public void removeTask(String routineName, String taskName) {
        List<Task> tasksList = routineTasks.get(routineName);
        Routine routine = routines.get(routineName);
        routine.removeTask(tasks.get(taskName));

        tasks.remove(taskName);

        if (tasksList != null) {
            tasksList.removeIf(t -> t.getName().equals(taskName));
        }

        // Update observable subject.
        if (routineTasksSubjects.containsKey(routineName)) {
            routineTasksSubjects.get(routineName).setValue(getTasksForRoutine(routineName));
        }
        // Remove individual task observable if present.
        taskSubjects.remove(taskName);
    }

    /**
     * Edits a task within a routine by delegating to the Routine's own editTask method.
     * Updates the global task mapping and observable subjects accordingly.
     *
     * @param routineName the name of the routine that contains the task.
     * @param oldTaskName the current name of the task.
     * @param newTaskName the new name to set for the task.
     */
    public void editTask(String routineName, String oldTaskName, String newTaskName) {
        Routine routine = routines.get(routineName);
        if (routine == null) {
            return;
        }
        List<Task> tasksList = routineTasks.get(routineName);
        if (tasksList == null) {
            return;
        }

        Task taskToEdit = null;
        for (Task t : tasksList) {
            if (t.getName().equals(oldTaskName)) {
                taskToEdit = t;
                break;
            }
        }
        if (taskToEdit == null) {
            return;
        }

        boolean edited = routine.editTask(taskToEdit, newTaskName);
        if (!edited) {
            return;
        }

        tasks.remove(oldTaskName);
        tasks.put(newTaskName, taskToEdit);

        // Update the observable subject for the routine's tasks.
        if (routineTasksSubjects.containsKey(routineName)) {
            routineTasksSubjects.get(routineName).setValue(getTasksForRoutine(routineName));
        }

        // Update the individual task observable:
        PlainMutableSubject<Task> subject = taskSubjects.remove(oldTaskName);
        if (subject != null) {
            subject.setValue(taskToEdit);
            taskSubjects.put(newTaskName, subject);
        }
    }


    // =================== Default Data Initialization ===================

    /**
     * Default routine and task for bootstrapping.
     */
    public static final Routine MORNING_ROUTINE = new Routine(30, "Morning Routine");
    public static final Task BRUSH_TEETH = new Task("Brush teeth");
    public static final Routine EVENING_ROUTINE = new Routine(45, "Evening Routine");
    public static final Task WORK_OUT = new Task("Work out");

    /**
     * Factory method to create a data source with default routines and tasks.
     */
    public static InMemoryDataSource fromDefault() {
        InMemoryDataSource data = new InMemoryDataSource();
        // Add default routine.
        data.putRoutine(MORNING_ROUTINE);
        data.putRoutine(EVENING_ROUTINE);
        // Add default task to the default routine.

        data.putTask(MORNING_ROUTINE.getTitle(), BRUSH_TEETH);
        data.putTask(EVENING_ROUTINE.getTitle(), WORK_OUT);

        return data;
    }
}
