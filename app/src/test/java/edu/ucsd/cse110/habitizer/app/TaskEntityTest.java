package edu.ucsd.cse110.habitizer.app;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ucsd.cse110.habitizer.app.data.db.TaskEntity;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class TaskEntityTest {

    @Test
    public void toTask() {
        TaskEntity te = new TaskEntity(0, "entity");
        Task t = te.toTask();
        assertEquals(t.getName(), "entity");
        assertFalse(t.isCompleted());
    }

    @Test
    public void fromTask() {
        Task t = new Task("brusft eete");
        t.completeTask();
        TaskEntity te = TaskEntity.fromTask(0, t);
        assertEquals("brusft eete", te.taskName);
        assertTrue(te.completed);
    }
}