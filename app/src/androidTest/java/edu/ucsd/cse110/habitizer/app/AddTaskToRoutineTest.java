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

public class AddTaskToRoutineTest {

    public HabitizerDatabase database;
    public TaskRepository taskRepo;
    public RoutineRepository routineRepo;

    @Before
    public void setUp() {
        HabitizerApplication app = (HabitizerApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();

        // Create in-memory database
        database = Room.inMemoryDatabaseBuilder(app, HabitizerDatabase.class)
                .allowMainThreadQueries()
                .build();

        this.taskRepo = new RoomTaskRepository(database.taskDao());
        this.routineRepo = new RoomRoutineRepository(database.routineDao());

        app.setDataSource(taskRepo, routineRepo);

        // Launch the main activity
        ActivityScenario.launch(MainActivity.class);
    }

    @Test
    public void testAddTaskToRoutine() {


        //Given I am in the routine list view
        RoutineEntity routine = new RoutineEntity(0, 30, "Morning Routine");
        database.routineDao().insertTimer(new CustomTimerEntity(0, 0, 0, false, 0, 0));
        long routineId = database.routineDao().insert(routine);
        ActivityScenario.launch(MainActivity.class);
        onData(anything())
                .inAdapterView(withId(R.id.routine_view))
                .atPosition(0) // Assuming it's the first item in the list
                .perform(click());

        //When I add a non duplicate task
        onView(withId(R.id.add_task_button)).perform(click());
        onView(withId(R.id.task_name_edit_text))
                .perform(typeText("Brush Teeth"), ViewActions.closeSoftKeyboard());

        onView(withText("Create")).perform(click());
        SystemClock.sleep(1000);

        //Then the task should be displayed
        List<TaskEntity> tasks = database.taskDao().findAllByRoutineId((int) routineId);
        assertEquals(1, tasks.size());
        assert tasks.get(0).taskName.equals("Brush Teeth");
        onView(withText("Brush Teeth")).check(matches(ViewMatchers.isDisplayed()));

    }


}
