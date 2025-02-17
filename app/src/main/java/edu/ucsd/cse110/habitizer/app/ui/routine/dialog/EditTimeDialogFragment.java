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

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentDialogAddTaskBinding;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentEditTimeBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class EditTimeDialogFragment extends DialogFragment {

    private FragmentEditTimeBinding view;
    private MainViewModel activityModel;


    public EditTimeDialogFragment() {

    }

    public static EditTimeDialogFragment newInstance() {
        var fragment = new EditTimeDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentEditTimeBinding.inflate(getLayoutInflater());

        return new AlertDialog.Builder(getActivity())
                .setTitle("Edit Estimated Routine Time")
                .setMessage("Please enter the estimated time for this routine!")
                .setView(view.getRoot())
                .setPositiveButton("Confirm", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        var time = view.editTime.getText().toString();
        try {
            try {
                int t = Integer.parseInt(time);
                activityModel.getCurRoutine().getValue().setEstimatedTime(t);
                Log.d("EditTimeDialogFragment", "Estimated Time: " + activityModel.getCurRoutine().getValue().getEstimatedTime());
            } catch (Exception e) {
                    var dialogFragment = new InvalidTimeDialogFragment();
                    dialogFragment.show(getParentFragmentManager(), "InvalidTimeDialogFragment");
                    dialog.dismiss();
                    return;
            }
        } catch (Exception e) {
            Log.e("EditTaskDialogFragment", "Exception while checking task list", e);
        }
        dialog.dismiss();
    }

    private void onNegativeButtonClick(DialogInterface dialog, int which) {
        dialog.cancel();
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
