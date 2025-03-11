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
import static androidx.test.espresso.matcher.ViewMatchers.withSubstring;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.os.SystemClock;

import edu.ucsd.cse110.habitizer.app.data.db.HabitizerDatabase;
import edu.ucsd.cse110.habitizer.app.data.db.RoomRoutineRepository;
import edu.ucsd.cse110.habitizer.app.data.db.RoomTaskRepository;
import edu.ucsd.cse110.habitizer.app.data.db.RoutineEntity;
import edu.ucsd.cse110.habitizer.app.data.db.TaskEntity;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;

public class US14AsyncTimerBDD1Test {

    public HabitizerDatabase database;
    public TaskRepository taskRepo;
    public RoutineRepository routineRepo;
    long routineId;
    long taskId;

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
        taskId = database.taskDao().append(task1);
        ActivityScenario.launch(MainActivity.class);
    }

    @Test
    public void testAsyncTimer() {


        //Given
        onData(anything())
                .inAdapterView(withId(R.id.routine_view))
                .atPosition(0)
                .perform(click());

        //When
        onView(withId(R.id.start_routine_button)).perform(click());

        //Then
        onView(withId(R.id.actual_time))
                .check(matches(withSubstring("Cumulative: 0m")));

        //When
        SystemClock.sleep(35000);

        //Then
        onView(withId(R.id.actual_time))
                .check(matches(withSubstring("Cumulative: 0m")));

        //When
        SystemClock.sleep(25000);

        //Then
        onView(withId(R.id.actual_time))
                .check(matches(withSubstring("Cumulative: 1m")));

    }

}
