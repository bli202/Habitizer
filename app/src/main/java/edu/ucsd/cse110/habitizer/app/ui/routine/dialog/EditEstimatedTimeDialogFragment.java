package edu.ucsd.cse110.habitizer.app.ui.routine.dialog;

import static edu.ucsd.cse110.habitizer.app.MainViewModel.getCurrentRoutine;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentEditTimeBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.observables.PlainMutableSubject;

public class EditEstimatedTimeDialogFragment extends DialogFragment {

    private FragmentEditTimeBinding view;

    public EditEstimatedTimeDialogFragment() {

    }

    public static EditEstimatedTimeDialogFragment newInstance() {
        var fragment = new EditEstimatedTimeDialogFragment();
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
            int newTime = Integer.parseInt(time);

            // Check for negative times
            if (newTime < 1) {
                var dialogFragment = new InvalidTimeDialogFragment();
                dialogFragment.show(getParentFragmentManager(), "InvalidTimeDialogFragment");
                dialog.dismiss();
                return;
            }
            // Get current routine
            Routine currentRoutine = getCurrentRoutine().getValue();
            assert currentRoutine != null;
            var modelOwner = requireActivity();
            var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
            var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
            var activityModel = modelProvider.get(MainViewModel.class);
            activityModel.setCurrentRoutineEstimatedTime(newTime);

            // Update the Subject with the modified routine
            ((PlainMutableSubject<Routine>) getCurrentRoutine()).setValue(currentRoutine);

        } catch (Exception e) {
            var dialogFragment = new InvalidTimeDialogFragment();
            dialogFragment.show(getParentFragmentManager(), "InvalidTimeDialogFragment");
            dialog.dismiss();
            return;
        }
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