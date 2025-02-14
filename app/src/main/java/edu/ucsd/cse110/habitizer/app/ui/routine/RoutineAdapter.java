package edu.ucsd.cse110.habitizer.app.ui.routine;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.habitizer.app.databinding.TaskViewBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import android.graphics.Paint;
import android.widget.TextView;

public class RoutineAdapter extends ArrayAdapter<Task> {

    Consumer<String> onDeleteClick;
//    public RoutineAdapter(Context context,
//                          List<Task> tasks) {
//        // This sets a bunch of stuff internally, which we can access
//        // with getContext() and getItem() for example.
//        //
//        // Also note that ArrayAdapter NEEDS a mutable List (ArrayList),
//        // or it will crash!
//        super(context, 0, new ArrayList<>(tasks));
//        this.onDeleteClick = onDeleteClick;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // Get the flashcard for this position.
//        var task = getItem(position);
//
//        if (task == null) {
//            Log.e("RoutineAdapter", "Task is NULL at position " + position);
//            return new View(getContext());
//        }
//        assert task != null;
//
//        Log.d("RoutineAdapter", "Displaying task: " + task.getName());
//
//        // Check if a view is being reused...
//        TaskViewBinding binding;
//        if (convertView != null) {
//            // if so, bind to it
//            binding = TaskViewBinding.bind(convertView);
//        } else {
//            // otherwise inflate a new view from our layout XML.
//            var layoutInflater = LayoutInflater.from(getContext());
//            binding = TaskViewBinding.inflate(layoutInflater, parent, false);
//        }
//
//        // Populate the view with the flashcard's data.
//        binding.TaskTitle.setText(task.getName());
//
//        binding.deleteButton.setOnClickListener(v -> {
//            var name = task.getName();
//            assert name != null;
//            onDeleteClick.accept(name);
//        });
//
//        return binding.getRoot();
//    }

    // ^ we dc about delete button rn

    public RoutineAdapter(Context context, List<Task> tasks) {
        // This sets a bunch of stuff internally, which we can access
        // with getContext() and getItem(), for example.
        //
        // Also note that ArrayAdapter NEEDS a mutable List (ArrayList),
        // or it will crash.
        super(context, 0, new ArrayList<>(tasks));
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        var task = getItem(position);
        if (task == null) {
            return new View(getContext());
        }

        TaskViewBinding binding;
        var layoutInflater = LayoutInflater.from(getContext());
        binding = TaskViewBinding.inflate(layoutInflater, parent, false);

        // Populate the view with the task's data
        binding.taskTitle.setText(task.getName());

        // Set initial strike-through based on task completion state
        updateStrikeThrough(binding.taskTitle, task.isCompleted());

        // Set click listener on the entire view
        binding.getRoot().setOnClickListener(v -> {
            task.toggleCompletion();  // Toggle task completion state
            updateStrikeThrough(binding.taskTitle, task.isCompleted());
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
        Task task = super.getItem(position);
        Log.d("RoutineAdapter", "getItem() called for position " + position + ": " + (task != null ? task.getName() : "NULL"));
        return task;
    }


}
