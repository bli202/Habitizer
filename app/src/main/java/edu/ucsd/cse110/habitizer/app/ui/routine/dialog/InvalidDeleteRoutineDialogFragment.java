package edu.ucsd.cse110.habitizer.app.ui.routine.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import edu.ucsd.cse110.habitizer.app.databinding.FragmentInvalidDeleteRoutineBinding;


public class InvalidDeleteRoutineDialogFragment extends DialogFragment {
    
    
    public InvalidDeleteRoutineDialogFragment() {
    
    }
    
    public static InvalidDeleteRoutineDialogFragment newInstance() {
        var fragment = new InvalidDeleteRoutineDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        FragmentInvalidDeleteRoutineBinding view = FragmentInvalidDeleteRoutineBinding.inflate(getLayoutInflater());
        
        return new AlertDialog.Builder(getActivity())
                .setTitle("You cannot remove the last routine")
                .setMessage("Please add a new routine if you'd like to delete this one!")
                .setView(view.getRoot())
                .setPositiveButton("Continue", this::onPositiveButtonClick)
                .create();
    }
    
    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        dialog.dismiss();
    }
    
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
}
