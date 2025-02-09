package edu.ucsd.cse110.habitizer.lib.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import junit.framework.AssertionFailedError;

import org.junit.Assert;
import org.junit.Test;

import java.time.*;

public class RoutineTest {
    int duration = 45;
    int duration_2 = 60;
    int duration_3 = 120;

    private final Routine morning = new Routine(duration, "Morning");
    private final Routine night = new Routine(duration_2, "Night");
    private final Routine school = new Routine(duration_3, "School");

    @Test
    public void testAddTask() {
        final Task shower = new Task("shower");
        final Task brush = new Task("brush");
        final Task lunch = new Task("lunch");

        morning.addTask(shower);
        morning.addTask(brush);
        morning.addTask(lunch);

        assertEquals(morning.getTaskList().size(), 3);
        assertEquals(morning.getTaskList().getFirst().getName(), "shower");
        assertEquals(morning.getTaskList().get(1).getName(), "brush");
        assertEquals(morning.getTaskList().getLast().getName(), "lunch");

    }

//    @Test
//    public void testRemoveTask() {
//      final Task lunch = new Task("lunch");
//      night.addTask(lunch);
//      assertEquals(night.getTaskList().size(), 1);
//      night.removeTask(lunch);
//      assertEquals(night.getTaskList().size(), 0);
//    }

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
    public void testTimer() throws InterruptedException {
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

    }
