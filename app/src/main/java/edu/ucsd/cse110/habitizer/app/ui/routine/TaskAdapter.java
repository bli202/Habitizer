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
import edu.ucsd.cse110.habitizer.lib.domain.Task;

import android.graphics.Paint;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TaskAdapter extends ArrayAdapter<Task> {
    private final String TAG = "TaskAdapter";
    Consumer<String> onDeleteClick;
    Consumer<String> onEditClick;
    Consumer<Task> onTaskClick;
    Boolean ongoing;
    private FloatingActionButton editTaskButton;
    private FloatingActionButton deleteTaskButton;
    private Boolean isCompleted = false;
    
    public TaskAdapter(Context context,
                       List<Task> taskList,
                       Boolean ongoing,
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
        this.ongoing = ongoing;
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
            Log.d(TAG, "Task is NULL at position " + position);
            return new View(getContext());
        }
        
        // UI Elements to reuse.
        editTaskButton = convertView.findViewById(R.id.editTaskButton);
        deleteTaskButton = convertView.findViewById(R.id.deleteTaskButton);
        TextView taskNameText = convertView.findViewById(R.id.taskTitle);
        TextView taskTimeText = convertView.findViewById(R.id.task_time);
        
        
        editTaskButton.setOnClickListener(v -> {
            var name = task.getName();
            assert name != null;
            onEditClick.accept(name);
        });
        
        deleteTaskButton.setOnClickListener(v -> onDeleteClick.accept(task.getName()));
        
        taskNameText.setText(task.getName());
        
        if (ongoing) {
//            Log.d(TAG, "ongoing is true");
            editTaskButton.setVisibility(View.INVISIBLE);
            deleteTaskButton.setVisibility(View.INVISIBLE);
        } else {
//            Log.d(TAG, "ongoing is false");
            editTaskButton.setVisibility(View.VISIBLE);
            deleteTaskButton.setVisibility(View.VISIBLE);
        }


//
//        // If the task is already completed, display its stored time.
//        if (task.isCompleted()){
//            String timeText = task.getTimeSpentMinutes() + "m";
//            taskTime.setText(timeText);
//        }
        
        
        // Set click listener on the entire view
        convertView.setOnClickListener(v -> {
            onTaskClick.accept(task);
            if (task.isCompleted() && ongoing) {
                setTaskCompletionState(true);
                Log.d(TAG, "Task: " + task.getName() + " - Completion state: " + task.isCompleted());
                strikethrough(taskNameText);
//                String timeText = routine.checkOffTask(task) + "m";
//                taskTime.setText(timeText);
                notifyDataSetChanged();
            }
        });
        
        // Set initial strike-through based on task completion state
        Log.d(TAG, "isCompleted: " + isCompleted);
        if (isCompleted) {
            strikethrough(taskNameText);
            Log.d(TAG, "task: " + task.getName() + " - completion state: " + task.isCompleted() + " - strikethrough applied");
        } else {
            removeStrikethrough(taskNameText);
            Log.d(TAG, "task: " + task.getName() + " - completion state: " + task.isCompleted() + " - strikethrough removed");
        }
        return convertView;
    }
    
    public void setOngoing(Boolean ongoing) {
        this.ongoing = ongoing;
    }
    
    public void setTaskCompletionState(Boolean inCompleted) {
        isCompleted = inCompleted;
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
