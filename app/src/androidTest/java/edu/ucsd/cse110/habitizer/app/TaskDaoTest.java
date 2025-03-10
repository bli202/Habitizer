package edu.ucsd.cse110.habitizer.app;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import edu.ucsd.cse110.habitizer.app.data.db.CustomTimerEntity;
import edu.ucsd.cse110.habitizer.app.data.db.HabitizerDatabase;
import edu.ucsd.cse110.habitizer.app.data.db.TaskDao;
import edu.ucsd.cse110.habitizer.app.data.db.TaskEntity;
import edu.ucsd.cse110.habitizer.app.data.db.RoutineDao;
import edu.ucsd.cse110.habitizer.app.data.db.RoutineEntity;

public class TaskDaoTest {
    public TaskDao taskDaot;
    public RoutineDao routineDaot;
    public HabitizerDatabase database;

    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @Before
    public void setUp() {
        database = Room.inMemoryDatabaseBuilder(
                        appContext,
                        HabitizerDatabase.class
                )
                .allowMainThreadQueries()
                .build();

        this.taskDaot = database.taskDao();
        this.routineDaot = database.routineDao();
        RoutineEntity DummyRoutine = new RoutineEntity(1, 30,"Routine1");
        CustomTimerEntity DummyTimer = new CustomTimerEntity(1, 0, 0, false, 0, 0);
        routineDaot.append(DummyRoutine, DummyTimer);


    }

    @Test
    public void testInsertAndFind() {

        TaskEntity t = new TaskEntity(1, "test", 2);
        taskDaot.insert(t);
        TaskEntity actualT = taskDaot.find(1, "test");

        assertEquals(t.taskName, actualT.taskName);
        assertEquals(t.routineId, actualT.routineId);
        assertEquals(t.sortOrder, actualT.sortOrder);

    }

    @Test
    public void testAppendAndFind() {

        TaskEntity t1 = new TaskEntity(1, "test1", 4);
        taskDaot.append(t1);
        TaskEntity actualT = taskDaot.find(1, "test1");
        int t1Sort = 1;

        assertEquals(t1.taskName, actualT.taskName);
        assertEquals(t1.routineId, actualT.routineId);
        System.out.println(actualT.sortOrder);
        assertEquals(Integer.valueOf(t1Sort), actualT.sortOrder);

    }

    @Test
    public void findAllByRoutineIdTest() {
        TaskEntity t1 = new TaskEntity(1, "1", 0);
        TaskEntity t2 = new TaskEntity(1, "2", 2);
        taskDaot.append(t1);
        taskDaot.append(t2);
        List<TaskEntity> taskList = taskDaot.findAllByRoutineId(1);
        assertEquals("meow", taskList.get(0).taskName);
        assertEquals("woof", taskList.get(1).taskName);
    }

    @Test
    public void deleteByRoutineIdAndTaskNameTest() {
        taskDaot.deleteByRoutineIdAndTaskName(1, "woof");
        assertEquals(1, taskDaot.findAllByRoutineId(1).size());
        taskDaot.deleteByRoutineIdAndTaskName(1, "meow meow");
        assertEquals(1, taskDaot.findAllByRoutineId(1).size());
    }

    @Test
    public void updateTest() {
        TaskEntity task3 = new TaskEntity(1, "grrr", 1);
        taskDaot.insert(task3);
        taskDaot.update(task3);
    }

    @Test
    public void getTaskTest() {
        TaskEntity fetchedTask = taskDaot.find(1,"meow");
        assertEquals("meow", fetchedTask.taskName);
    }


}