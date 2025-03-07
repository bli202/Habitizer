package edu.ucsd.cse110.habitizer.app.ui.routine;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentTasklistViewBinding;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.AddTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.DeleteTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.EditTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.EditEstimatedTimeDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.InvalidStartDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.EditRoutineDialogFragment;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFragment extends Fragment {
    
    private final String TAG = "TaskFragment";

    private MainViewModel activityModel;
    private TaskAdapter adapter;
    private FragmentTasklistViewBinding view;

    private CountDownTimer timer;

    public TaskFragment() {
        // Required empty public constructor
    }
    private static final boolean[] timerRunning = {false};

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RoutineFragment.
     */
    public static TaskFragment newInstance() {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        this.adapter = new TaskAdapter(requireContext(),
                new ArrayList<>(List.of()),
                activityModel.getCurRoutine().getValue(),
                name -> {
                    var EditTaskdialogFragment = EditTaskDialogFragment.newInstance(name);
                    EditTaskdialogFragment.show(getParentFragmentManager(), "EditCardDialogFragment");},
                taskName -> {
                    var DeleteTaskdialogFragment = DeleteTaskDialogFragment.newInstance(taskName);
                    DeleteTaskdialogFragment.show(getParentFragmentManager(), "ConfirmDeleteCardDialogFragment");
                });

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        var curRoutineSubject = MainViewModel.getCurRoutine();
        var curRoutine = curRoutineSubject.getValue();
        var curTasksSubject = activityModel.getCurTasks();

        this.view = FragmentTasklistViewBinding.inflate(inflater, container, false);

        // Set the adapter on the ListView
        view.taskListView.setAdapter(adapter);

        curRoutineSubject.observe(routine -> {
            if (routine != null) {
                view.routineTitle.setText(routine.getName());
                String timeText = routine.getEstimatedTime() + "m";
                view.estimatedTime.setText(timeText);
            }
        });

        curTasksSubject.observe(tasks -> {
            if (tasks == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(tasks)); // remember the mutable copy here!
            adapter.notifyDataSetChanged();
        });

        activityModel.getCompleted().observe(completed -> {
            Log.d(TAG, "COMPLETED!!!");
            if(completed == null) {
                Log.d(TAG, "COMPLETED = NULL");
                return;
            } else {
                Log.d(TAG, "COMPLETION: " + completed);
            }
            if(completed) {
                view.stopRoutineButton.setVisibility(View.INVISIBLE);
                view.addTaskButton.setVisibility(View.VISIBLE);
                view.startRoutineButton.setVisibility(View.VISIBLE);
            }
        });

        view.editRoutineButton.setOnClickListener(x -> {
            var dialogFragment = new EditRoutineDialogFragment().newInstance(curRoutine.getName());
            dialogFragment.show(getChildFragmentManager(), "EditRoutineDialogFragment");
            Log.d(TAG, "edit routine button pressed");
        });

        view.addTaskButton.setOnClickListener(x -> {
            var dialogFragment = new AddTaskDialogFragment();
            dialogFragment.show(getChildFragmentManager(), "AddTaskDialogFragment");
        });

        view.estimatedTime.setOnClickListener(v -> {
            if (!curRoutine.getOngoing()) {
                var dialogFragment = new EditEstimatedTimeDialogFragment();
                dialogFragment.show(getChildFragmentManager(), "EditTimeDialogFragment");
            }
        });


        view.startRoutineButton.setOnClickListener(x -> {
            if (curRoutine.getNumTasks() == 0) {
                var dialogFragment = InvalidStartDialogFragment.newInstance();
                dialogFragment.show(getParentFragmentManager(), "InvalidStartDialogFragment");
                return;
            }

            activityModel.startTime();
            view.addTaskButton.setVisibility(View.INVISIBLE);
            view.startRoutineButton.setVisibility(View.INVISIBLE);
            view.stopRoutineButton.setVisibility(View.VISIBLE);


            timerRunning[0] = true;
            if (timer != null) {
                timer.cancel();
            }

            timer = new CountDownTimer(Integer.MAX_VALUE, 1000) {


                @Override
                public void onTick(long l) {
                    String timeText = curRoutine.getElapsedTime() + "m";
                    view.actualTime.setText(timeText);
                    if (!curRoutine.getOngoing()) {
//                        adapter.notifyDataSetChanged();
                        view.stopRoutineButton.setVisibility(View.INVISIBLE);
                        view.addTaskButton.setVisibility(View.VISIBLE);
                        view.startRoutineButton.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFinish() {

                }
            }.start();

        });

        view.fastForwardTimerButton.setOnClickListener(x -> {
            if(!curRoutine.getOngoing()) return;
            curRoutine.manualAddTime(30);
        });

        view.pauseTimerButton.setOnClickListener(x -> {
            if(!curRoutine.getTimer().getOngoing()) return;
            curRoutine.pauseRoutineTimer();
            view.pauseTimerButton.setVisibility(View.GONE);
            view.restartTimerButton.setVisibility(View.VISIBLE);
        });

        view.restartTimerButton.setOnClickListener(v -> {
            if(curRoutine.getTimer().getOngoing()) return;
            curRoutine.pauseRoutineTimer();
            view.restartTimerButton.setVisibility(View.GONE);
            view.pauseTimerButton.setVisibility(View.VISIBLE);
        });

        view.stopRoutineButton.setOnClickListener(v -> {
            if(!curRoutine.getOngoing()) return;
            activityModel.endCurRoutine();
        });


        if (getArguments() != null) {
            view.routineTitle.setText(curRoutine.getName());
            String timeText = curRoutine.getEstimatedTime() + "m";
            view.estimatedTime.setText(timeText);
        }

        return view.getRoot();
    }
}