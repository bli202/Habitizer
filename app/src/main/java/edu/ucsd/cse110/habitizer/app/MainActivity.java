package edu.ucsd.cse110.habitizer.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.habitizer.app.ui.routine.RoutineFragment;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding view;
    private boolean homeScreen = true;
    private MainViewModel activityModel;
    private ArrayAdapter<Routine> adapter;
    private List<Routine> routineList = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_title);

        this.view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());

        // Initialize ViewModel
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(this, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        ListView routineView = findViewById(R.id.routine_view);

        // Setup adapter for routines
        this.adapter = new ArrayAdapter<>(this, R.layout.routine_view, routineList) {
            @NonNull
            @SuppressLint("SetTextI18n")
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.routine_view, parent, false);
                }

                TextView titleView = convertView.findViewById(R.id.RoutineTitle);
                TextView timeView = convertView.findViewById(R.id.RoutineTime);

                Routine routine = getItem(position);
                if (routine != null) {
                    titleView.setText(routine.getTitle());
                    timeView.setText(routine.getDuration() + " min");
                }
                return convertView;
            }
        };

        routineView.setAdapter(adapter);

        // Observe routines from repository via MainViewModel
        activityModel.getRoutineRepository().getRoutineList().observe(routines -> {
            if (routines == null) return;
            routineList.clear();
            routineList.addAll(routines);
            adapter.notifyDataSetChanged();
        });

        // Observe changes to the currently selected routine
        activityModel.getCurRoutine().observe(routine -> {
            if (routine == null) return;
            for (int i = 0; i < routineList.size(); i++) {
                if (routineList.get(i).getName().equals(routine.getName())) {
                    routineList.set(i, routine);
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        });

        // Handle routine selection
        routineView.setOnItemClickListener((parent, view, position, id) -> {
            Routine selectedRoutine = routineList.get(position);
            Log.d("MainActivity", "Selected Routine: " + selectedRoutine);

            // Update selected routine in ViewModel
            MainViewModel.switchRoutine(selectedRoutine);

            // Show RoutineFragment
            RoutineFragment routineFragment = RoutineFragment.newInstance();
            routineView.setVisibility(View.GONE);
            findViewById(R.id.fragment_routine).setVisibility(View.VISIBLE);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_routine, routineFragment)
                    .addToBackStack(null) // Enables back navigation
                    .commit();

            homeScreen = false;
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home_menu) {
            swapFragments();
        }
        return super.onOptionsItemSelected(item);
    }

    private void swapFragments() {
        if (!homeScreen) {
            View fragmentContainer = findViewById(R.id.fragment_routine);
            fragmentContainer.setVisibility(View.GONE);

            // Show the routine list
            ListView routineView = findViewById(R.id.routine_view);
            routineView.setVisibility(View.VISIBLE);

            homeScreen = true;
        }
    }
}
