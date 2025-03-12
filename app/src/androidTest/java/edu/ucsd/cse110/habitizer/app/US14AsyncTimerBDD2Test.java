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
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSubstring;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.os.SystemClock;

import edu.ucsd.cse110.habitizer.app.data.db.CustomTimerDao;
import edu.ucsd.cse110.habitizer.app.data.db.CustomTimerEntity;
import edu.ucsd.cse110.habitizer.app.data.db.HabitizerDatabase;
import edu.ucsd.cse110.habitizer.app.data.db.RoomRoutineRepository;
import edu.ucsd.cse110.habitizer.app.data.db.RoomTaskRepository;
import edu.ucsd.cse110.habitizer.app.data.db.RoutineEntity;
import edu.ucsd.cse110.habitizer.app.data.db.TaskEntity;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;

public class US14AsyncTimerBDD2Test {

    public HabitizerDatabase database;
    public TaskRepository taskRepo;
    public RoutineRepository routineRepo;
    long routineId;
    long taskId;

    public HabitizerApplication app = (HabitizerApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();

    @Before
    public void setUp() {

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
        TaskEntity task1 = new TaskEntity(1, "t11", 0);
        taskId = database.taskDao().append(task1);
        database.customTimerDao().insert(new CustomTimerEntity(0,
                0,false, 0, 0));
        ActivityScenario.launch(MainActivity.class);
    }


    @Test
    public void testAsyncAdvanceTime() {

        //Given that I am on the Routines screen
        onData(anything())
                .inAdapterView(withId(R.id.routine_view))
                .atPosition(0)
                .perform(click());


        //When I tap a routine to start it
        onView(withId(R.id.start_routine_button)).perform(click());

        //Then I'm shown the routine's Routine screen
        //And the the routine's elapsed time displays "0m"
        onView(withId(R.id.actual_time))
                .check(matches(withSubstring("Cumulative: 0m")));
        onView(withId(R.id.pause_timer_button)).check(matches(isDisplayed()));

        //When I tap the Stop button (Story 3b)
        onView(withId((R.id.pause_timer_button))).perform(click());

        //Then the Stop button turns into an Advance button
        onView(withId(R.id.restart_timer_button)).check(matches(isDisplayed()));

        //(and, internally, the routine timer has been stopped)
        CustomTimerEntity timer = database.customTimerDao().findTimer();
        SystemClock.sleep(1000);
        assertFalse(timer.ongoing);

        //When I tap the Advance button 2 times (i.e., advance time 30 seconds)
        onView(withId(R.id.fast_forward_timer_button)).perform(click());
        onView(withId(R.id.fast_forward_timer_button)).perform(click());

        //Then the routine's elapsed time (still) displays "0m"
        onView(withId(R.id.actual_time))
                .check(matches(withSubstring("Cumulative: 0m")));

        //When I tap the Advance button 2 (more) times (i.e., 60 seconds total)
        onView(withId(R.id.fast_forward_timer_button)).perform(click());
        onView(withId(R.id.fast_forward_timer_button)).perform(click());
        SystemClock.sleep(1000);

        //Then the routine's elapsed time displays "1m"
        onView(withId(R.id.actual_time))
                .check(matches(withSubstring("Cumulative: 1m")));

    }
}
