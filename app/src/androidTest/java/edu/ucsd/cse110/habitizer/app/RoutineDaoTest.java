package edu.ucsd.cse110.habitizer.app;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        TaskEntity task1 = new TaskEntity(1, "woof");
        TaskEntity task2 = new TaskEntity(1, "meow");
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
        List<RoutineEntity> routines = new ArrayList<>();
        routines.add(sampleRoutine1);
        routines.add(sampleRoutine2);
        assertEquals(1, routineDao.findAll().size());
        routineDao.insertTimer(new CustomTimerEntity(2, 0, 0, false, 0, 0));
        routineDao.insertTimer(new CustomTimerEntity(4, 0, 0, false, 0, 0));
        routineDao.insertRoutines(routines);
        assertEquals(3, routineDao.findAll().size());
    }

    @Test
    public void insertTaskTest() {
        TaskEntity task3 = new TaskEntity(1, "bark");
        routineDao.insertTask(task3);
        assertEquals(3, routineDao.findTasksForRoutine(1).size());
    }

    @Test
    public void insertTasksTest() {
        TaskEntity task3 = new TaskEntity(1, "moo");
        TaskEntity task4 = new TaskEntity(1, "purr");
        List<TaskEntity> tasks = new ArrayList<>();
        tasks.add(task3);
        tasks.add(task4);
        routineDao.insertTasks(tasks);
        assertEquals(4, routineDao.findTasksForRoutine(1).size());

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
    public void findTasksForRoutineTest() {
        var rout3 = new RoutineEntity(3, 0,"rout3");
        routineDao.append(rout3, new CustomTimerEntity(3, 0, 0, false, 0, 0));
        List<TaskEntity> tasklist = new ArrayList<>();
        tasklist.add(new TaskEntity(3, "task1"));
        tasklist.add(new TaskEntity(3, "task2"));
        routineDao.insertTasks(tasklist);
        assertEquals(tasklist.get(0).taskName, routineDao.findTasksForRoutine(3).get(0).taskName);
        assertEquals(tasklist.get(1).taskName, routineDao.findTasksForRoutine(3).get(1).taskName);

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
    public void findAsLiveDataTest() {

        var rout3 = new RoutineEntity(3, 0,"rout3");
        routineDao.append(rout3, new CustomTimerEntity(3, 0, 0, false, 0, 0));
        LiveData<RoutineEntity> rrout3 = routineDao.findAsLiveData(3);
        RoutineEntity[] rrout3data = new RoutineEntity[1];
        rrout3.observeForever(new Observer<RoutineEntity>() {
            @Override
            public void onChanged(RoutineEntity routineEntity) {
                rrout3data[0] = routineEntity;
            }
        });

        assertEquals(rout3, rrout3data[0]);
    }

    @Test
    public void findAllAsLiveDataTest() {
        assertEquals(2, Objects.requireNonNull(routineDao.findAllAsLiveData().getValue()).size());

    }

    @Test
    public void appendTest() {
        var rout3 = new RoutineEntity(2, 0,"rout3");
        routineDao.append(rout3, new CustomTimerEntity(2, 0, 0, false, 0, 0));
        assertEquals(2, routineDao.findAll().size());
    }

    @Test
    public void addTaskToRoutineTest() {
        TaskEntity task3 = new TaskEntity(1, "bark");
        routineDao.addTaskToRoutine(routineDao.find(1), task3.toTask());
        assertEquals(3, routineDao.findTasksForRoutine(1).size());
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
