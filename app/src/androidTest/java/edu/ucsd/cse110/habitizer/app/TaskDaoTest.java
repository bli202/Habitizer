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
        assertEquals("1", taskList.get(0).taskName);
        assertEquals("2", taskList.get(1).taskName);
        assertEquals(Integer.valueOf(1), taskList.get(0).sortOrder);
        assertEquals(Integer.valueOf(2), taskList.get(1).sortOrder);

    }

    @Test
    public void deleteByRoutineIdAndTaskNameTest() {

        TaskEntity t1 = new TaskEntity(1, "1", 0);
        TaskEntity t2 = new TaskEntity(1, "2", 2);
        taskDaot.append(t1);
        taskDaot.append(t2);
        taskDaot.deleteByRoutineIdAndTaskName(1, "1");
        assertEquals(1, taskDaot.findAllByRoutineId(1).size());

    }

    @Test
    public void editTaskTest() {

        TaskEntity t1 = new TaskEntity(1, "1", 0);
        taskDaot.append(t1);
        taskDaot.editTask(1, "1","2");
        TaskEntity actual = taskDaot.find(1, "2");
        assertEquals("2", actual.taskName);
        assertEquals(Integer.valueOf(1), actual.sortOrder);

    }

    @Test
    public void findTest() {

        TaskEntity t1 = new TaskEntity(1, "1", 0);
        taskDaot.append(t1);
        TaskEntity fetchedTask = taskDaot.find(1,"1");
        assertEquals("1", fetchedTask.taskName);
        assertEquals(Integer.valueOf(1), fetchedTask.sortOrder);

    }

    @Test
    public void getMaxSortOrderTest() {
        TaskEntity t1 = new TaskEntity(1, "1", 0);
        TaskEntity t2 = new TaskEntity(1, "1", 1);
        taskDaot.append(t1);
        taskDaot.append(t2);

        assertEquals(2, taskDaot.getMaxSortOrder(1));
    }

    @Test
    public void updateSortOrderTest() {

        TaskEntity t1 = new TaskEntity(1, "1", 5);
        taskDaot.insert(t1);
        taskDaot.updateSortOrder(1, 5, 3);
        assertEquals(3, taskDaot.getMaxSortOrder(1));
    }
}