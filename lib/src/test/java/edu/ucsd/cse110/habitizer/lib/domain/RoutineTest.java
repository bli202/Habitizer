package edu.ucsd.cse110.habitizer.lib.domain;
import static org.junit.Assert.*;

import org.junit.Assert;
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
        assertEquals(0, morning.getNumTasks());
        assertFalse(morning.isOnGoing());
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
      school.checkOffTask("dinner");
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
        long startTime = System.currentTimeMillis();
        school.startRoutine();
        Thread.sleep(1000);
        school.checkOffTask("dinner");
        int elapsedTime = (int) (System.currentTimeMillis() - startTime);
        assertEquals(elapsedTime, dinner.getTimeSpent(), 100);
        Thread.sleep(1000);
        school.checkOffTask("lunch");
        assertEquals(elapsedTime, lunch.getTimeSpent(), 100);

        // Testing routine elapsed time.
        Thread.sleep(2000);
        long elapsedTimeThree = System.currentTimeMillis() - startTime;
        assertEquals(elapsedTimeThree, school.getElapsedTime().toMillis(), 100);
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
        assertTrue(morning.isOnGoing());
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

}
