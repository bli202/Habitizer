package edu.ucsd.cse110.habitizer.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;

//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.ViewModel;
//import androidx.lifecycle.ViewModelProvider;
//
//public class MainActivity extends AppCompatActivity {
//    private ActivityMainBinding view;
//    private boolean isHome = true;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setTitle(R.string.app_title);
//
//        // Initialize the View
//        this.view = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(view.getRoot());
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.action_bar, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        var itemId = item.getItemId();
//        if (itemId == R.id.action_bar_menu_swap_views) {
//            swapFragments();
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void swapFragments() {
//        if (isHome) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, CardListFragment.newInstance()).commit();
//        } else {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, StudyFragment.newInstance()).commit();
//        }
//        isHome = !isHome;
//    }
//}

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_view);

        // Initializing 2 mock routines for MS1
        Routine routine1 = new Routine(30, "Morning Routine");
        Routine routine2 = new Routine(45, "Exercise Routine");
        ArrayList<Routine> routineList = new ArrayList<>();
        routineList.add(routine1);
        routineList.add(routine2);

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
    }
}