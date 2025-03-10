package edu.ucsd.cse110.habitizer.app;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.Optional;

import edu.ucsd.cse110.habitizer.app.data.db.TaskEntity;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class TaskEntityTest {

    @Test
    public void toTask() {
        TaskEntity te = new TaskEntity(0, "entity", 5);
        Task t = te.toTask();
        assertEquals(t.getName(), "entity");
        assertFalse(t.isCompleted());
        assertEquals(t.getOrder(), 5);
    }

    @Test
    public void fromTaskDefault() {
        Task t = new Task("brush");
        t.completeTask();
        TaskEntity te = TaskEntity.fromTask(0, t);
        assertEquals("brush", te.taskName);
        assertTrue(te.completed);
    }

    @Test
    public void fromTaskWithOrder() {
        Task t = new Task("brush", 4);
        TaskEntity te = TaskEntity.fromTask(4, t);
        assertEquals("brush", te.taskName);
        assertEquals(Integer.valueOf(4), te.sortOrder);
        assertFalse(te.completed);
        assertEquals(Integer.valueOf(4), te.routineId);
    }
}
