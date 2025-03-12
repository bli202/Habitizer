package edu.ucsd.cse110.habitizer.app;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.ucsd.cse110.habitizer.app.data.db.CustomTimerEntity;
import edu.ucsd.cse110.habitizer.app.data.db.RoutineEntity;
import edu.ucsd.cse110.habitizer.app.data.db.TaskEntity;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class RoutineEntityTest {
    Routine r;
    RoutineEntity re;
    @Before
    public void setUp() {
        r = new Routine(0, 13, "routine 1");
        Task task1 = new Task("eat food");
        Task task2 = new Task("smell the flowers");
        r.addTask(task1);
        r.addTask(task2);
        re = new RoutineEntity(3, 20, "routineEntity 1");
    }
    @Test
    public void fromRoutine() {
        RoutineEntity re = RoutineEntity.fromRoutine(r);
        assertEquals(Integer.valueOf(0), re.id);
        assertEquals(Integer.valueOf(13), re.estimatedTime);
        assertEquals(false, re.ongoing);
        assertEquals(Integer.valueOf(0), re.tasksDone);
        assertEquals("routine 1", re.name);
    }

    @Test
    public void toRoutine() {
        TaskEntity te1 = new TaskEntity(3, "meow for a lil bit", 5);
        TaskEntity te2 = new TaskEntity(3, "bark bark", 6);
        List<TaskEntity> teList = new ArrayList<>();
        teList.add(te1);
        teList.add(te2);
        Routine re2 = re.toRoutine(teList);
        assertEquals(te1.taskName, re2.getTaskList().get(0).getName());
        assertEquals(te1.routineId, Integer.valueOf(re2.getTaskList().get(0).getId()));
        assertEquals(te2.taskName, re2.getTaskList().get(1).getName());
        assertEquals(re.ongoing, re2.getOngoing());

    }
}