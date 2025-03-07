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
    public TaskDao taskDao;
    public RoutineDao routineDao;
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

        taskDao = database.taskDao();
        routineDao = database.routineDao();
        RoutineEntity routine = new RoutineEntity(1, 1,"morning routine");
        var customTimerEntity = new CustomTimerEntity(1, 0, 0, false, 0, 0);

        routineDao.append(routine, customTimerEntity);
        TaskEntity task1 = new TaskEntity(1, "woof");
        TaskEntity task2 = new TaskEntity(1, "meow");
        taskDao.insert(task1);
        taskDao.insert(task2);
    }

    @Test
    public void testInsert() {
        TaskEntity fetchedTask = taskDao.getTask(1,"meow");  // Fetch the task by name
        assertEquals("meow", fetchedTask.taskName);
    }

    @Test
    public void findAllByRoutineIdTest() {
        List<TaskEntity> taskList = taskDao.findAllByRoutineId(1);
        assertEquals("meow", taskList.get(0).taskName);
        assertEquals("woof", taskList.get(1).taskName);
    }

    @Test
    public void deleteByRoutineIdAndTaskNameTest() {
        taskDao.deleteByRoutineIdAndTaskName(1, "woof");
        assertEquals(1, taskDao.findAllByRoutineId(1).size());
        taskDao.deleteByRoutineIdAndTaskName(1, "meow meow");
        assertEquals(1, taskDao.findAllByRoutineId(1).size());
    }

    @Test
    public void updateTest() {
        TaskEntity task3 = new TaskEntity(1, "grrr");
        taskDao.insert(task3);
        taskDao.update(task3);
    }

    @Test
    public void getTaskTest() {
        TaskEntity fetchedTask = taskDao.getTask(1,"meow");
        assertEquals("meow", fetchedTask.taskName);
    }


}
