package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CustomTimerTest {

    CustomTimer t;

    /**
     * Initializes
     */
    @Before
    public void setup() {
        CustomTimer t = new CustomTimer();
    }

    @Test
    public void start() throws InterruptedException {
        CustomTimer t = new CustomTimer();
        assertEquals(0, t.getTime());
        assertFalse(t.getOngoing());
        t.start();
        Thread.sleep(1000);
        assertEquals(1, t.getTime());
    }

    @Test
    public void pause() throws InterruptedException {
        CustomTimer t = new CustomTimer();
        t.start();
        Thread.sleep(1000);
        assertEquals(1, t.getTime());
        t.pause();
        Thread.sleep(1000);
        assertEquals(1, t.getTime());
    }

    @Test
    public void getTime() throws InterruptedException {
        CustomTimer t = new CustomTimer();
        t.start();
        Thread.sleep(5000);
        assertEquals(5, t.getTime());
    }

    /**
     * Tests taskTime.
     */
    @Test
    public void getTaskTime() throws InterruptedException {
        CustomTimer t = new CustomTimer();
        t.start();
        assertEquals(0, t.getTaskTime());
        Thread.sleep(1000);
        assertEquals(1, t.getTaskTime());
        Thread.sleep(2000);
        assertEquals(2, t.getTaskTime());
    }

    /**
     * Tests add time.
     */
    @Test
    public void addTime() throws InterruptedException {
        CustomTimer t = new CustomTimer();
        t.start();
        t.addTime(27);
        assertEquals(27, t.getTime());
        Thread.sleep(1000);
        assertEquals(28, t.getTime());
        t.addTime(12);
        assertEquals(40, t.getTime());
    }

    /**
     * 1. Start Timer
     * 2. Add 10 Seconds
     * 3. Add 10 Seconds
     * 4. Pause
     * 5. Add 10 Seconds
     * 6. Add 10 Seconds
     * 7. Resume
     * 8. Add 10 Seconds
     */
    @Test
    public void addTimeIntegrated() {
        CustomTimer t = new CustomTimer();
        t.start();                          // 1
        t.addTime(10);                      // 2
        assertEquals(10, t.getTime());      // 2
        t.addTime(10);                      // 3
        assertEquals(20, t.pause());        // 4
        assertEquals(20, t.getTime());      // 4
        t.addTime(10);                      // 5
        assertEquals(30, t.getTime());      // 5
        t.addTime(10);                      // 6
        assertEquals(40, t.getTime());      // 6
        t.start();                          // 7
        assertEquals(40, t.getTime());      // 7
        t.addTime(10);                      // 8
        assertEquals(50, t.getTime());      // 8

    }

    /**
     * 1. Start timer
     * 2. Elapse 1 second
     * 3. Get task time - expect 1
     * 4. Elapse 1 second
     * 5. Pause and get time - expect 2
     * 6. Elapse 1 second
     * 7. Get task time - expect 1
     * 8. Elapse 1 second
     * 9. Play and get time - expect 2
     * 10. Elapse 1 second
     * 11. Get task time - expect 1
     */
    @Test
    public void integratedTest1() throws InterruptedException {
        CustomTimer t = new CustomTimer();
        t.start();                          // 1
        Thread.sleep(1000);                 // 2
        assertEquals(1, t.getTaskTime());   // 3
        Thread.sleep(1000);                 // 4
        assertEquals(2, t.getTime());       // 5
        assertEquals(2, t.pause());         // 5
        assertEquals(2, t.getTime());       // 5
        Thread.sleep(1000);                 // 6
        assertEquals(1, t.getTaskTime());   // 7
        Thread.sleep(1000);                 // 8
        t.start();                          // 9
        assertEquals(2, t.getTime());       // 9
        Thread.sleep(1000);                 // 10
        assertEquals(1, t.getTaskTime());   // 11
    }
}