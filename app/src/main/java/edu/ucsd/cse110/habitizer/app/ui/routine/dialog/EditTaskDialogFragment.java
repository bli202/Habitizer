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

import java.util.Objects;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentEditTaskBinding;

public class EditTaskDialogFragment extends DialogFragment {

    private FragmentEditTaskBinding binding;
    private MainViewModel activityModel;

    // Required empty public constructor
    public EditTaskDialogFragment() { }

    /**
     * Creates a new instance of EditTaskDialogFragment with the old task name as an argument.
     *
     * @param oldTaskName the name of the task to be edited.
     * @return a new instance of EditTaskDialogFragment.
     */
    public static EditTaskDialogFragment newInstance(String oldTaskName) {
        EditTaskDialogFragment fragment = new EditTaskDialogFragment();
        Bundle args = new Bundle();
        args.putString("oldTaskName", oldTaskName);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = FragmentEditTaskBinding.inflate(getLayoutInflater());

        // Pre-fill the edit text with the current (old) task name.
        String oldTaskName = "";
        if (getArguments() != null) {
            oldTaskName = getArguments().getString("oldTaskName", "");
        }
        binding.editTask.setText(oldTaskName);

        return new AlertDialog.Builder(getActivity())
                .setTitle("Edit Task Name")
                .setMessage("Enter new task name")
                .setView(binding.getRoot())
                .setPositiveButton("Edit", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        final String TAG = "EditTaskDialogFragment";

        String newName = binding.editTask.getText().toString().trim();

        String oldTaskName = "";
        if (getArguments() != null) {
            oldTaskName = getArguments().getString("oldTaskName", "");
        }

        try {
            Log.d(TAG, "About to check task list");

            for (Task t : activityModel.getCurrentRoutineTasks()) {
                Log.d(TAG, "Task Name: " + t.getName());
                if (t.getName().equals(newName) || newName.isEmpty()) {
                    Log.d(TAG, "they r equal");
                    var dialogFragment = InvalidTaskDialogFragment.newInstance(oldTaskName);
                    dialogFragment.show(getParentFragmentManager(), "InvalidTaskDialogFragment");
                    dialog.dismiss();
                    return;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception while checking task list", e);
        }
        if (oldTaskName.isEmpty()) {
            dialog.dismiss();
            return;
        }

        // Remove the old task and append the new task via the view model.
        activityModel.editTaskInCurrentRoutine(oldTaskName, newName);
        dialog.dismiss();
    }

    private void onNegativeButtonClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the MainViewModel.
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }
}
