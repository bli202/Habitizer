package edu.ucsd.cse110.habitizer.app;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;


import android.os.SystemClock;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.CoreMatchers.anything;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.data.db.CustomTimerEntity;
import edu.ucsd.cse110.habitizer.app.data.db.HabitizerDatabase;
import edu.ucsd.cse110.habitizer.app.data.db.RoomRoutineRepository;
import edu.ucsd.cse110.habitizer.app.data.db.RoomTaskRepository;
import edu.ucsd.cse110.habitizer.app.data.db.TaskEntity;
import edu.ucsd.cse110.habitizer.app.data.db.RoutineEntity;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;

public class PersistanceTest {

    public HabitizerDatabase database;
    public TaskRepository taskRepo;
    public RoutineRepository routineRepo;
    public long routineId;

    @Before
    public void setUp() {
        HabitizerApplication app = (HabitizerApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();

        // Create in-memory database
        database = Room.inMemoryDatabaseBuilder(app, HabitizerDatabase.class)
                .allowMainThreadQueries()
                .build();


        this.taskRepo = new RoomTaskRepository(database.taskDao());
        this.routineRepo = new RoomRoutineRepository(database.routineDao());

        if (taskRepo != null) {
            this.taskRepo.clear();
        }

        if (routineRepo != null) {
            this.routineRepo.clear();
        }

        app.setDataSource(taskRepo, routineRepo);
    }

    @Test
    public void testPersistance() {

        //Given I have a routine with tasks
        RoutineEntity routine = new RoutineEntity(0, 30, "Morning Routine");
        database.routineDao().insertTimer(new CustomTimerEntity(0, 0, 0, false, 0, 0));
        routineId = database.routineDao().insert(routine);
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        onData(anything())
                .inAdapterView(withId(R.id.routine_view))
                .atPosition(0) // Assuming it's the first item in the list
                .perform(click());
        onView(withId(R.id.add_task_button)).perform(click());
        onView(withId(R.id.task_name_edit_text))
                .perform(typeText("task1"), ViewActions.closeSoftKeyboard());
        onView(withText("Create")).perform(click());
        SystemClock.sleep(1000);
        onView(withId(R.id.add_task_button)).perform(click());
        onView(withId(R.id.task_name_edit_text))
                .perform(typeText("task2"), ViewActions.closeSoftKeyboard());
        onView(withText("Create")).perform(click());
        SystemClock.sleep(1000);

        //When I close and reopen the app
        scenario.close();
        scenario = ActivityScenario.launch(MainActivity.class);
        onData(anything())
                .inAdapterView(withId(R.id.routine_view))
                .atPosition(0)
                .perform(click());

        //Then the routine and tasks should persist
        List<String> tasks = List.of("task1", "task2");
        onView(withText("task1")).check(matches(ViewMatchers.isDisplayed()));
        onView(withText("task2")).check(matches(ViewMatchers.isDisplayed()));
        List<TaskEntity> taskL = database.taskDao().findAllByRoutineId((int) routineId);
        assertEquals(2, tasks.size());
        assertEquals("task1", taskL.get(0).taskName);
        assertEquals("task2", taskL.get(1).taskName);

        // Close the scenario after test completion
        scenario.close();


    }


}
