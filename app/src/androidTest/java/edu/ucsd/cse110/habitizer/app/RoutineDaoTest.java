package edu.ucsd.cse110.habitizer.app;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

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

        this.routineDao = database.routineDao();
        this.taskDao = database.taskDao();
        RoutineEntity routine = new RoutineEntity(1, 1,"morning routine");
        var customTimerEntity = new CustomTimerEntity(1, 0, 0, false, 0, 0);

        routineDao.append(routine, customTimerEntity);
        TaskEntity task1 = new TaskEntity(1, "woof", 0);
        TaskEntity task2 = new TaskEntity(1, "meow", 1);
        taskDao.insert(task1);
        taskDao.insert(task2);
    }

    @Test
    public void insertTest() {
        RoutineEntity sampleRoutine = new RoutineEntity(2, 30, "sampleRoutine");
        routineDao.insertTimer(new CustomTimerEntity(2, 0, 0, false, 0, 0));
        routineDao.insert(sampleRoutine);
        assertEquals(2, routineDao.findAll().size());
    }

    @Test
    public void insertRoutinesTest() {
        RoutineEntity sampleRoutine1 = new RoutineEntity(2, 30, "sampleRoutine1");
        RoutineEntity sampleRoutine2 = new RoutineEntity(4, 30, "sampleRoutine2");
        assertEquals(1, routineDao.findAll().size());
        List<RoutineEntity> routines = new ArrayList<>();
        routines.add(sampleRoutine1);
        routines.add(sampleRoutine2);

        assertEquals(3, routineDao.findAll().size());
    }



    @Test
    public void insertTimerTest() {
        var tim =  new CustomTimerEntity(2, 0, 0, false, 0, 0);
        routineDao.insertTimer(tim);
        RoutineEntity sampleRoutine = new RoutineEntity(2, 30, "sampleRoutine");
        routineDao.insert(sampleRoutine);
        var rtim = routineDao.findTimerForRoutine(2);
        assertEquals(tim.routineId, rtim.routineId);
        assertEquals(tim.cumTime, rtim.cumTime);
        assertEquals(tim.taskTime, rtim.taskTime);
        assertEquals(tim.ongoing, rtim.ongoing);
        assertEquals(tim.startTime, rtim.startTime);
        assertEquals(tim.taskStartTime, rtim.taskStartTime);
    }

    @Test
    public void findTest() {
        var rout3 = new RoutineEntity(3, 0,"rout3");
        routineDao.append(rout3, new CustomTimerEntity(3, 0, 0, false, 0, 0));
        var rrout3 = routineDao.find(3);
        assertEquals(rout3.id, rrout3.id);
        assertEquals(rout3.estimatedTime, rrout3.estimatedTime);
        assertEquals(rout3.name, rrout3.name);
    }




    @Test
    public void findTimerForRoutineTest() {
        var tim =  new CustomTimerEntity(2, 0, 0, false, 0, 0);
        routineDao.insertTimer(tim);
        RoutineEntity sampleRoutine = new RoutineEntity(2, 30, "sampleRoutine");
        routineDao.insert(sampleRoutine);
        var rtim = routineDao.findTimerForRoutine(2);
        assertEquals(tim.routineId, rtim.routineId);
        assertEquals(tim.cumTime, rtim.cumTime);
        assertEquals(tim.taskTime, rtim.taskTime);
        assertEquals(tim.ongoing, rtim.ongoing);
        assertEquals(tim.startTime, rtim.startTime);
        assertEquals(tim.taskStartTime, rtim.taskStartTime);
    }

    @Test
    public void findAllTest() {
        assertEquals(1, routineDao.findAll().size());
    }

    @Test
    public void appendTest() {
        var rout3 = new RoutineEntity(2, 0,"rout3");
        routineDao.append(rout3, new CustomTimerEntity(2, 0, 0, false, 0, 0));
        assertEquals(2, routineDao.findAll().size());
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
    }
}