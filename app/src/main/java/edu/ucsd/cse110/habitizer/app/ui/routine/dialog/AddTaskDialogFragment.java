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

    private FragmentDialogAddTaskBinding view;
    
    public AddTaskDialogFragment() {

    }

    public static AddTaskDialogFragment newInstance() {
        var fragment = new AddTaskDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentDialogAddTaskBinding.inflate(getLayoutInflater());

        return new AlertDialog.Builder(getActivity())
                .setTitle("New Task")
                .setMessage("Please provide task name")
                .setView(view.getRoot())
                .setPositiveButton("Create", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        var name = view.taskNameEditText.getText().toString();

        var task = new Task(name);

        if (name.isEmpty()) {
            var dialogFragment = InvalidTaskDialogFragment.newInstance("");
            dialogFragment.show(getParentFragmentManager(), "InvalidTaskDialogFragment");
            dialog.dismiss();
            return;
        }

        try {
            for (Task t : Objects.requireNonNull(MainViewModel.getCurRoutine().getValue()).getTaskList()) {
                if (t.getName().equals(name)) {
                    var dialogFragment = InvalidTaskDialogFragment.newInstance("");
                    dialogFragment.show(getParentFragmentManager(), "InvalidTaskDialogFragment");
                    dialog.dismiss();
                    return;
                }
            }
        } catch (Exception e) {
            Log.e("EditTaskDialogFragment", "Exception while checking task list", e);
        }
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        var activityModel = modelProvider.get(MainViewModel.class);
        activityModel.append(task);
        dialog.dismiss();
    }

    private void onNegativeButtonClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
