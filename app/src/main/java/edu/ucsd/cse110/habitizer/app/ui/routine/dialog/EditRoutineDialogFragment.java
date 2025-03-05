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
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentEditRoutineBinding;

public class EditRoutineDialogFragment extends DialogFragment {

    private FragmentEditRoutineBinding binding;
    private MainViewModel activityModel;

    private static final String TAG = "EditRoutineDialogFragment";

    // Required empty public constructor
    public EditRoutineDialogFragment() { }

    /**
     * Creates a new instance of EditRoutineDialogFragment with the old routine name as an argument.
     *
     * @param oldRoutineName the name of the routine to be edited.
     * @return a new instance of EditRoutineDialogFragment.
     */
    public static EditRoutineDialogFragment newInstance(String oldRoutineName) {
        EditRoutineDialogFragment fragment = new EditRoutineDialogFragment();
        Bundle args = new Bundle();
        args.putString("oldRoutineName", oldRoutineName);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = FragmentEditRoutineBinding.inflate(getLayoutInflater());

        // Pre-fill the edit text with the current (old) routine name.
        String oldRoutineName = "";
        if (getArguments() != null) {
            oldRoutineName = getArguments().getString("oldRoutineName", "");
        }
        binding.editRoutine.setText(oldRoutineName);

        return new AlertDialog.Builder(getActivity())
                .setTitle("Edit Routine Name")
                .setMessage("Enter new routine name")
                .setView(binding.getRoot())
                .setPositiveButton("Save", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        // Get the new routine name from the EditText.
        String newName = binding.editRoutine.getText().toString().trim();

        // Retrieve the old routine name from the arguments.
        String oldRoutineName = "";
        if (getArguments() != null) {
            oldRoutineName = getArguments().getString("oldRoutineName", "");
        }

        try {
            Log.d(TAG, "Checking routine list for duplicate names");
            // Check if the new routine name already exists or is empty
            if (routineNameExists(newName) || newName.isEmpty()) {
                Log.d(TAG, "Invalid routine name: duplicate or empty");
                var dialogFragment = InvalidRoutineDialogFragment.newInstance(oldRoutineName);
                dialogFragment.show(getParentFragmentManager(), "InvalidRoutineDialogFragment");
                dialog.dismiss();
                return;
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception while checking routine list", e);
        }

        if (oldRoutineName.isEmpty()) {
            // Should not occur if this dialog was invoked for an existing routine.
            dialog.dismiss();
            return;
        }

        // Update the routine name via the view model.
        activityModel.editRoutine(oldRoutineName, newName);
        dialog.dismiss();
    }

    private boolean routineNameExists(String name) {
        // Check if a routine with the given name already exists
        try {
            for (Routine r : Objects.requireNonNull(activityModel.getRoutines())) {
                if (r.getName().equals(name)) {
                    return true;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error checking for routine name existence", e);
        }
        return false;
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