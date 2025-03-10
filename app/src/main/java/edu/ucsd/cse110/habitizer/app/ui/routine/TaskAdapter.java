package edu.ucsd.cse110.habitizer.app.ui.routine;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.observables.Subject;

import android.graphics.Paint;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TaskAdapter extends ArrayAdapter<Task> {
    private final String TAG = "TaskAdapter";
    private final Consumer<Task> taskCompletionUpdate;
    private final Consumer<Task> onUpClick;
    private final Consumer<Task> onDownClick;
    Consumer<String> onDeleteClick;
    Consumer<String> onEditClick;
    Consumer<Task> onTaskClick;
    Subject<Boolean> ongoingSubject;
    
    int taskTime;
    boolean ongoing;
    private FloatingActionButton editTaskButton;
    private FloatingActionButton deleteTaskButton;
    Boolean isCompleted = false;
    
    public void setTaskTime(int taskTime) {
        this.taskTime = taskTime;
    }
    
    public TaskAdapter(Context context,
                       List<Task> taskList,
                       Subject<Boolean> ongoingSubject,
                       Consumer<String> onEditClick,
                       Consumer<String> onDeleteClick,
                       Consumer<Task> onTaskClick,
                       Consumer<Task> taskCompletionUpdate,
                       Consumer<Task> onUpClick,
                       Consumer<Task> onDownClick
    ) {
        // This sets a bunch of stuff internally, which we can access
        // with getContext() and getItem(), for example.
        //
        // Also note that ArrayAdapter NEEDS a mutable List (ArrayList),
        // or it will crash.
        super(context, 0, taskList);
        this.onEditClick = onEditClick;
        this.onDeleteClick = onDeleteClick;
        this.ongoingSubject = ongoingSubject;
        this.onTaskClick = onTaskClick;
        this.taskCompletionUpdate = taskCompletionUpdate;
        this.onUpClick = onUpClick;
        this.onDownClick = onDownClick;
        
        // Set initial ongoing state
        ongoingSubject.observe(ongoing -> this.ongoing = Boolean.TRUE.equals(ongoing));
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
        ImageButton upArrow = convertView.findViewById(R.id.move_up_arrow);
        ImageButton downArrow = convertView.findViewById(R.id.move_down_arrow);
        
        /*
         * Set edit and delete buttons visibility based on ongoing state
         */
        if (ongoing) {
            editTaskButton.setVisibility(View.INVISIBLE);
            deleteTaskButton.setVisibility(View.INVISIBLE);
        } else {
            editTaskButton.setVisibility(View.VISIBLE);
            deleteTaskButton.setVisibility(View.VISIBLE);
        }
        
        taskNameText.setText(task.getName());
        
        editTaskButton.setOnClickListener(v -> {
            var name = task.getName();
            assert name != null;
            onEditClick.accept(name);
        });
        
        deleteTaskButton.setOnClickListener(v -> {
            onDeleteClick.accept(task.getName());
            notifyDataSetChanged();
        });

        upArrow.setOnClickListener(x -> {
            onUpClick.accept(task);
//            notifyDataSetChanged();
//            Log.d(TAG, "task : " + task.getName() + " order: " + task.getOrder());
        });

        downArrow.setOnClickListener(x -> {
            onDownClick.accept(task);
//            notifyDataSetChanged();
//            Log.d(TAG, "task : " + task.getName() + " order: " + task.getOrder());
        });
        

        
        // Set click listener on the entire view
        convertView.setOnClickListener(v -> {
            onTaskClick.accept(task);
        });
        
        // taskCompletionUpdate checks weather the current task is completed or not
        taskCompletionUpdate.accept(task);
        // Then sets the strikethrough accordingly
        if (isCompleted) {
            strikethrough(taskNameText);
            String taskTimeTextString = taskTime + "m";
            taskTimeText.setText(taskTimeTextString);
        } else {
            removeStrikethrough(taskNameText);
        }
        return convertView;
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
