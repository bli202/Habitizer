package edu.ucsd.cse110.habitizer.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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


    }

}