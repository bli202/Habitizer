package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import junit.framework.AssertionFailedError;

import org.junit.Assert;
import org.junit.Test;
import java.time.*;

public class TaskTest {

    private final Task shower = new Task("Take shower");
    private final Task lunch = new Task("Make lunch");
    private final Task dinner = new Task("Make dinner");

    @Test
    public void testStartState() {

        var shower = new Task("Take shower");

        //Check a new task is not completed and the timer should be 0
        assertEquals("Initial time spent should be zero",
                shower.getTimeSpent(), 0);

        //Check that a task is marked incomplete when created
        assertFalse("Task should not be completed when initialized", shower.isCompleted());
    }

    @Test
    public void testGetName() {

        String expectedName = "Take shower";
        String actualName = shower.getName();
        assertEquals("Name returned should match the name in constructor",
                expectedName, actualName);
    }

    @Test
    public void testSetName() {
        var shower = new Task("Take shower");
        shower.setName("Brush teeth");
        String expectedName = "Brush teeth";
        String actName = shower.getName();

        assertEquals("Name returned should match the newly set name",
                expectedName, actName);
    }

    //Testing getTime and setTime getter/setter
    @Test
    public void testSetGetTaskTime() {
        var shower = new Task("Take shower");
        shower.setTime(5);
        int expectedAns = 5;
        int actAns = shower.getTimeSpent();
        assertEquals(expectedAns, actAns);
    }

    @Test
    public void testTaskToggleCompletion() {
        Task task = new Task("Test Task");

        assertFalse("New task should not be completed", task.isCompleted());

        task.toggleCompletion();
        assertTrue("Task should be completed after first toggle", task.isCompleted());

        task.toggleCompletion();
        assertFalse("Task should not be completed after second toggle", task.isCompleted());
    }

    @Test
    public void testTaskToggleCompletionMultipleTasks() {
        Task shower = new Task("Test");
        Task gym = new Task("Test");
        Task night = new Task("Test");

        assertFalse("New task should not be completed", shower.isCompleted());
        assertFalse("New task should not be completed", gym.isCompleted());
        assertFalse("New task should not be completed", night.isCompleted());


        shower.toggleCompletion();
        // testing complete/non complete mix
        assertTrue("Task should be completed after first toggle", shower.isCompleted());
        assertFalse("Gym and night should still be false", gym.isCompleted());
        assertFalse("Gym and night should still be false", night.isCompleted());

        shower.toggleCompletion();
        gym.toggleCompletion();
        assertFalse("Task should not be completed after second toggle", shower.isCompleted());
        assertTrue("Gym should be complete", gym.isCompleted());
        assertFalse("Night should be complete", night.isCompleted());
    }
}