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


public class DeleteTaskDialogFragment extends DialogFragment {
    
    private static final String tName = "taskName";

    public DeleteTaskDialogFragment() {
        // Required empty public constructor
    }


    public static DeleteTaskDialogFragment newInstance(String taskName) {
        var fragment = new DeleteTaskDialogFragment();
        Bundle args = new Bundle();
        args.putString(tName, taskName);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Delete", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        assert getArguments() != null;
        String taskNameToDelete = getArguments().getString(tName);
        if (taskNameToDelete != null) {
            Log.d("DeleteTaskDialogFragment", "Task being deleted: " + getArguments().getString(tName));
            // Initialize the Model
            var modelOwner = requireActivity();
            var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
            var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
            var activityModel = modelProvider.get(MainViewModel.class);
            activityModel.removeTaskByName(taskNameToDelete);
        }
        dialog.dismiss();
    }
    private void onNegativeButtonClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}