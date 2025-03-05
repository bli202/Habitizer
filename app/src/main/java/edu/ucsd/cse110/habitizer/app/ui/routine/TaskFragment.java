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

import java.util.Objects;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.AddTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.DeleteTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.EditTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.EditEstimatedTimeDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.InvalidStartDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFragment extends Fragment {
    
    private final String TAG = "TaskFragment";

    private MainViewModel activityModel;
    private TaskAdapter adapter;

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
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        var curRoutineSubject = MainViewModel.getCurRoutine();
        var curRoutine = curRoutineSubject.getValue();
        View view = inflater.inflate(R.layout.fragment_tasklist_view, container, false);
        Log.d(TAG, "Current Routine: " + Objects.requireNonNull(curRoutine).getName());
        this.adapter = new TaskAdapter(requireContext(),
                activityModel.getCurTasks().getValue(),
                curRoutine,
                name -> {
                    var EditTaskdialogFragment = EditTaskDialogFragment.newInstance(name);
                    EditTaskdialogFragment.show(getParentFragmentManager(), "EditCardDialogFragment");},
                taskName -> {
                    var DeleteTaskdialogFragment = DeleteTaskDialogFragment.newInstance(taskName);
                    DeleteTaskdialogFragment.show(getParentFragmentManager(), "ConfirmDeleteCardDialogFragment");
        });
        adapter.notifyDataSetChanged();

        // List of all elements in fragment_tasklist_view
        ListView taskList = view.findViewById(R.id.task_list_view);
        taskList.setAdapter(adapter);

        activityModel.getCurTasks().observe(tasks -> {
            if (tasks == null) return;
            Log.d(TAG, "Notified Data Set 3");
            adapter.notifyDataSetChanged();
        });

        TextView titleView = view.findViewById(R.id.routine_title);
        TextView estimatedTimeView = view.findViewById(R.id.estimated_time);
        TextView actualTimeView = view.findViewById(R.id.actual_time);

        Button addTask = view.findViewById(R.id.add_task_button);
        Button startRoutine = view.findViewById(R.id.start_routine_button);
        Button stopRoutine = view.findViewById(R.id.stop_routine_button);

        FloatingActionButton ffButton = view.findViewById(R.id.fast_forward_timer_button);
        FloatingActionButton pauseTimerButton = view.findViewById(R.id.pause_timer_button);
        FloatingActionButton restartTimerButton = view.findViewById(R.id.restart_timer_button);
        
        
        activityModel.getCompleted().observe(completed -> {
            Log.d(TAG, "COMPLETED!!!");
            if(completed == null) {
                Log.d(TAG, "COMPLETED = NULL");
                return;
            } else {
                Log.d(TAG, "COMPLETION: " + completed);
            }
            if(completed) {
                stopRoutine.setVisibility(View.INVISIBLE);
                addTask.setVisibility(View.VISIBLE);
                startRoutine.setVisibility(View.VISIBLE);
            }
        });

        addTask.setOnClickListener(x -> {
//            Log.d(TAG, "Notified Data Set");
            var dialogFragment = new AddTaskDialogFragment();
            dialogFragment.show(getChildFragmentManager(), "AddTaskDialogFragment");
//            adapter.notifyDataSetChanged();
        });

        estimatedTimeView.setOnClickListener(v -> {
            if (!curRoutine.getOngoing()) {
                var dialogFragment = new EditEstimatedTimeDialogFragment();
                dialogFragment.show(getChildFragmentManager(), "EditTimeDialogFragment");
//                Log.d(TAG, "getTime: " + curRoutine.getEstimatedTime());
            }
//            adapter.notifyDataSetChanged();
        });
        
        curRoutineSubject.observe(routine -> {
            assert routine != null;
            String timeText = routine.getEstimatedTime() + "m";
            estimatedTimeView.setText(timeText);
        });

        startRoutine.setOnClickListener(x -> {
//            Log.d(TAG, "Notified Data Set");
//            adapter.notifyDataSetChanged();
            
            if (curRoutine.getNumTasks() == 0) {
                var dialogFragment = InvalidStartDialogFragment.newInstance();
                dialogFragment.show(getParentFragmentManager(), "InvalidStartDialogFragment");
                return;
            }

            activityModel.startTime();
//            Log.d(TAG, "ROUTINE SHOULD BE ONGOING: " + curRoutine.getOngoing());
//            Log.d(TAG, "COMPLETION: " + activityModel.getCompleted());
            addTask.setVisibility(View.INVISIBLE);
            startRoutine.setVisibility(View.INVISIBLE);
            stopRoutine.setVisibility(View.VISIBLE);


            timerRunning[0] = true;
            if (timer != null) {
                timer.cancel();
            }

            timer = new CountDownTimer(Integer.MAX_VALUE, 1000) {


                @Override
                public void onTick(long l) {
                    String timeText = curRoutine.getElapsedTime() + "m";
                    actualTimeView.setText(timeText);
                    if (!curRoutine.getOngoing()) {
//                        adapter.notifyDataSetChanged();
                        stopRoutine.setVisibility(View.INVISIBLE);
                        addTask.setVisibility(View.VISIBLE);
                        startRoutine.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFinish() {

                }
            }.start();
//            Log.d(TAG, "Notified Data Set");
//            adapter.notifyDataSetChanged();

        });

        ffButton.setOnClickListener(x -> {
            if(!curRoutine.getOngoing()) return;
            curRoutine.manualAddTime(30);
        });

        pauseTimerButton.setOnClickListener(x -> {
            if(!curRoutine.getTimer().getOngoing()) return;
            curRoutine.pauseRoutineTimer();
            pauseTimerButton.setVisibility(View.GONE);
            restartTimerButton.setVisibility(View.VISIBLE);
        });

        restartTimerButton.setOnClickListener(v -> {
            if(curRoutine.getTimer().getOngoing()) return;
            curRoutine.pauseRoutineTimer();
            restartTimerButton.setVisibility(View.GONE);
            pauseTimerButton.setVisibility(View.VISIBLE);
        });

        stopRoutine.setOnClickListener(v -> {
            if(!curRoutine.getOngoing()) return;
            activityModel.endCurRoutine();
//            Log.d(TAG, "ROUTINE SHOULD NOT BE ONGOING: " + curRoutine.getOngoing());
//            Log.d(TAG, "COMPLETION: " + activityModel.getCompleted());
        });


        if (getArguments() != null) {
            titleView.setText(curRoutine.getName());
            String timeText = curRoutine.getEstimatedTime() + "m";
            estimatedTimeView.setText(timeText);
        }

        return view;
    }
}