package edu.ucsd.cse110.habitizer.app;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
        routineDao.insertTimer(new CustomTimerEntity(2, 0, 0, false, 0, 0));
        TaskEntity task1 = new TaskEntity(1, "woof");
        TaskEntity task2 = new TaskEntity(1, "meow");
        taskDao.insert(task1);
        taskDao.insert(task2);
    }

    @Test
    public void insertTest() {
        RoutineEntity sampleRoutine = new RoutineEntity(2, 30, "sampleRoutine");
        routineDao.insert(sampleRoutine);
        assertEquals(2, routineDao.findAll().size());
    }

    @Test
    public void insertRoutinesTest() {
        RoutineEntity sampleRoutine1 = new RoutineEntity(2, 30, "sampleRoutine1");
        RoutineEntity sampleRoutine2 = new RoutineEntity(4, 30, "sampleRoutine2");
        List<RoutineEntity> routines = new ArrayList<>();
        routines.add(sampleRoutine1);
        routines.add(sampleRoutine2);
        assertEquals(1, routineDao.findAll().size());
        routineDao.insertRoutines(routines);
        assertEquals(3, routineDao.findAll().size());
    }
}
