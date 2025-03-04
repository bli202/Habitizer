//package edu.ucsd.cse110.habitizer.lib.data;
//
//import static org.junit.Assert.*;
//
//import org.junit.Test;
//
//import edu.ucsd.cse110.habitizer.lib.domain.Routine;
//import edu.ucsd.cse110.habitizer.lib.domain.Task;
//
//public class InMemoryDataSourceTest {
//
//    public InMemoryDataSource setup() {
//        InMemoryDataSource data = new InMemoryDataSource();
//        final Routine MORNING_ROUTINE = new Routine(30, "Morning Routine");
//        final Task BRUSH_TEETH = new Task("Brush teeth");
//        final Routine EVENING_ROUTINE = new Routine(45, "Evening Routine");
//        final Task WORK_OUT = new Task("Work out");
//        data.putRoutine(MORNING_ROUTINE);
//        data.putRoutine(EVENING_ROUTINE);
//
//        data.putTask(MORNING_ROUTINE.getTitle(), BRUSH_TEETH);
//        data.putTask(EVENING_ROUTINE.getTitle(), WORK_OUT);
//        return data;
//    }
//
//    @Test
//    public void getRoutines() {
//        var data = setup();
//        var routines = data.getRoutines();
//        assertEquals(2, routines.size());
//        assertEquals("Morning Routine", routines.get(1).getName());
//        assertEquals("Evening Routine", routines.get(0).getName());
//    }
//
//    @Test
//    public void getRoutine() {
//        var data = setup();
//        var routine = data.getRoutine("Morning Routine");
//        assertEquals("Morning Routine", routine.getName());
//        assertEquals(30, routine.getDuration());
//        assertEquals("Brush teeth", routine.getTaskList().get(0).getName());
//    }
//
//    @Test
//    public void getRoutineSubject() {
//        var data = setup();
//        var routineSubject = data.getRoutineSubject("Evening Routine");
//        var value = routineSubject.getValue();
//        assertEquals("Evening Routine", value.getName());
//        assertEquals(45, value.getDuration());
//        assertEquals("Work out", value.getTaskList().get(0).getName());
//    }
//
//    @Test
//    public void putRoutine() {
//        InMemoryDataSource newData = new InMemoryDataSource();
//        assertEquals(0, newData.getRoutines().size());
//        Routine newRoutine = new Routine(15, "test routine");
//        newData.putRoutine(newRoutine);
//        assertEquals(1, newData.getRoutines().size());
//        assertEquals(newRoutine, newData.getRoutines().get(0));
//    }
//
//    @Test
//    public void removeRoutine() {
//        var data = setup();
//        data.removeRoutine("Morning Routine");
//        assertEquals(1, data.getRoutines().size());
//        assertEquals("Evening Routine", data.getRoutines().get(0).getName());
//    }
//
//    @Test
//    public void getTasksForRoutine() {
//        var data = setup();
//        var tasks = data.getTasksForRoutine("Morning Routine");
//        assertEquals(1, tasks.size());
//        assertEquals("Brush teeth", tasks.get(0).getName());
//    }
//
//    @Test
//    public void getRoutineTasksSubject() {
//        var data = setup();
//        var tasksSubject = data.getRoutineTasksSubject("Evening Routine");
//        var tasks = tasksSubject.getValue();
//        assertEquals(1, tasks.size());
//        assertEquals("Work out", tasks.get(0).getName());
//    }
//
//    @Test
//    public void putTask() {
//        var data = setup();
//        Task newTask = new Task("new task");
//        data.putTask("Morning Routine", newTask);
//        var routine = data.getRoutine("Morning Routine");
//        assertEquals(2, routine.getTaskList().size());
//        assertEquals(newTask, routine.getTaskList().get(1));
//    }
//
//    @Test
//    public void removeTask() {
//        var data = setup();
//        data.removeTask("Evening Routine", "Work out");
//        var routine = data.getRoutine("Evening Routine");
//        assertEquals(0, routine.getTaskList().size());
//    }
//
//    @Test
//    public void editTask() {
//        var data = setup();
//        data.editTask("Morning Routine", "Brush teeth", "Don't brush teeth");
//        var routine = data.getRoutine("Morning Routine");
//        var task = data.getTasksForRoutine("Morning Routine").get(0);
//        assertEquals(1, routine.getTaskList().size());
//        assertEquals("Don't brush teeth", task.getName());
//    }
//}