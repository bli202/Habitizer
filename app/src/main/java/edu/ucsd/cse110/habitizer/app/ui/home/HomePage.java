package edu.ucsd.cse110.habitizer.app.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.HomePageBinding;

public class HomePage extends Fragment {
    private MainViewModel activityModel;
    private HomePageBinding view;

    public HomePage() {
        // Required empty public constructor
    }

    public static HomePage newInstance() {
        HomePage fragment = new HomePage();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = HomePageBinding.inflate(inflater, container, false);
        setupMvp();
        return view.getRoot();
    }

    private void setupMvp() {
//        activityModel.getDisplayedText().observe(text -> view.cardText.setText(text));
//
//        view.start.setOnClickListener(v -> activityModel.flipTopCard());
//        view.nextButton.setOnClickListener(v -> activityModel.stepForward());
//        view.prevButton.setOnClickListener(v -> activityModel.stepBackward());
//        view.shuffleButton.setOnClickListener(v -> activityModel.shuffle());
    }
}
