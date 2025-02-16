package edu.ucsd.cse110.habitizer.app.ui.routine;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.TaskViewBinding;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.EditTaskDialogFragment;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import android.graphics.Paint;
import android.widget.TextView;

public class RoutineAdapter extends ArrayAdapter<Task> {

    Consumer<String> onDeleteClick;
    Consumer<String> onEditClick;
    MainViewModel activityModel;


    Routine routine;
    public RoutineAdapter(Context context, Routine routine,
                          Consumer<String> onEditClick,
                          Consumer<String> onDeleteClick
                          ) {
        // This sets a bunch of stuff internally, which we can access
        // with getContext() and getItem(), for example.
        //
        // Also note that ArrayAdapter NEEDS a mutable List (ArrayList),
        // or it will crash.
        super(context, 0, routine.getTaskList());
        this.notifyDataSetChanged();
        Log.d("RoutineAdapter Constructor", "Routine " + routine + "'s first task: " + routine.getTaskList().get(0).getName());
        this.onEditClick = onEditClick;
        this.onDeleteClick = onDeleteClick;
        this.routine = routine;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d("RoutineAdapter", "getView() called for position " + position);

        // Get the task for this position.
        var task = getItem(position);
        if (task == null) {
            Log.e("RoutineAdapter", "Task is NULL at position " + position);
            return new View(getContext());
        }
        assert task != null;

        Log.d("RoutineAdapter", "Displaying task: " + task.getName());

        // Check if a view is being reused...
        TaskViewBinding binding;
        var layoutInflater = LayoutInflater.from(getContext());
        binding = TaskViewBinding.inflate(layoutInflater, parent, false);

        if (routine.getongoing()) {
            binding.editButton.setVisibility(View.INVISIBLE);
            binding.deleteButton.setVisibility(View.INVISIBLE);
        }
        else {
            binding.editButton.setVisibility(View.VISIBLE);
            binding.deleteButton.setVisibility(View.VISIBLE);
            //return binding.getRoot();
        }


        binding.editButton.setOnClickListener(v -> {
            var name = task.getName();
            assert name != null;
            onEditClick.accept(name);
        });

        binding.deleteButton.setOnClickListener(v -> {
            onDeleteClick.accept(task.getName());
        });

        binding.taskTitle.setText(task.getName());

        // Set initial strike-through based on task completion state
        updateStrikeThrough(binding.taskTitle, task.isCompleted());

        // If the task is already completed, display its stored time.
        if (task.isCompleted()){
            binding.taskTime.setText(String.valueOf(task.getTimeSpent()));
        }


        // Set click listener on the entire view
        binding.getRoot().setOnClickListener(v -> {
            if(task.isCompleted() || !routine.getongoing()) return;
            task.toggleCompletion();  // Toggle task completion state
            Log.d("TAG", "Task: " + task.getName() + " - Completion state: " + task.isCompleted());
            updateStrikeThrough(binding.taskTitle, task.isCompleted());
//            routine.checkOffTask(task);
            binding.taskTime.setText(String.valueOf(routine.checkOffTask(task)));
        });


        return binding.getRoot();
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
        super.getItem(position);
        Task task = super.getItem(position);
        Log.d("RoutineAdapter", "getItem() called for position " + position + ": " + (task != null ? task.getName() : "NULL"));
        return task;
    }

    public void switchRoutine(Routine routine) {
        this.clear();
        this.addAll(routine.getTaskList());
    }



}
