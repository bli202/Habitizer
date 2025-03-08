package edu.ucsd.cse110.habitizer.app.ui.routine.dialog;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;

public class RoutineAdapter extends ArrayAdapter<Routine> {
    private final String TAG = "RoutineAdapter";
    public RoutineAdapter(@NonNull Context context, List<Routine> routineList) {
        super(context, 0, routineList);
    }
}
