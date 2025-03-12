package edu.ucsd.cse110.habitizer.app;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.platform.app.InstrumentationRegistry;


import org.junit.Before;
import org.junit.Test;


import android.os.SystemClock;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onIdle;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import edu.ucsd.cse110.habitizer.app.data.db.CustomTimerEntity;
import edu.ucsd.cse110.habitizer.app.data.db.RoomCustomTimerRepository;
import edu.ucsd.cse110.habitizer.app.data.db.HabitizerDatabase;
import edu.ucsd.cse110.habitizer.app.data.db.RoomRoutineRepository;
import edu.ucsd.cse110.habitizer.app.data.db.RoomTaskRepository;
import edu.ucsd.cse110.habitizer.app.data.db.RoutineEntity;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;

public class RoutinePauseAndRestartTest {

    public HabitizerDatabase database;
    long routineId;
    public TaskRepository taskRepo;
    public RoutineRepository routineRepo;
    public RoomCustomTimerRepository customTimerRepo;

    @Before
    public void setUp() {

        HabitizerApplication app = (HabitizerApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();

        // Create a new in-memory database with foreign key constraints
        database = Room.inMemoryDatabaseBuilder(app, HabitizerDatabase.class)
                .allowMainThreadQueries()
                .build();

        if (taskRepo != null) {
            this.taskRepo.clear();
        }

        if (routineRepo != null) {
            this.routineRepo.clear();
        }

        if (customTimerRepo != null) {
            this.customTimerRepo.clear();
        }
        this.taskRepo = new RoomTaskRepository(database.taskDao());
        this.routineRepo = new RoomRoutineRepository(database.routineDao());
        this.customTimerRepo = new RoomCustomTimerRepository(database.customTimerDao());

        app.setDataSource(taskRepo, routineRepo, customTimerRepo);
        RoutineEntity routine = new RoutineEntity(0, 30, "Morning Routine");
        routineId = database.routineDao().insert(routine);
        CustomTimerEntity cte = new CustomTimerEntity(0, 0, false, 0, 0);
        // Launch the main activity
        ActivityScenario.launch(MainActivity.class);
    }

    @Test
    public void testRoutinePause() {

        // Given I am in the routine list view
        //And its non empty
        onData(anything())
                .inAdapterView(withId(R.id.routine_view))
                .atPosition(0) // Assuming it's the first item in the list
                .perform(click());

        onView(withId(R.id.add_task_button)).perform(click());
        onView(withId(R.id.task_name_edit_text))
                .perform(typeText("Brush"), ViewActions.closeSoftKeyboard());

        //When I start the routine
        //And stop it
        onView(withText("Create")).perform(click());
        SystemClock.sleep(1000);

        onView(withId(R.id.start_routine_button)).perform(click());
        RoutineEntity br = database.routineDao().find((int)routineId);
        CustomTimerEntity cte = database.customTimerDao().findTimer();
        //Then when the timer is paused, the routine is ongoing but the timer should not count up

        onView(withId(R.id.pause_timer_button)).check(matches(isDisplayed())).perform(click());
        assertTrue(br.ongoing);
        long beforeTime = customTimerRepo.getCumulativeTime();
        SystemClock.sleep(1000);
        assertEquals(beforeTime, customTimerRepo.getCumulativeTime());

        //Then when the timer is resumed, the routine is still ongoing but the timer should continue

        onView(withId(R.id.restart_timer_button)).check(matches(isDisplayed())).perform(click());
        assertTrue(br.ongoing);
        long beforeTime2 = customTimerRepo.getCumulativeTime();
        SystemClock.sleep(1000);
        assertNotEquals(beforeTime2, customTimerRepo.getCumulativeTime());
    }
}

