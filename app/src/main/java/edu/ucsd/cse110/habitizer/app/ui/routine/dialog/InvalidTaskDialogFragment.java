package edu.ucsd.cse110.habitizer.app.ui.routine.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentDuplicateTaskBinding;

public class InvalidTaskDialogFragment extends DialogFragment {

    private FragmentDuplicateTaskBinding view;
    private MainViewModel activityModel;


    public InvalidTaskDialogFragment() {

    }

    public static InvalidTaskDialogFragment newInstance(String oldTaskName) {
        var fragment = new InvalidTaskDialogFragment();
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
                .setTitle("Invalid task name!")
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
