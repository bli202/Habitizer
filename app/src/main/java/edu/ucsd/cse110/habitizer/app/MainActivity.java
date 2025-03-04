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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.habitizer.app.databinding.RoutinelistItemBinding;
import edu.ucsd.cse110.habitizer.app.databinding.TasklistItemBinding;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.AddRoutineDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.routine.TaskFragment;
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
        
        this.view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
        
        ArrayList<Routine> routineList = new ArrayList<>();
        
        routineList.add(InMemoryDataSource.MORNING_ROUTINE);
        routineList.add(InMemoryDataSource.EVENING_ROUTINE);
        
        ListView routineView = findViewById(R.id.routine_view);
        Button addRoutine = findViewById(R.id.addRoutine);
        
        ArrayAdapter<Routine> adapter = new ArrayAdapter<>(
                this,
                R.layout.routinelist_item,
                routineList
        ) {
            @NonNull
            @SuppressLint("SetTextI18n")
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(R.layout.routinelist_item, parent, false);
                }
                
                TextView titleView = convertView.findViewById(R.id.RoutineTitle);
                TextView timeView = convertView.findViewById(R.id.RoutineTime);
                
                Routine routine = getItem(position);
                assert routine != null;
                titleView.setText(routine.getTitle());
                timeView.setText(routine.getDuration() + " min");
                
                ImageButton deleteButton = (ImageButton) convertView.findViewById(R.id.deleteRoutineButton);
                
                // TODO
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        notifyDataSetChanged();
                        //implementation
                        Log.d("MainActivity", "Delete Button CLicked");
                    }
                });
                
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Routine selectedRoutine = getItem(position);
                        Log.d("MainActivity", "Selected Routine: " + selectedRoutine);
                        
                        // FIRST ROUTINE CLICKED SETS THE TASK VIEW
                        MainViewModel.switchRoutine(selectedRoutine);
                        
                        TaskFragment taskFragment = TaskFragment.newInstance();
                        
                        routineView.setVisibility(View.GONE);
                        addRoutine.setVisibility(View.INVISIBLE);
                        findViewById(R.id.fragment_routine).setVisibility(View.VISIBLE);
                        
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_routine, taskFragment)
                                .addToBackStack(null) // for back button
                                .commit();
                        homeScreen = !homeScreen;
                    }
                });
                return convertView;
            }
        };
        
        // Keep track of routine time changes
        var modelOwner = this;
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        var activityModel = modelProvider.get(MainViewModel.class);
        
        activityModel.getCurRoutine().observe(routine -> {
            for (int i = 0; i < routineList.size(); i++) {
                if (routineList.get(i).getId() == (routine.getId())) {
                    routineList.set(i, routine);
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        });
        
        addRoutine.setOnClickListener(x -> {
            Log.d("MainActivity", "Added New Routine");
            var routine = new Routine(0, "New Routine");
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
            Log.d("MainActivity", "Home Button Pushed");
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
