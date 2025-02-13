package edu.ucsd.cse110.habitizer.app;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import edu.ucsd.cse110.habitizer.app.databinding.ActivityMainBinding;

//public class MainActivity extends AppCompatActivity {
//    private ActivityMainBinding view;
//    private boolean isHome = true;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setTitle(R.string.app_name);
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
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, RoutineFragment.newInstance()).commit();
//        } else {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, HomePage.newInstance()).commit();
//        }
//        isHome = !isHome;
//    }
//}

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routine_view);
        //this.view = ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(view.getRoot());


    }


}