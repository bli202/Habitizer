package edu.ucsd.cse110.habitizer.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;

public class RoutineAdapter extends ArrayAdapter<Routine> {
    private final String TAG = "RoutineAdapter";
    private final Consumer<Routine> onDeleteRoutineClick;
    private final Consumer<View> onRoutineItemClick;
    
    public RoutineAdapter(@NonNull Context context,
                          List<Routine> routineList,
                          Consumer<Routine> onDeleteRoutineClick,
                          Consumer<View> onRoutineItemClick)
    {
        super(context, 0, routineList);
        this.onDeleteRoutineClick = onDeleteRoutineClick;
        this.onRoutineItemClick = onRoutineItemClick;
    }
    
    
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.routinelist_item, parent, false);
        }
        
        TextView titleView = convertView.findViewById(R.id.RoutineTitle);
        TextView timeView = convertView.findViewById(R.id.RoutineTime);
        
        Routine selectedRoutine = Objects.requireNonNull(getItem(position));
        titleView.setText(selectedRoutine.getName());
        String timeText = selectedRoutine.getDuration() + "m";
        timeView.setText(timeText);
        
        ImageButton deleteButton = convertView.findViewById(R.id.deleteRoutineButton);
        
        deleteButton.setOnClickListener(view1 -> {
            onDeleteRoutineClick.accept(selectedRoutine);
            notifyDataSetChanged();
        });
        
        convertView.setOnClickListener(view2 -> {
            MainViewModel.switchRoutine(selectedRoutine);
            onRoutineItemClick.accept(view2);
        });
        
        return convertView;
    }
}
