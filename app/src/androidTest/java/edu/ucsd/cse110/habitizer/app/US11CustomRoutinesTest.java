
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
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;

public class US11CustomRoutinesTest {

    public HabitizerDatabase database;
    public TaskRepository taskRepo;
    public RoutineRepository routineRepo;
    long routineId;
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
        ActivityScenario.launch(MainActivity.class);
    }

    @Test
    public void testCustomRoutines() {

        //Given
        RoutineEntity bR = database.routineDao().find((int) routineId);
        assertEquals("Morning", bR.name);
        assertEquals(Integer.valueOf(30), bR.estimatedTime);
        assertEquals(Integer.valueOf(1), bR.id);
        assertEquals(1, database.routineDao().count());

        //When
        onView(withId(R.id.addRoutine)).perform(click());

        //Then
        assertEquals(2, database.routineDao().count());
        onView(withText("Morning")).check(matches(ViewMatchers.isDisplayed()));
        onView(withText("New Routine")).check(matches(ViewMatchers.isDisplayed()));
        List<RoutineEntity> actualRes = database.routineDao().findAll();
        assertEquals("Morning", actualRes.get(0).name);
        assertEquals("New Routine", actualRes.get(1).name);

    }


}
