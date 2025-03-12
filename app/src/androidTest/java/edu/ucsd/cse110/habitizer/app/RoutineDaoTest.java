package edu.ucsd.cse110.habitizer.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.ucsd.cse110.habitizer.app.data.db.CustomTimerEntity;
import edu.ucsd.cse110.habitizer.app.data.db.HabitizerDatabase;
import edu.ucsd.cse110.habitizer.app.data.db.TaskDao;
import edu.ucsd.cse110.habitizer.app.data.db.TaskEntity;
import edu.ucsd.cse110.habitizer.app.data.db.RoutineDao;
import edu.ucsd.cse110.habitizer.app.data.db.RoutineEntity;
public class RoutineDaoTest {
    public RoutineDao routineDao;
    public TaskDao taskDao;
    public HabitizerDatabase database;

    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @Before
    public void setUp() {
        // Create in-memory database
        database = Room.inMemoryDatabaseBuilder(
                        appContext,
                        HabitizerDatabase.class
                )
                .allowMainThreadQueries()
                .build();

        this.taskDao = database.taskDao();
        this.routineDao = database.routineDao();
        RoutineEntity DummyRoutine = new RoutineEntity(1, 30,"Routine1");
        routineDao.append(DummyRoutine);

    }

    @Test
    public void insertTest() {

        RoutineEntity sampleRoutine = new RoutineEntity(2, 30, "sampleRoutine");
        routineDao.insert(sampleRoutine);
        assertEquals(2, routineDao.findAll().size());
        assertEquals("Routine1", routineDao.find(1).name);
        assertEquals("sampleRoutine", routineDao.find(2).name);
        assertEquals(Integer.valueOf(1), routineDao.find(1).id);
        assertEquals(Integer.valueOf(2), routineDao.find(2).id);


    }

    @Test
    public void findTest() {
        var rout3 = new RoutineEntity(3, 0,"rout3");
        routineDao.append(rout3);
        var rrout3 = routineDao.find(3);
        assertEquals(rout3.id, rrout3.id);
        assertEquals(rout3.estimatedTime, rrout3.estimatedTime);
        assertEquals(rout3.name, rrout3.name);
        assertFalse(rrout3.ongoing);
    }

    @Test
    public void findAllTest() {
        RoutineEntity r1 = new RoutineEntity(2, 30, "sampleRoutine1");
        routineDao.insert(r1);
        RoutineEntity r2 = new RoutineEntity(3, 30, "sampleRoutine2");
        routineDao.insert(r2);

        List<RoutineEntity> actList = new ArrayList<>(routineDao.findAll());
        assertEquals("Routine1", actList.get(0).name);
        assertEquals("sampleRoutine1", actList.get(1).name);
        assertEquals("sampleRoutine2", actList.get(2).name);
        assertEquals(3, actList.size());
        assertEquals(Integer.valueOf(2), actList.get(1).id);
        assertEquals(Integer.valueOf(3), actList.get(2).id);

    }

    @Test
    public void appendTest() {
        RoutineEntity r3 = new RoutineEntity(2, 0,"rout3");
        routineDao.append(r3);
        assertEquals(2, routineDao.findAll().size());
        assertEquals("Routine1", routineDao.find(1).name);
        assertEquals("rout3", routineDao.find(2).name);
    }

    @Test
    public void deleteTest() {
        routineDao.delete(1);
        assertEquals(0, routineDao.findAll().size());
    }

    @Test
    public void setEstimatedTimeTest() {
        routineDao.setEstimatedTime(1, 7);
        assertEquals(Optional.of(7), Optional.of(routineDao.find(1).estimatedTime));
    }

    @Test
    public void countTest() {
        assertEquals(1, routineDao.count());
        RoutineEntity r1 = new RoutineEntity(2, 30, "sampleRoutine1");
        routineDao.insert(r1);
        assertEquals(2, routineDao.count());
    }

    @Test
    public void setOnGoingTest() {

        RoutineEntity actualR = routineDao.find(1);
        assertFalse(actualR.ongoing);
        routineDao.setOngoing(1, true);
        actualR = routineDao.find(1);
        assertTrue(actualR.ongoing);

    }

    @Test
    public void IncAndResetAndGetTasksTest() {
        assertEquals(0, routineDao.getTasksDone(1));
        routineDao.incrementTasksDone(1);
        assertEquals(1, routineDao.getTasksDone(1));
        routineDao.incrementTasksDone(1);
        assertEquals(2, routineDao.getTasksDone(1));
        routineDao.resetTasksDone(1);
        assertEquals(0, routineDao.getTasksDone(0));

    }

}