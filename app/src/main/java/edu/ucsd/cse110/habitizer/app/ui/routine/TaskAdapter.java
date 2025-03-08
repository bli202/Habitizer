package edu.ucsd.cse110.habitizer.app.ui.routine;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import android.graphics.Paint;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TaskAdapter extends ArrayAdapter<Task> {
    private final String TAG = "TaskAdapter";
    Consumer<String> onDeleteClick;
    Consumer<String> onEditClick;
    Routine routine;
    
    public TaskAdapter(Context context,
                       List<Task> taskList,
                       Routine routine,
                       Consumer<String> onEditClick,
                       Consumer<String> onDeleteClick
                          ) {
        // This sets a bunch of stuff internally, which we can access
        // with getContext() and getItem(), for example.
        //
        // Also note that ArrayAdapter NEEDS a mutable List (ArrayList),
        // or it will crash.
        super(context, 0, taskList);
        this.onEditClick = onEditClick;
        this.onDeleteClick = onDeleteClick;
        this.routine = routine;

        Log.d(TAG, "task adapter tasklist size:" + taskList.size());
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.tasklist_item, parent, false);
        }

//        Log.d(TAG, "getView() called for position " + position);

        // Get the task for this position.
        var task = getItem(position);
        if (task == null) {
            Log.e(TAG, "Task is NULL at position " + position);
            return new View(getContext());
        }

//        Log.d(TAG, "Displaying task: " + task.getName());

        // UI Elements to reuse.
        FloatingActionButton editTaskButton = convertView.findViewById(R.id.editTaskButton);
        FloatingActionButton deleteTaskButton = convertView.findViewById(R.id.deleteTaskButton);
        TextView taskTitle = convertView.findViewById(R.id.taskTitle);
        TextView taskTime = convertView.findViewById(R.id.task_time);

        if (routine.getOngoing()) {
            editTaskButton.setVisibility(View.INVISIBLE);
            deleteTaskButton.setVisibility(View.INVISIBLE);
        }
        else {
            editTaskButton.setVisibility(View.VISIBLE);
            deleteTaskButton.setVisibility(View.VISIBLE);
        }
        
        editTaskButton.setOnClickListener(v -> {
            var name = task.getName();
            assert name != null;
            onEditClick.accept(name);
            notifyDataSetChanged();
        });
        
        deleteTaskButton.setOnClickListener(v -> {
            onDeleteClick.accept(task.getName());
            notifyDataSetChanged();
        });
        
        taskTitle.setText(task.getName());

        // Set initial strike-through based on task completion state
        updateStrikeThrough(taskTitle, task.isCompleted());

        // If the task is already completed, display its stored time.
        if (task.isCompleted()){
            String timeText = task.getTimeSpentMinutes() + "m";
            taskTime.setText(timeText);
        }


        // Set click listener on the entire view
        convertView.setOnClickListener(v -> {
            if(task.isCompleted() || !routine.getOngoing()) return;
            task.toggleCompletion();  // Toggle task completion state
            Log.d(TAG, "Task: " + task.getName() + " - Completion state: " + task.isCompleted());
            updateStrikeThrough(taskTitle, task.isCompleted());
            String timeText = routine.checkOffTask(task) + "m";
            taskTime.setText(timeText);
        });
        
        return convertView;
    }

    private void updateStrikeThrough(TextView textView, boolean isCompleted) {
        if (isCompleted) {
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            textView.setPaintFlags(textView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    // The below methods aren't strictly necessary, usually.
    // But get in the habit of defining them because they never hurt
    // (as long as you have IDs for each item) and sometimes you need them.

    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public Task getItem(int position) {
        Task task = super.getItem(position);
//        Log.d(TAG, "getItem() called for position " + position + ": " + (task != null ? task.getName() : "NULL"));
        return task;
    }
}
