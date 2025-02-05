package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Test;

import java.time.Duration;

/**
 * Example test demonstrating use of Hamcrest matchers (which are also
 * used for espresso assertions in instrumented Android UI tests).
 */
public class TaskTest {
    private Task task1 = new Task("我要死了");

    @Test
    public void testDefaultCheckoff() {
        boolean actual = task1.isCheckedOff();
        boolean expected = false;
        assertEquals(actual, expected);
    }

    @Test
    public void testDefaultElapsedTime() {
        int actual = task1.getElapsedTime();
        int expected = 0;
        assertEquals(actual, expected);
    }

    @Test
    public void testConstructorName() {
        String actual = task1.getName();
        String expected = "我要死了";
        assertEquals(actual, expected);
    }

    @Test
    public void testSetCheckoff() {
        task1.setCheckedOff(true);
        boolean actual = task1.isCheckedOff();
        boolean expected = true;
        assertEquals(actual, expected);
    }

    @Test
    public void testSetName() {
        task1.setName("quiero matarme");
        String actual = task1.getName();
        String expected = "quiero matarme";
        assertEquals(actual, expected);
    }

    @Test
    public void testSetElapsedTime() {
        task1.setElapsedTime(69);
        int actual = task1.getElapsedTime();
        int expected = 69;
        assertEquals(actual, expected);
    }
}