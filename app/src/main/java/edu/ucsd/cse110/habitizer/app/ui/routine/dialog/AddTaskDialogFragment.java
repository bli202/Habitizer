package edu.ucsd.cse110.habitizer.app.ui.routine.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentDialogAddTaskBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class AddTaskDialogFragment extends DialogFragment {

    private FragmentDialogAddTaskBinding binding;
    private MainViewModel activityModel;

    private static final String TAG = "AddTaskDialogFragment";

    // Required empty public constructor
    public AddTaskDialogFragment() {}

    /**
     * Creates a new instance of AddTaskDialogFragment.
     *
     * @return a new instance of AddTaskDialogFragment.
     */
    public static AddTaskDialogFragment newInstance() {
        return new AddTaskDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = FragmentDialogAddTaskBinding.inflate(getLayoutInflater());

        return new AlertDialog.Builder(getActivity())
                .setTitle("New Task")
                .setMessage("Please provide a task name")
                .setView(binding.getRoot())
                .setPositiveButton("Create", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        // Get the task name from the EditText.
        String taskName = binding.taskNameEditText.getText().toString().trim();

        // Validate task name.
        if (taskName.isEmpty()) {
            Log.d(TAG, "Invalid task name: empty");
            var dialogFragment = InvalidTaskDialogFragment.newInstance("");
            dialogFragment.show(getParentFragmentManager(), "InvalidTaskDialogFragment");
            dialog.dismiss();
            return;
        }

        // Ensure task does not already exist.
        try {
            for (Task t : Objects.requireNonNull(activityModel.getCurRoutine().getValue()).getTaskList()) {
                if (t.getName().equals(taskName)) {
                    Log.d(TAG, "Task with this name already exists.");
                    var dialogFragment = InvalidTaskDialogFragment.newInstance("");
                    dialogFragment.show(getParentFragmentManager(), "InvalidTaskDialogFragment");
                    dialog.dismiss();
                    return;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception while checking task list", e);
        }

        // Create a new task and add it via ViewModel.
        Task newTask = new Task(taskName);
        activityModel.append(newTask);
        dialog.dismiss();
    }

    private void onNegativeButtonClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the ViewModel.
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }
}
