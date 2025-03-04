//package edu.ucsd.cse110.habitizer.lib.domain;
//import static org.junit.Assert.*;
//
//import org.junit.Test;
//
//import java.util.*;
//
//public class RoutineTest {
//    int duration = 45;
//    int duration_2 = 60;
//    int duration_3 = 120;
//
//    private final Routine morning = new Routine(duration, "Morning");
//    private final Routine night = new Routine(duration_2, "Night");
//    private final Routine school = new Routine(duration_3, "School");
//
//    /**
//     * Routine is properly initialized with duration and name
//     */
//    @Test
//    public void testRoutineInitialState() {
//        var morning = new Routine(duration, "Morning");
//        var expectedDuration = duration;
//        var actDuration = morning.getEstimatedTime();
//        assertFalse(morning.getOngoing());
//        assertEquals(0, morning.getElapsedTime());
//        assertEquals(expectedDuration, actDuration);
//        assertEquals("Morning", morning.getName());
//        assertEquals(0, morning.getTasksDone());
//
//    }
//
//    /**
//     * Tasks are added properly
//     */
//    @Test
//    public void testAddTask() {
//
//        var morning = new Routine(duration, "Morning");
//        final Task shower = new Task("shower");
//        final Task brush = new Task("brush");
//        final Task lunch = new Task("lunch");
//
//        morning.addTask(shower);
//        morning.addTask(brush);
//        morning.addTask(lunch);
//
//        assertEquals(morning.getTaskList().size(), 3);
//        assertEquals(morning.getTaskList().get(0).getName(), "shower");
//        assertEquals(morning.getTaskList().get(1).getName(), "brush");
//        assertEquals(morning.getTaskList().get(2).getName(), "lunch");
//
//    }
//
//    /**
//     * Attempting to add a task with the same name as an existing task
//     * throws an error
//     */
//    @Test
//    public void testAddTaskDup() {
//
//        var morning = new Routine(duration, "Morning");
//        final Task shower = new Task("shower");
//        final Task dupShower = new Task("shower");
//        String expectedErrMsg = "Cannot have two tasks with the same name in Routine";
//        morning.addTask(shower);
//
//        try {
//            morning.addTask(dupShower);
//            fail("Expected exception thrown");
//
//        } catch (IllegalArgumentException e) {
//
//            assertEquals(expectedErrMsg, e.getMessage());
//        }
//    }
//
//    /**
//     * Cannot add task while routine is ongoing
//     */
//    @Test
//    public void testAddTaskOngoingRoutine() {
//
//        final Task shower = new Task("shower");
//        final Task brush = new Task("brush");
//        morning.addTask(shower);
//        morning.startRoutine();
//        assertFalse(morning.addTask(brush));
//    }
//
//    /**
//     * Cannot remove task while routine is ongoing
//     */
//    @Test
//    public void testRemoveTaskOngoing() {
//
//        final Task shower = new Task("shower");
//        morning.addTask(shower);
//        morning.startRoutine();
//        assertFalse(morning.removeTask(shower));
//    }
//
//    /**
//     * Properly remove tasks (while routine not ongoing)
//     */
//    @Test
//    public void testRemoveTask() {
//
//      final Task lunch = new Task("lunch");
//      night.addTask(lunch);
//      assertEquals(night.getTaskList().size(), 1);
//      night.removeTask(lunch);
//      assertEquals(night.getTaskList().size(), 0);
//    }
//
//    /**
//     * Properly check off task
//     */
//    @Test
//    public void testCheckOffTask() {
//
//      final Task dinner = new Task("dinner");
//      final Task lunch = new Task("lunch");
//
//      school.addTask(dinner);
//      school.addTask(lunch);
//      school.startRoutine();
//      school.checkOffTask(dinner);
//      assertTrue(dinner.isCompleted());
//      assertFalse(lunch.isCompleted());
//    }
//
//    /**
//     * Task times are correct after checking off
//     */
//    @Test
//    public void testTaskTimer() throws InterruptedException {
//        final Task lunch = new Task("lunch");
//        final Task dinner = new Task("dinner");
//        final Task snack = new Task("snack");
//
//        school.addTask(dinner);
//        school.addTask(lunch);
//        school.addTask(snack);
//
//        // Testing two task timers
//        school.startRoutine();
//        Thread.sleep(1000);
//        school.checkOffTask(dinner);
//        assertEquals(1, dinner.getTimeSpent());
//        Thread.sleep(3400);
//        school.checkOffTask(lunch);
//        assertEquals(3, lunch.getTimeSpent());
//    }
//
//    /**
//     * Cannot start an empty routine
//     */
//    @Test
//    public void testStartRoutineNoTask() {
//
//        try {
//            morning.startRoutine();
//            fail("Expected IllegalArgumentException thrown");
//        } catch (IllegalArgumentException e){
//            assertEquals( "Cannot start a routine with no tasks", e.getMessage());
//        }
//    }
//
//    /**
//     * Routine should have no tasks done and have no elapsed time right after starting
//     */
//    @Test
//    public void testStartRoutineState() {
//
//        final Task lunch = new Task("lunch");
//        morning.addTask(lunch);
//        morning.startRoutine();
//
//        assertEquals(0, morning.getTasksDone());
//        assertEquals(0, morning.getElapsedTime());
//        assertTrue(morning.getOngoing());
//    }
//
//    /**
//     * Properly retrieve task list from routine
//     */
//    @Test
//    public void testGetTaskList() {
//        final Task lunch = new Task("lunch");
//        final Task dinner = new Task("dinner");
//        final Task read = new Task("Read");
//
//        school.addTask(lunch);
//        school.addTask(dinner);
//        school.addTask(read);
//
//        List<Task> actualAns = school.getTaskList();
//        List<Task> expectedAns = new ArrayList<>();
//
//        expectedAns.add(lunch);
//        expectedAns.add(dinner);
//        expectedAns.add(read);
//
//        assertEquals(expectedAns, actualAns);
//    }
//
//    /**
//     * Ongoing boolean is properly updated when routine is
//     * started (true) and manually or automatically ended (false)
//     */
//    @Test
//    public void testIsOnGoing() {
//
//        final Task brush = new Task("brush");
//        final Task eat = new Task("eat");
//
//        assertFalse(morning.getOngoing());
//        morning.addTask(brush);
//        morning.addTask(eat);
//        morning.startRoutine();
//        assertTrue(morning.getOngoing());
//        morning.checkOffTask(brush);
//        assertTrue(morning.getOngoing());
//        morning.endRoutine();
//        assertFalse(morning.getOngoing());
//
//        school.addTask(eat);
//        school.startRoutine();
//        school.checkOffTask(eat);
//        assertFalse(school.getOngoing());
//
//    }
//
//    /**
//     * Number of tasks properly updates with adding and removing tasks;
//     * number of completed tasks properly updates when tasks are checked off
//     */
//    @Test
//    public void testGetTasksDoneGetNumTask() {
//
//        final Task brush = new Task("brush");
//        final Task eat = new Task("eat");
//        final Task shower = new Task("shower");
//
//        morning.addTask(brush);
//        morning.addTask(eat);
//        morning.addTask(shower);
//
//        assertEquals(3, morning.getNumTasks());
//        morning.removeTask(shower);
//        assertEquals(2, morning.getNumTasks());
//
//        morning.startRoutine();
//        morning.checkOffTask(brush);
//        assertEquals(1, morning.getTasksDone());
//        morning.checkOffTask(eat);
//        assertEquals(2, morning.getTasksDone());
//    }
//
//    /**
//     * Routine name and expected time properly instantiated
//     */
//    @Test
//    public void getNameAndEstimatedTime() {
//        String name = "New Routine";
//        int expectedTime = 1000;
//        final Routine testRoutine = new Routine(expectedTime, name);
//        assertEquals(expectedTime, testRoutine.getEstimatedTime());
//        assertEquals(name, testRoutine.getName());
//    }
//
//    /**
//     * Routine timer stops counting when routine is paused
//     */
//    @Test
//    public void pauseRoutineTimer() throws InterruptedException {
//        Routine testRoutine = new Routine(10, "test");
//        testRoutine.addTask(new Task("test task"));
//        testRoutine.startRoutine();
//        Thread.sleep(1000);
//        testRoutine.pauseRoutineTimer();
//        Thread.sleep(2000);
//        assertEquals(1, testRoutine.getElapsedTimeSecs());
//    }
//
//    /**
//     * Routine timer can manually add time even when paused
//     */
//    @Test
//    public void manualAddTime() throws InterruptedException {
//        Routine testRoutine = new Routine(10, "test");
//        testRoutine.addTask(new Task("test task"));
//        testRoutine.startRoutine();
//        Thread.sleep(1000);
//        testRoutine.pauseRoutineTimer();
//        Thread.sleep(2000);
//        assertEquals(1, testRoutine.getElapsedTimeSecs());
//        testRoutine.manualAddTime(46);
//        assertEquals(47, testRoutine.getElapsedTimeSecs());
//    }
//
//    /**
//     * Tasks are properly renamed when edited
//     */
//    @Test
//    public void editTask() {
//        Routine testRoutine = new Routine(10, "test");
//        Task task1 = new Task("test task 1");
//        Task task2 = new Task("test task 2");
//        Task task3 = new Task("test task 3");
//        testRoutine.addTask(task1);
//        testRoutine.addTask(task2);
//        testRoutine.addTask(task3);
//        testRoutine.editTask(task2, "renamed task 2");
//        assertEquals("renamed task 2", testRoutine.getTaskList().get(1).getName());
//    }
//
//    /**
//     * Cannot edit a task name to the name of another existing task
//     */
//    @Test
//    public void editTaskDup() {
//
//        var morning = new Routine(duration, "Morning");
//        final Task shower = new Task("shower");
//        final Task dupShower = new Task("new shower");
//        String expectedErrMsg = "Cannot have two tasks with the same name in Routine";
//        morning.addTask(shower);
//
//        try {
//            morning.editTask(dupShower, "shower");
//            fail("Expected exception thrown");
//
//        } catch (IllegalArgumentException e) {
//
//            assertEquals(expectedErrMsg, e.getMessage());
//        }
//        Routine testRoutine = new Routine(10, "test");
//        Task task1 = new Task("test task 1");
//        Task task2 = new Task("test task 2");
//        Task task3 = new Task("test task 3");
//        testRoutine.addTask(task1);
//        testRoutine.addTask(task2);
//        testRoutine.addTask(task3);
//        try {
//            testRoutine.editTask(task2, "test task 3");
//
//        } catch (IllegalArgumentException e) {
//            assertEquals(expectedErrMsg, e.getMessage());
//        }
//    }
//
//    /**
//     * Ending and restarting routine properly resets timer
//     */
//    @Test
//    public void pauseRestartRoutine() throws InterruptedException {
//        var morning = new Routine(duration, "Morning");
//        Task task1 = new Task("test task 1");
//        Task task2 = new Task("test task 2");
//        Task task3 = new Task("test task 3");
//        morning.addTask(task1);
//        morning.addTask(task2);
//        morning.addTask(task3);
//
//        morning.startRoutine();
//        Thread.sleep(2000);
//        assertEquals(2, morning.getElapsedTimeSecs());
//        morning.endRoutine();
//        assertEquals(2, morning.getElapsedTimeSecs());
//        morning.startRoutine();
//        assertEquals(0, morning.getElapsedTimeSecs());
//        Thread.sleep(1000);
//        assertEquals(1, morning.getElapsedTimeSecs());
//    }
//
//
//}
