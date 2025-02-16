package edu.ucsd.cse110.habitizer.app.ui.routine.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentDialogAddTaskBinding;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentDuplicateTaskBinding;
import edu.ucsd.cse110.habitizer.app.databinding.TaskViewBinding;
import edu.ucsd.cse110.habitizer.app.ui.routine.RoutineAdapter;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class NoDuplicateDialogFragment extends DialogFragment {

    private FragmentDuplicateTaskBinding view;
    private MainViewModel activityModel;


    public NoDuplicateDialogFragment() {

    }

    public static NoDuplicateDialogFragment newInstance(String oldTaskName) {
        var fragment = new NoDuplicateDialogFragment();
        Bundle args = new Bundle();
        args.putString("oldTaskName", oldTaskName);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentDuplicateTaskBinding.inflate(getLayoutInflater());

        return new AlertDialog.Builder(getActivity())
                .setTitle("Task name is the same as another task")
                .setMessage("Please provide a new task name")
                .setView(view.getRoot())
                .setPositiveButton("Continue", this::onPositiveButtonClick)
                .create();
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        String oldTaskName = "";
        if (getArguments() != null) {
            oldTaskName = getArguments().getString("oldTaskName", "");
        }
        if (oldTaskName.equals("")) {
            var dialogFragment = new AddTaskDialogFragment();
            dialogFragment.show(getParentFragmentManager(), "AddTaskDialogFragment");
        }
        else {
            var EditTaskdialogFragment = EditTaskDialogFragment.newInstance(oldTaskName);
            EditTaskdialogFragment.show(getParentFragmentManager(), "EditCardDialogFragment");
        }
        dialog.dismiss();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the Model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

}
