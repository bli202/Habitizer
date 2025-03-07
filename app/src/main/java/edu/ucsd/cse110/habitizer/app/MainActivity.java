package edu.ucsd.cse110.habitizer.app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import edu.ucsd.cse110.habitizer.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.habitizer.app.ui.routine.TaskFragment;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.InvalidDeleteRoutineDialogFragment;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;

public class MainActivity extends AppCompatActivity {
    
    private final String TAG = "MainActivity";
    
    boolean homeScreen = true;
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return false;
    }
    
    private void toggleHomeScreen() {
        this.homeScreen = !homeScreen;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_title);
        
        ActivityMainBinding view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
        
        ListView routineView = findViewById(R.id.routine_view);
        Button addRoutine = findViewById(R.id.addRoutine);
        
        // Keep track of routine time changes and new routines
        var modelOwner = this;
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        var activityModel = modelProvider.get(MainViewModel.class);
        
        ArrayList<Routine> routineList = (ArrayList<Routine>) activityModel.getRoutines();
        
        RoutineAdapter adapter = new RoutineAdapter(this,
                routineList,
                routine -> {
                    Log.d(TAG, "Delete Button CLicked");
                    if (routineList.size() == 1) {
                        var dialogFragment = new InvalidDeleteRoutineDialogFragment();
                        dialogFragment.show(getSupportFragmentManager(), "InvalidDeleteRoutineDialogFragment");
                    } else {
                        activityModel.removeRoutine(routine);
                        routineList.remove(routine);
                    }
                },
                view2 -> {
                    TaskFragment taskFragment = TaskFragment.newInstance();
                    
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_routine, taskFragment)
                            .addToBackStack(null)
                            .commit();
                    
                    routineView.setVisibility(View.GONE);
                    addRoutine.setVisibility(View.INVISIBLE);
                    findViewById(R.id.fragment_routine).setVisibility(View.VISIBLE);
                    toggleHomeScreen();
                });
        
        MainViewModel.getCurRoutine().observe(routine -> {
            for (int i = 0; i < routineList.size(); i++) {
                assert routine != null;
                if (routineList.get(i).getId() == (routine.getId())) {
                    routineList.set(i, routine);
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        });
        
        addRoutine.setOnClickListener(x -> {
            Log.d(TAG, "Added New Routine");
            var routine = new Routine((activityModel.getRoutines()
                    .get(activityModel.getRoutines().size() - 1))
                    .getId() + 1, 0, "New Routine");
            activityModel.putRoutine(routine);
            routineList.add(routine);
            adapter.notifyDataSetChanged();
        });
        
        routineView.setAdapter(adapter);
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        var itemId = item.getItemId();
        
        if (itemId == R.id.home_menu) {
            swapFragments();
            Log.d(TAG, "Home Button Pushed");
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    private void swapFragments() {
        if (!homeScreen) {
            // Hide the fragment container
            View fragmentContainer = findViewById(R.id.fragment_routine);
            Button stop = findViewById(R.id.stop_routine_button);
            if (stop.getVisibility() == View.VISIBLE) {
                return;
            }
            fragmentContainer.setVisibility(View.GONE);
            
            
            // Show the routine list
            ListView routineView = findViewById(R.id.routine_view);
            routineView.setVisibility(View.VISIBLE);
            Button addRoutine = findViewById(R.id.addRoutine);
            addRoutine.setVisibility(View.VISIBLE);
            
            homeScreen = true;
        }
    }
}
