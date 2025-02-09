package edu.ucsd.cse110.habitizer.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.viewmodel.ViewModelInitializer;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class MainViewModel extends ViewModel {

    private static final String LOG_TAG = "MainViewModel";

    // Domain state ("true Model" state)
    //private final Routine routine;

    // UI state


    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (HabitizerApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel();
                    });

    public MainViewModel() {

    }

}
