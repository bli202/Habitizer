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


    @Test
    public void testStartTaskNotCompleted() {
        var shower = new Task("Take shower");
//        shower.startTask();
        assertFalse("The started task should not be marked complete", shower.isCompleted());
        assertEquals("Incomplete task should have a 0 timer",
                shower.getTimeSpent(), 0);
    }


    @Test
    public void testStartTaskCompleteTask() {

    }





}