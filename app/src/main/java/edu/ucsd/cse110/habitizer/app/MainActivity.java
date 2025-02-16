package edu.ucsd.cse110.habitizer.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import edu.ucsd.cse110.habitizer.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.habitizer.app.ui.home.HomePage;
import edu.ucsd.cse110.habitizer.app.ui.routine.RoutineFragment;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding view;
    boolean homeScreen = true;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_title);

        //setContentView(R.layout.homepage_view);
        this.view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());

//        Initializing 2 mock routines for MS1
//        Routine routine1 = new Routine(30, "Morning Routine");
//        Routine routine2 = new Routine(45, "Exercise Routine");
        ArrayList<Routine> routineList = new ArrayList<>();
//        routineList.add(routine1);
//        routineList.add(routine2);

        routineList.add(InMemoryDataSource.MORNING_ROUTINE);
        routineList.add(InMemoryDataSource.EVENING_ROUTINE);

        ListView routineView = findViewById(R.id.routine_view);

        ArrayAdapter<Routine> adapter = new ArrayAdapter<Routine>(
                this,
                R.layout.routine_view,
                routineList
        ) {
            @NonNull
            @SuppressLint("SetTextI18n")
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(R.layout.routine_view, parent, false);
                }

                TextView titleView = convertView.findViewById(R.id.RoutineTitle);
                TextView timeView = convertView.findViewById(R.id.RoutineTime);

                Routine routine = getItem(position);
                assert routine != null;
                titleView.setText(routine.getTitle());
                timeView.setText(routine.getDuration() + " min");
                return convertView;
            }
        };

        routineView.setAdapter(adapter);

        routineView.setOnItemClickListener((parent, view, position, id) -> {
            Routine selectedRoutine = routineList.get(position);

            RoutineFragment routineFragment = RoutineFragment.newInstance(
                    selectedRoutine.getTitle(),
                    String.valueOf(selectedRoutine.getDuration())
            );

            routineView.setVisibility(View.GONE);
            findViewById(R.id.fragment_routine).setVisibility(View.VISIBLE);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_routine, routineFragment)
                    .commit();
            homeScreen = !homeScreen;
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        var itemId = item.getItemId();

        if (itemId == R.id.home_menu) {
            swapFragments();
        }

        return super.onOptionsItemSelected(item);
    }

    private void swapFragments() {
        if (!homeScreen) {
            // Hide the fragment container
            View fragmentContainer = findViewById(R.id.fragment_routine);
            fragmentContainer.setVisibility(View.GONE);

            // Show the routine list
            ListView routineView = findViewById(R.id.routine_view);
            routineView.setVisibility(View.VISIBLE);

            homeScreen = true;
        }
    }
}