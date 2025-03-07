package edu.ucsd.cse110.habitizer.app;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.app.Application;
import androidx.test.platform.app.InstrumentationRegistry;

import androidx.room.Room;

import edu.ucsd.cse110.habitizer.app.data.db.TaskDao;
import edu.ucsd.cse110.habitizer.app.data.db.TaskEntity;
import edu.ucsd.cse110.habitizer.app.data.db.HabitizerDatabase;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class TaskDaoTest {
//    public TaskDao taskDao;
//    public HabitizerDatabase database;
//
//    @Before
//    public void setUp() {
//        var database = Room.inMemoryDatabaseBuilder(
//                getApplicationContext(),
//                HabitizerDatabase.class
//        )
//                .allowMainThreadQueries()
//                .build();
//
//        this.taskDao = database.taskDao();
//    }
//
//    @Test
//    public void testInsert() {
//        TaskEntity task = new TaskEntity(1, "meow");
//        taskDao.insert(task);
//
//        assertEquals(task, taskDao.findAllByRoutineId(1));
//    }
}
