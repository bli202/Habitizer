package edu.ucsd.cse110.habitizer.app.ui.routine.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import edu.ucsd.cse110.habitizer.app.databinding.FragmentInvalidTimeBinding;

public class InvalidTimeDialogFragment extends DialogFragment {
    
    
    public InvalidTimeDialogFragment() {

    }

    public static InvalidTimeDialogFragment newInstance() {
        var fragment = new InvalidTimeDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        FragmentInvalidTimeBinding view = FragmentInvalidTimeBinding.inflate(getLayoutInflater());

        return new AlertDialog.Builder(getActivity())
                .setTitle("Invalid estimated time!")
                .setMessage("Please provide a new estimated routine time")
                .setView(view.getRoot())
                .setPositiveButton("Continue", this::onPositiveButtonClick)
                .create();
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        var dialogFragment = new EditEstimatedTimeDialogFragment();
        dialogFragment.show(getParentFragmentManager(), "EditTimeDialogFragment");
        dialog.dismiss();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
