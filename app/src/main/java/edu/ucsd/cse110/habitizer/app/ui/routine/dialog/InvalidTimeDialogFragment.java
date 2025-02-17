package edu.ucsd.cse110.habitizer.app.ui.routine.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentDuplicateTaskBinding;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentInvalidTimeBinding;

public class InvalidTimeDialogFragment extends DialogFragment {

    private FragmentInvalidTimeBinding view;
    private MainViewModel activityModel;


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
        this.view = FragmentInvalidTimeBinding.inflate(getLayoutInflater());

        return new AlertDialog.Builder(getActivity())
                .setTitle("Invalid estimated time!")
                .setMessage("Please provide a new estimated routine time")
                .setView(view.getRoot())
                .setPositiveButton("Continue", this::onPositiveButtonClick)
                .create();
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        var dialogFragment = new EditTimeDialogFragment();
        dialogFragment.show(getParentFragmentManager(), "EditTimeDialogFragment");
        dialog.dismiss();
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
