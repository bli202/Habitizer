package edu.ucsd.cse110.habitizer.app;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;


import org.junit.Before;
import org.junit.Test;


import android.graphics.Paint;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onIdle;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
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
import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;



public class AutoEndRoutineTest {

    public HabitizerDatabase database;
    long routineId;
    public TaskRepository taskRepo;
    public RoutineRepository routineRepo;

    @Before
    public void setUp() {

        HabitizerApplication app = (HabitizerApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();

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
        RoutineEntity routine = new RoutineEntity(1, 30, "Morning");
        routineId = database.routineDao().insert(routine);
        // Launch the main activity
        ActivityScenario.launch(MainActivity.class);
    }

    @Test
    public void testAutoEnd() {

        // Given I am in the routine list view
        //And its non empty
        //And the routine is started
        onData(anything())
                .inAdapterView(withId(R.id.routine_view))
                .atPosition(0) // Assuming it's the first item in the list
                .perform(click());

        onView(withId(R.id.add_task_button)).perform(click());
        onView(withId(R.id.task_name_edit_text))
                .perform(typeText("Brush1"), ViewActions.closeSoftKeyboard());
        onView(withText("Create")).perform(click());
        SystemClock.sleep(1000);
        onView(withId(R.id.start_routine_button)).perform(click());


        //When I click to complete the last task
        onData(anything())
                .inAdapterView(withId(R.id.task_list_view))
                .atPosition(0)
                .perform(click());

        //Then the routine should be stopped
        RoutineEntity br = database.routineDao().find((int)routineId);
        assertFalse(br.ongoing);
    }
}
