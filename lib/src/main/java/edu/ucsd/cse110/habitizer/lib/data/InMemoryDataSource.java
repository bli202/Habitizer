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
    // Mapping from routine id to Routine object.
    private final Map<Integer, Routine> routines = new HashMap<>();

    // Global mapping from task name to Task object.
    private final Map<String, Task> tasks = new HashMap<>();

    // Mapping from routine id to the list of tasks belonging to that routine.
    private final Map<Integer, List<Task>> routineTasks = new HashMap<>();

    // Observable subjects for individual routines.
    private final Map<Integer, PlainMutableSubject<Routine>> routineSubjects = new HashMap<>();
    // Observable subject for the list of all routines.
    private final PlainMutableSubject<List<Routine>> allRoutinesSubject = new PlainMutableSubject<>();

    // Observable subjects for individual tasks.
    private final Map<String, PlainMutableSubject<Task>> taskSubjects = new HashMap<>();
    // Observable subjects for the list of tasks in a specific routine.
    private final Map<Integer, PlainMutableSubject<List<Task>>> routineTasksSubjects = new HashMap<>();
    

    public InMemoryDataSource() {
        allRoutinesSubject.setValue(new ArrayList<>());
    }

    // =================== Routine CRUD ===================

    /**
     * Returns a copy of all routines.
     */
    public List<Routine> getRoutines() {
        return new ArrayList<>(routines.values());
    }

    /**
     * Returns the Routine object with the given name.
     */
    public Routine getRoutine(int routineId) {
        return routines.get(routineId);
    }

    /**
     * Returns an observable subject for a specific routine.
     */
    public Subject<Routine> getRoutineSubject(int routineId) {
        if (!routineSubjects.containsKey(routineId)) {
            PlainMutableSubject<Routine> subject = new PlainMutableSubject<>();
            subject.setValue(getRoutine(routineId));
            routineSubjects.put(routineId, subject);
        }
        return routineSubjects.get(routineId);
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
        int routineId = routine.getId();
        routines.put(routineId, routine);
        if (!routineTasks.containsKey(routineId)) {
            routineTasks.put(routineId, new ArrayList<>());
        }
        if (routineSubjects.containsKey(routineId)) {
            routineSubjects.get(routineId).setValue(routine);
        }
        allRoutinesSubject.setValue(getRoutines());
    }

    /**
     * Removes a routine and all its associated tasks.
     */
    public void removeRoutine(int routineId) {
        routines.remove(routineId);
        // Remove all tasks associated with this routine.
        List<Task> removedTasks = routineTasks.remove(routineId);
        if (removedTasks != null) {
            for (Task task : removedTasks) {
                tasks.remove(task.getName());
                taskSubjects.remove(task.getName());
            }
        }
        routineSubjects.remove(routineId);
        routineTasksSubjects.remove(routineId);
        allRoutinesSubject.setValue(getRoutines());
    }

    /**
     * Edits a routine by replacing the old routine with the new one.
     * (You could also update properties directly on the Routine.)
     */
    public void editRoutine(int oldRoutineId, Routine newRoutine) {
        removeRoutine(oldRoutineId);
        putRoutine(newRoutine);
    }

    // =================== Task CRUD within a Routine ===================

    /**
     * Returns a copy of the tasks in the specified routine.
     */
    public List<Task> getTasksForRoutine(int routineId) {
        List<Task> tasksList = routineTasks.get(routineId);
        if (tasksList == null) {
            tasksList = new ArrayList<>();
            routineTasks.put(routineId, tasksList);
        }
        return new ArrayList<>(tasksList);
    }

    /**
     * Returns an observable subject for the list of tasks for a routine.
     */
    public Subject<List<Task>> getRoutineTasksSubject(int routineId) {
        if (!routineTasksSubjects.containsKey(routineId)) {
            PlainMutableSubject<List<Task>> subject = new PlainMutableSubject<>();
            subject.setValue(getTasksForRoutine(routineId));
            routineTasksSubjects.put(routineId, subject);
        }
        return routineTasksSubjects.get(routineId);
    }

    /**
     * Inserts or updates a task within a specific routine.
     * Updates both the global tasks mapping and the routine's task list.
     */
    public void putTask(int routineId, Task task) {
        // Update global mapping.
        tasks.put(task.getName(), task);
        // Update the routine's task list.
        List<Task> tasksList = routineTasks.get(routineId);
        Routine routine = routines.get(routineId);
        routine.addTask(task);
        if (tasksList == null) {
            tasksList = new ArrayList<>();
            routineTasks.put(routineId, tasksList);
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
        if (routineTasksSubjects.containsKey(routineId)) {
            routineTasksSubjects.get(routineId).setValue(getTasksForRoutine(routineId));
        }
    }

    /**
     * Removes a task from a specific routine.
     */
    public void removeTask(int routineId, String taskName) {
        // Remove from global mapping.
        List<Task> tasksList = routineTasks.get(routineId);
        Routine routine = routines.get(routineId);
        routine.removeTask(tasks.get(taskName));

        tasks.remove(taskName);
        // Remove from the routine's task list.

        if (tasksList != null) {
            tasksList.removeIf(t -> t.getName().equals(taskName));
        }
        // Update observable subject.
        if (routineTasksSubjects.containsKey(routineId)) {
            routineTasksSubjects.get(routineId).setValue(getTasksForRoutine(routineId));
        }
        // Remove individual task observable if present.
        taskSubjects.remove(taskName);
    }

    /**
     * Edits a task within a routine by changing its name.
     * Removes the old task and inserts a new one with the updated name.
     */
    /**
     * Edits a task within a routine by delegating to the Routine's own editTask method.
     * Updates the global task mapping and observable subjects accordingly.
     *
     * @param routineId the id of the routine that contains the task.
     * @param oldTaskName the current name of the task.
     * @param newTaskName the new name to set for the task.
     */
    public void editTask(int routineId, String oldTaskName, String newTaskName) {
        // Retrieve the routine object from the routines mapping.
        Routine routine = routines.get(routineId);
        if (routine == null) {
            // Optionally handle the error (e.g., routine not found)
            return;
        }

        // Get the list of tasks for this routine.
        List<Task> tasksList = routineTasks.get(routineId);
        if (tasksList == null) {
            // No tasks for this routine; nothing to edit.
            return;
        }

        // Find the task that matches the old task name.
        Task taskToEdit = null;
        for (Task t : tasksList) {
            if (t.getName().equals(oldTaskName)) {
                taskToEdit = t;
                break;
            }
        }
        if (taskToEdit == null) {
            // Task not found in the routine.
            return;
        }

        // Delegate the editing to the Routine object's own method.
        // This method will handle validation (e.g., duplicates, routine status).
        boolean edited = routine.editTask(taskToEdit, newTaskName);
        if (!edited) {
            // Editing failed (e.g., routine is ongoing); handle as needed.
            return;
        }

        // Update the global tasks mapping: remove the old entry and add the updated task.
        tasks.remove(oldTaskName);
        tasks.put(newTaskName, taskToEdit);

        // Update the observable subject for the routine's tasks.
        if (routineTasksSubjects.containsKey(routineId)) {
            routineTasksSubjects.get(routineId).setValue(getTasksForRoutine(routineId));
        }

        // Update the individual task observable: if a subject exists for the old task name,
        // update its key and value.
        PlainMutableSubject<Task> subject = taskSubjects.remove(oldTaskName);
        if (subject != null) {
            subject.setValue(taskToEdit);
            taskSubjects.put(newTaskName, subject);
        }
    }
    
    public void setEstimatedTime(int routineId, int time) {
        routines.get(routineId).setEstimatedTime(time);
    }


    // =================== Default Data Initialization ===================

    /**
     * Default routine and task for bootstrapping.
     */
    public static final Routine MORNING_ROUTINE = new Routine(30, "Morning Routine");
    public static final Task SHOWER = new Task("Shower", MORNING_ROUTINE.getNumTasks());
    public static final Task BRUSH_TEETH = new Task("Brush Teeth", MORNING_ROUTINE.getNumTasks());
    public static final Task DRESS = new Task("Dress", MORNING_ROUTINE.getNumTasks());
    public static final Task MAKE_COFFEE = new Task("Make Coffee", MORNING_ROUTINE.getNumTasks());
    public static final Task MAKE_LUNCH = new Task("Make Lunch", MORNING_ROUTINE.getNumTasks());
    public static final Task DINNER_PREP = new Task("Dinner Prep", MORNING_ROUTINE.getNumTasks());
    public static final Task PACK_BAG = new Task("Pack Bag", MORNING_ROUTINE.getNumTasks());
    public static final Routine EVENING_ROUTINE = new Routine(45, "Evening Routine");
    public static final Task CHARGE_DEVICES = new Task("Charge Devices", EVENING_ROUTINE.getNumTasks());
    public static final Task PREPARE_DINNER = new Task("Prepare Dinner", EVENING_ROUTINE.getNumTasks());
    public static final Task EAT_DINNER = new Task("Eat Dinner", EVENING_ROUTINE.getNumTasks());
    public static final Task WASH_DISHES = new Task("Wash Dishes", EVENING_ROUTINE.getNumTasks());
    public static final Task PACK_BAG_EVENING = new Task("Pack Bag", EVENING_ROUTINE.getNumTasks());

    /**
     * Factory method to create a data source preloaded with default routines and tasks.
     */
    public static InMemoryDataSource fromDefault() {
        InMemoryDataSource data = new InMemoryDataSource();
        // Add default routines.
        data.putRoutine(MORNING_ROUTINE);
        data.putRoutine(EVENING_ROUTINE);


        // Add default tasks to the default routines.
        data.putTask(MORNING_ROUTINE.getId(), SHOWER);
        data.putTask(MORNING_ROUTINE.getId(), BRUSH_TEETH);
        data.putTask(MORNING_ROUTINE.getId(), DRESS);
        data.putTask(MORNING_ROUTINE.getId(), MAKE_COFFEE);
        data.putTask(MORNING_ROUTINE.getId(), MAKE_LUNCH);
        data.putTask(MORNING_ROUTINE.getId(), DINNER_PREP);
        data.putTask(MORNING_ROUTINE.getId(), PACK_BAG);
        data.putTask(EVENING_ROUTINE.getId(), CHARGE_DEVICES);
        data.putTask(EVENING_ROUTINE.getId(), PREPARE_DINNER);
        data.putTask(EVENING_ROUTINE.getId(), EAT_DINNER);
        data.putTask(EVENING_ROUTINE.getId(), WASH_DISHES);
        data.putTask(EVENING_ROUTINE.getId(), PACK_BAG_EVENING);

        return data;
    }
    
    public void removeTaskById(int routineId, int taskId) {
    
    }
}
