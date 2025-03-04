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
import edu.ucsd.cse110.habitizer.app.databinding.FragmentDialogAddRoutineBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;

public class AddRoutineDialogFragment extends DialogFragment {

    private FragmentDialogAddRoutineBinding view;
    private MainViewModel activityModel;


    public AddRoutineDialogFragment() {

    }

    public static AddRoutineDialogFragment newInstance() {
        var fragment = new AddRoutineDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentDialogAddRoutineBinding.inflate(getLayoutInflater());

        return new AlertDialog.Builder(getActivity())
                .setTitle("New Routine")
                .setMessage("Please provide routine name")
                .setView(view.getRoot())
                .setPositiveButton("Create", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        var name = view.routineNameEditText.getText().toString();

        var routine = new Routine(0, name);

        activityModel.putRoutine(routine);
        Log.d("AddRoutineDialogFragment", "called mvm putroutine on " + routine.getName());
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
