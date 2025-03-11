package edu.ucsd.cse110.habitizer.app;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import edu.ucsd.cse110.habitizer.app.data.db.HabitizerDatabase;
import edu.ucsd.cse110.habitizer.app.data.db.RoomRoutineRepository;
import edu.ucsd.cse110.habitizer.app.data.db.RoomTaskRepository;
import edu.ucsd.cse110.habitizer.app.data.db.RoutineEntity;
import edu.ucsd.cse110.habitizer.app.data.db.TaskEntity;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;
public class US12TaskReOrderingTest {

    public HabitizerDatabase database;
    public TaskRepository taskRepo;
    public RoutineRepository routineRepo;
    long routineId;
    long t1Id;
    long t2Id;
    long t3Id;

    public HabitizerApplication app = (HabitizerApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();


    @Before
    public void setUp() {

        // Create in-memory database
        database = Room.inMemoryDatabaseBuilder(app, HabitizerDatabase.class)
                .allowMainThreadQueries()
                .build();

        database.taskDao().clearALL();
        database.routineDao().clearAll();


        this.taskRepo = new RoomTaskRepository(database.taskDao());
        this.routineRepo = new RoomRoutineRepository(database.routineDao());
        this.taskRepo.clear();
        this.routineRepo.clear();
        app.setDataSource(taskRepo, routineRepo);
        RoutineEntity routine = new RoutineEntity(1, 30, "Morning");
        routineId = database.routineDao().insert(routine);
        TaskEntity task1 = new TaskEntity(1, "t1", 0);
        TaskEntity task2 = new TaskEntity(1, "t2", 1);
        TaskEntity task3 = new TaskEntity(1, "t3", 2);
        t1Id = database.taskDao().append(task1);
        t2Id = database.taskDao().append(task2);
        t3Id = database.taskDao().append(task3);

        ActivityScenario.launch(MainActivity.class);
    }

    @Test
    public void testTastReorder() {

        //Given
        //Start order is t1, t2, t3
        onData(anything())
                .inAdapterView(withId(R.id.routine_view))
                .atPosition(0)
                .perform(click());

        //When
        //Order now is t1, t3, t2
        onData(anything())
                .inAdapterView(withId(R.id.task_list_view))
                .atPosition(1)
                .onChildView(withId(R.id.move_down_arrow))
                .perform(click());

        //Then
        onData(anything())
                .inAdapterView(withId(R.id.task_list_view))
                .atPosition(0)
                .onChildView(withId(R.id.taskTitle))
                .check(matches(withText("t1")));
        onData(anything())
                .inAdapterView(withId(R.id.task_list_view))
                .atPosition(1)
                .onChildView(withId(R.id.taskTitle))
                .check(matches(withText("t3")));
        onData(anything())
                .inAdapterView(withId(R.id.task_list_view))
                .atPosition(2)
                .onChildView(withId(R.id.taskTitle))
                .check(matches(withText("t2")));
        List<TaskEntity> actualRes = database.taskDao().findAllByRoutineId((int) routineId);
        assertEquals("t1", actualRes.get(0).taskName);
        assertEquals("t3", actualRes.get(1).taskName);
        assertEquals("t2", actualRes.get(2).taskName);


        //When
        //Order now is t1, t2, t3
        onData(anything())
                .inAdapterView(withId(R.id.task_list_view))
                .atPosition(2)
                .onChildView(withId(R.id.move_up_arrow))
                .perform(click());

        //Then
        onData(anything())
                .inAdapterView(withId(R.id.task_list_view))
                .atPosition(0)
                .onChildView(withId(R.id.taskTitle))
                .check(matches(withText("t1")));
        onData(anything())
                .inAdapterView(withId(R.id.task_list_view))
                .atPosition(1)
                .onChildView(withId(R.id.taskTitle))
                .check(matches(withText("t2")));
        onData(anything())
                .inAdapterView(withId(R.id.task_list_view))
                .atPosition(2)
                .onChildView(withId(R.id.taskTitle))
                .check(matches(withText("t3")));
        List<TaskEntity> actualRes1 = database.taskDao().findAllByRoutineId((int) routineId);
        assertEquals("t1", actualRes1.get(0).taskName);
        assertEquals("t2", actualRes1.get(1).taskName);
        assertEquals("t3", actualRes1.get(2).taskName);
    }
}
