package edu.ucsd.cse110.habitizer.app.ui.routine.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class InvalidRoutineDialogFragment extends DialogFragment {

    private static final String TAG = "InvalidRoutineDialogFragment";

    // Required empty public constructor
    public InvalidRoutineDialogFragment() { }

    /**
     * Creates a new instance of InvalidRoutineDialogFragment with the original routine name as an argument.
     *
     * @param originalRoutineName the original name of the routine attempted to be edited.
     * @return a new instance of InvalidRoutineDialogFragment.
     */
    public static InvalidRoutineDialogFragment newInstance(String originalRoutineName) {
        InvalidRoutineDialogFragment fragment = new InvalidRoutineDialogFragment();
        Bundle args = new Bundle();
        args.putString("originalRoutineName", originalRoutineName);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String originalRoutineName;
        if (getArguments() != null) {
            originalRoutineName = getArguments().getString("originalRoutineName", "");
        } else {
            originalRoutineName = "";
        }

        return new AlertDialog.Builder(getActivity())
                .setTitle("Invalid Routine Name")
                .setMessage("Routine name cannot be empty of an existing routine.")
                .setPositiveButton("Try Again", (dialog, which) -> {
                    // Show the edit dialog again with the original name
                    var editDialogFragment = EditRoutineDialogFragment.newInstance(originalRoutineName);
                    editDialogFragment.show(getParentFragmentManager(), "EditRoutineDialogFragment");
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create();
    }
}