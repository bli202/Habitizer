package edu.ucsd.cse110.habitizer.app.ui.routine.dialog;

import static edu.ucsd.cse110.habitizer.app.MainViewModel.getCurRoutine;

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
            // Get current routine
            Routine currentRoutine = getCurRoutine().getValue();
            // Update the time
            assert currentRoutine != null;
            var modelOwner = requireActivity();
            var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
            var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
            var activityModel = modelProvider.get(MainViewModel.class);
            activityModel.setCurRoutineEstimatedTime(newTime);
            // Update the Subject with the modified routine
            ((PlainMutableSubject<Routine>) getCurRoutine()).setValue(currentRoutine);
            
            Log.d("EditTimeDialogFragment", "Estimated Time: " + currentRoutine.getEstimatedTime());
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
