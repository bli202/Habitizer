package edu.ucsd.cse110.habitizer.app;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ucsd.cse110.habitizer.app.data.db.RoutineEntity;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;

public class RoutineEntityTest {

    @Test
    public void fromRoutine() {
        Routine r = new Routine(0, 13, "routine 1");
        RoutineEntity re = RoutineEntity.fromRoutine(r);
        assertEquals(Integer.valueOf(0), re.id);
        assertEquals(Integer.valueOf(13), re.estimatedTime);
        assertEquals(false, re.ongoing);
        assertEquals(Integer.valueOf(0), re.tasksDone);
        assertEquals("routine 1", re.name);
    }

    @Test
    public void toRoutine() {
        RoutineEntity re = new RoutineEntity(0, 14, "routine 2");

        Routine r = re.toRoutine()
    }
}