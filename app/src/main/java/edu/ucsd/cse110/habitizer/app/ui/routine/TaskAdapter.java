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
    Consumer<Task> onTaskClick;
    Routine routine;
    
    public TaskAdapter(Context context,
                       List<Task> taskList,
                       Routine routine,
                       Consumer<String> onEditClick,
                       Consumer<String> onDeleteClick,
                       Consumer<Task> onTaskClick
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
        this.onTaskClick = onTaskClick;

        Log.d(TAG, "task adapter taskList size:" + taskList.size());
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.tasklist_item, parent, false);
        }

        // Get the task for this position.
        var task = getItem(position);
        if (task == null) {
            Log.e(TAG, "Task is NULL at position " + position);
            return new View(getContext());
        }

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
        });
        
        deleteTaskButton.setOnClickListener(v -> onDeleteClick.accept(task.getName()));
        
        taskTitle.setText(task.getName());



//
//        // If the task is already completed, display its stored time.
//        if (task.isCompleted()){
//            String timeText = task.getTimeSpentMinutes() + "m";
//            taskTime.setText(timeText);
//        }


        // Set click listener on the entire view
        convertView.setOnClickListener(v -> {
            onTaskClick.accept(task);
            if (task.isCompleted() && routine.getOngoing()) {
                Log.d(TAG, "Task: " + task.getName() + " - Completion state: " + task.isCompleted());
                strikethrough(taskTitle);
//                String timeText = routine.checkOffTask(task) + "m";
//                taskTime.setText(timeText);
            }
        });
        
        //         Set initial strike-through based on task completion state
        if (task.isCompleted()) {
            strikethrough(taskTitle);
            Log.d(TAG, "task: " + task.getName() + " - completion state: " + task.isCompleted() + " - strikethrough applied");
        }
        else {
            removeStrikethrough(taskTitle);
            Log.d(TAG, "task: " + task.getName() + " - completion state: " + task.isCompleted() + " - strikethrough removed");
        }
        
        
        return convertView;
    }

    private void strikethrough(TextView textView) {
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }
    
    private void removeStrikethrough(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
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
        //        Log.d(TAG, "getItem() called for position " + position + ": " + (task != null ? task.getName() : "NULL"));
        return super.getItem(position);
    }
}
