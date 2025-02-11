package edu.ucsd.cse110.habitizer.lib.domain;
import static org.junit.Assert.*;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class RoutineTest {
    int duration = 45;
    int duration_2 = 60;
    int duration_3 = 120;

    private final Routine morning = new Routine(duration, "Morning");
    private final Routine night = new Routine(duration_2, "Night");
    private final Routine school = new Routine(duration_3, "School");

    @Test
    public void testRoutineInitialState() {
        var morning = new Routine(duration, "Morning");
        var expectedDuration = duration;
        var actDuration = morning.getEstimatedTime();
        //checks initial num of task in routine
        assertFalse(morning.getongoing());
        assertEquals(Duration.ZERO, morning.getElapsedTime());
        assertEquals(expectedDuration, actDuration);
        assertEquals("Morning", morning.getName());
        assertEquals(0, morning.getTasksDone());

    }

    @Test
    public void testAddTask() {

        var morning = new Routine(duration, "Morning");
        final Task shower = new Task("shower");
        final Task brush = new Task("brush");
        final Task lunch = new Task("lunch");

        morning.addTask(shower);
        morning.addTask(brush);
        morning.addTask(lunch);

        assertEquals(morning.getTaskList().size(), 3);
        assertEquals(morning.getTaskList().get(0).getName(), "shower");
        assertEquals(morning.getTaskList().get(1).getName(), "brush");
        assertEquals(morning.getTaskList().get(2).getName(), "lunch");

    }

    @Test
    public void testAddTaskDup() {

        var morning = new Routine(duration, "Morning");
        final Task shower = new Task("shower");
        final Task dupShower = new Task("shower");
        String expectedErrMsg = "Cannot have two tasks with the same name in Routine";
        morning.addTask(shower);

        try {
            morning.addTask(dupShower);
            fail("Expected exception thrown");

        } catch (IllegalArgumentException e) {

            assertEquals(expectedErrMsg, e.getMessage());
        }
    }

    @Test
    public void testAddTaskOngoingRoutine() {

        final Task shower = new Task("shower");
        final Task brush = new Task("brush");
        morning.addTask(shower);
        morning.startRoutine();
        assertFalse(morning.addTask(brush));
    }

    @Test
    public void testRemoveTaskOngoing() {

        final Task shower = new Task("shower");
        morning.addTask(shower);
        morning.startRoutine();
        assertFalse(morning.removeTask(shower));
    }

    @Test
    public void testRemoveTask() {

      final Task lunch = new Task("lunch");
      night.addTask(lunch);
      assertEquals(night.getTaskList().size(), 1);
      night.removeTask(lunch);
      assertEquals(night.getTaskList().size(), 0);
    }

    @Test
    public void testCheckOffTask() {

      final Task dinner = new Task("dinner");
      final Task lunch = new Task("lunch");

      school.addTask(dinner);
      school.addTask(lunch);
      school.startRoutine();
      school.checkOffTask(dinner);
      assertTrue(dinner.isCompleted());
      assertFalse(lunch.isCompleted());
    }


    @Test
    public void testTaskTimer() throws InterruptedException {
        final Task lunch = new Task("lunch");
        final Task dinner = new Task("dinner");
        final Task snack = new Task("snack");

        school.addTask(dinner);
        school.addTask(lunch);
        school.addTask(snack);

        // Testing two task timers
        school.startRoutine();
        Thread.sleep(1000);
        school.checkOffTask(dinner);
        assertEquals(1000, dinner.getTimeSpent(), 100);
        Thread.sleep(3400);
        school.checkOffTask(lunch);
        assertEquals(3000, lunch.getTimeSpent(), 100);
    }

    @Test
    public void testStartRoutineNoTask() {

        try {
            morning.startRoutine();
            fail("Expected IllegalArgumentException thrown");
        } catch (IllegalArgumentException e){
            assertEquals( "Cannot start a routine with no tasks", e.getMessage());
        }
    }

    @Test
    public void testStartRoutineState() {

        final Task lunch = new Task("lunch");
        morning.addTask(lunch);
        morning.startRoutine();

        assertEquals(0, morning.getTasksDone());
        assertEquals(0, morning.getElapsedTime().toMillis(), 100);
        assertEquals(Instant.now().toEpochMilli(), morning.getStartTime().toEpochMilli(), 100);
        assertTrue(morning.getongoing());
    }

    @Test
    public void testGetTaskList() {
        final Task lunch = new Task("lunch");
        final Task dinner = new Task("dinner");
        final Task read = new Task("Read");

        school.addTask(lunch);
        school.addTask(dinner);
        school.addTask(read);

        List<Task> actualAns = school.getTaskList();
        List<Task> expectedAns = new ArrayList<>();

        expectedAns.add(lunch);
        expectedAns.add(dinner);
        expectedAns.add(read);

        assertEquals(expectedAns, actualAns);
    }

    @Test
    public void testGetCumTaskTime() throws InterruptedException {
        var morning = new Routine(duration, "Morning");
        final Task brush = new Task("brush");
        assertNotNull(morning.getCumTaskTime());

        morning.addTask(brush);
        morning.startRoutine();
        //Get the cumulative task time before checking off a task, which should be 0
        Instant beforeCheckOff = morning.getCumTaskTime();
        Thread.sleep(1000);
        morning.checkOffTask(brush);
        //here we should get a time for 1 second
        Instant afterCheckOff = morning.getCumTaskTime();

        int actAns = (int) (afterCheckOff.toEpochMilli() - beforeCheckOff.toEpochMilli());
        assertEquals(1000, actAns, 100);
    }

    @Test
    public void testIsOnGoing() {

        final Task brush = new Task("brush");
        final Task eat = new Task("eat");

        assertFalse(morning.getongoing());
        morning.addTask(brush);
        morning.addTask(eat);
        morning.startRoutine();
        assertTrue(morning.getongoing());
        morning.checkOffTask(brush);
        assertTrue(morning.getongoing());
        morning.endRoutine();
        assertFalse(morning.getongoing());

        school.addTask(eat);
        school.startRoutine();
        school.checkOffTask(eat);
        assertFalse(school.getongoing());

    }

    @Test
    public void testGetTasksDoneGetNumTask() {

        final Task brush = new Task("brush");
        final Task eat = new Task("eat");
        final Task shower = new Task("shower");

        morning.addTask(brush);
        morning.addTask(eat);
        morning.addTask(shower);

        assertEquals(3, morning.getNumTasks());
        morning.removeTask(shower);
        assertEquals(2, morning.getNumTasks());

        morning.startRoutine();
        morning.checkOffTask(brush);
        assertEquals(1, morning.getTasksDone());
        morning.checkOffTask(eat);
        assertEquals(2, morning.getTasksDone());
    }

    @Test
    public void getNameandEstimatedTime() {
        String name = "New Routine";
        int expectedTime = 1000;
        final Routine testRoutine = new Routine(expectedTime, name);
        assertEquals(expectedTime, testRoutine.getEstimatedTime());
        assertEquals(name, testRoutine.getName());
    }

    @Test
    public void getStartTime() throws InterruptedException{
        final Task brush = new Task("brush");
        Instant expStartTime = Instant.now();
        morning.addTask(brush);
        morning.startRoutine();
        assertEquals(expStartTime.toEpochMilli(), morning.getStartTime().toEpochMilli(), 100);
    }




}
