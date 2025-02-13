package edu.ucsd.cse110.habitizer.app.ui.routine;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.RoutineViewBinding;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.AddTaskDialogFragment;

public class RoutineFragment extends Fragment {


    private MainViewModel activityModel;
    private RoutineViewBinding view;

    public RoutineFragment() {
        // Required empty public constructor
    }

    public static RoutineFragment newInstance() {
        RoutineFragment fragment = new RoutineFragment();
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
        view = RoutineViewBinding.inflate(inflater, container, false);

        view.addTaskButton.setOnClickListener(v -> {
            var dialogFragment = AddTaskDialogFragment.newInstance();
            dialogFragment.show(getChildFragmentManager(), "AddTaskDialogFragment");
        });

        return view.getRoot();


    }

}



