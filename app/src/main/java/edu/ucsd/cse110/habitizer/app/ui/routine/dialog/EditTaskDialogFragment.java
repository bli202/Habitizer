package edu.ucsd.cse110.habitizer.app.ui.routine.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.content.DialogInterface;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentEditTaskBinding;


public class EditTaskDialogFragment extends DialogFragment {

    private FragmentEditTaskBinding view;
    private MainViewModel activityModel;

    private static final String ARG_TASK_NAME = "task_name";
    private String taskname;

    public EditTaskDialogFragment() {
        // Required empty public constructor
    }


    public static EditTaskDialogFragment newInstance(String taskname) {
        var fragment = new EditTaskDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        args.putString(ARG_TASK_NAME, taskname);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentEditTaskBinding.inflate(getLayoutInflater());

        return new AlertDialog.Builder(getActivity())
                .setTitle("Edit Task Name")
                .setMessage("Enter new task name")
                .setView(view.getRoot())
                .setPositiveButton("Edit", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        var name = view.editTask.getText().toString();
        activityModel.edit(taskname, name);
        dialog.dismiss();
    }

    private void onNegativeButtonClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.taskname = requireArguments().getString(ARG_TASK_NAME);

        // Initialize the Model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }
}