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

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.AddTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.DeleteTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.EditTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.EditTimeDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.InvalidStartDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.InvalidTaskDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoutineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoutineFragment extends Fragment {

    private MainViewModel activityModel;
    private RoutineAdapter adapter;

    private CountDownTimer timer;

    public RoutineFragment() {
        // Required empty public constructor
    }
    private static final boolean[] timerRunning = {false};

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RoutineFragment.
     */
    public static RoutineFragment newInstance() {
        RoutineFragment fragment = new RoutineFragment();
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
        View view = inflater.inflate(R.layout.fragment_routine, container, false);
        Log.d("Routine Fragment", "onCreateView called and adapter made with " + activityModel.getCurRoutine().getValue());
        Log.d("RoutineFragment", "Current Routine: " + activityModel.getCurRoutine().getValue().getName());
        this.adapter = new RoutineAdapter(requireContext(),
                activityModel.getCurRoutine().getValue(),
                name -> {
                    var EditTaskdialogFragment = EditTaskDialogFragment.newInstance(name);
                    EditTaskdialogFragment.show(getParentFragmentManager(), "EditCardDialogFragment");},
                taskName -> {
                    var DeleteTaskdialogFragment = DeleteTaskDialogFragment.newInstance(taskName);
                    DeleteTaskdialogFragment.show(getParentFragmentManager(), "ConfirmDeleteCardDialogFragment");
        });


        activityModel.getMorningTasks().observe(tasks -> {
            if (tasks == null) return;
            Log.d("RoutineFragment", "Notified Data Set 1");
            Log.d("RoutineFragment", "Number of tasks: " + activityModel.getMorningTasks().getValue().size());
            adapter.notifyDataSetChanged();
        });

        activityModel.getEveningTasks().observe(tasks -> {
            if (tasks == null) return;
            Log.d("RoutineFragment", "Notified Data Set 2");
            adapter.notifyDataSetChanged();
        });

        ListView taskList = view.findViewById(R.id.task_list_view);
        taskList.setAdapter(adapter);

        TextView titleView = view.findViewById(R.id.routine_title);
        TextView timeView = view.findViewById(R.id.estimated_time);
        TextView actualTimeView = view.findViewById(R.id.actual_time);

        Button addTask = view.findViewById(R.id.add_task_button);
        Button startRoutine = view.findViewById(R.id.start_routine_button);
        Button stopRoutine = view.findViewById(R.id.stop_routine_button);

        FloatingActionButton ffButton = view.findViewById(R.id.fast_forward_timer_button);
        FloatingActionButton pauseTimerButton = view.findViewById(R.id.pause_timer_button);
        FloatingActionButton restartTimerButton = view.findViewById(R.id.restart_timer_button);

        activityModel.getCompleted().observe(completed -> {
            Log.d("RoutineFragment", "COMPLETED OSVEVSIUDUDSIXOJH!!!");
            if(completed == null) {
                Log.d("RoutineFragment", "COMPLETED = NULL");
                return;
            } else {
                Log.d("RoutineFragment", "COMPLETION: " + completed);
            }
//            Log.d("HabitizerApplication",completed.toString());
            if(completed) {
                adapter.notifyDataSetChanged();
//                activityModel.getCurRoutine().getValue().setOngoing(false);
                stopRoutine.setVisibility(View.INVISIBLE);
                addTask.setVisibility(View.VISIBLE);
                startRoutine.setVisibility(View.VISIBLE);
            }
        });

        addTask.setOnClickListener(x -> {
            Log.d("RoutineFragment", "Notified Data Set");
            adapter.notifyDataSetChanged();
            var dialogFragment = new AddTaskDialogFragment();
            dialogFragment.show(getChildFragmentManager(), "AddTaskDialogFragment");
            adapter.notifyDataSetChanged();

        });

        timeView.setOnClickListener(v -> {
            if (!activityModel.getCurRoutine().getValue().getongoing()) {
                var dialogFragment = new EditTimeDialogFragment();
                dialogFragment.show(getChildFragmentManager(), "EditTimeDialogFragment");
                Log.d("RoutineFragment", "getTime: " + activityModel.getCurRoutine().getValue().getEstimatedTime());
            }
        });
        
        activityModel.getCurRoutine().observe(routine -> {
            timeView.setText(routine.getEstimatedTime() + " min");
        });

//        final boolean[] timerRunning = {false};

        startRoutine.setOnClickListener(x -> {
            Log.d("RoutineFragment", "Notified Data Set");
            adapter.notifyDataSetChanged();
            var routine = activityModel.getCurRoutine().getValue();
//            routine.startRoutine();

            if (routine.getNumTasks() == 0) {
                var dialogFragment = InvalidStartDialogFragment.newInstance();
                dialogFragment.show(getParentFragmentManager(), "InvalidStartDialogFragment");
                return;
            }

            activityModel.startTime();
            Log.d("RoutineFragment", "ROUTINE SHOULD BE ONGOING: " + activityModel.getCurRoutine().getValue().getongoing());
            Log.d("RoutineFragment", "COMPLETION: " + activityModel.getCompleted().getValue());
            addTask.setVisibility(View.INVISIBLE);
            startRoutine.setVisibility(View.INVISIBLE);
            stopRoutine.setVisibility(View.VISIBLE);




            if(true) {

                timerRunning[0] = true;
                if (timer != null) {
                    timer.cancel();
                }
                timer = new CountDownTimer(Integer.MAX_VALUE, 10) {

                    @Override
                    public void onTick(long l) {
//                        Log.d("HabitizerApplication", "ROUTINE TIME: " + routine.getElapsedTimeSecs());
//                        Log.d("HabitizerApplication", "ROUTINE ONGOING: " + routine.getongoing());
//                        Log.d("HabitizerApplication", "TIMER ONGOING: " + routine.getTimer().getOngoing());
                        actualTimeView.setText(String.valueOf(routine.getElapsedTimeSecs()));
                        if (!routine.getongoing()) {
                            adapter.notifyDataSetChanged();
                            stopRoutine.setVisibility(View.INVISIBLE);
                            addTask.setVisibility(View.VISIBLE);
                            startRoutine.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();
            }
            Log.d("RoutineFragment", "Notified Data Set");
            adapter.notifyDataSetChanged();

        });

        ffButton.setOnClickListener(x -> {
            if(!activityModel.getCurRoutine().getValue().getongoing()) return;
            activityModel.getCurRoutine().getValue().manualAddTime(30);
        });

        pauseTimerButton.setOnClickListener(x -> {
            if(!activityModel.getCurRoutine().getValue().getTimer().getOngoing()) return;
            activityModel.getCurRoutine().getValue().pauseRoutineTimer();
            pauseTimerButton.setVisibility(View.GONE);
            restartTimerButton.setVisibility(View.VISIBLE);
        });

        restartTimerButton.setOnClickListener(v -> {
            if(activityModel.getCurRoutine().getValue().getTimer().getOngoing()) return;
            activityModel.getCurRoutine().getValue().pauseRoutineTimer();
            restartTimerButton.setVisibility(View.GONE);
            pauseTimerButton.setVisibility(View.VISIBLE);
        });

        stopRoutine.setOnClickListener(v -> {
            if(!activityModel.getCurRoutine().getValue().getongoing()) return;
            activityModel.endRoutine();
            Log.d("RoutineFragment", "ROUTINE SHOULD NOT BE ONGOING: " + activityModel.getCurRoutine().getValue().getongoing());
            Log.d("RoutineFragment", "COMPLETION: " + activityModel.getCompleted().getValue());
        });


        if (getArguments() != null) {
            titleView.setText(activityModel.getCurRoutine().getValue().getName());
            timeView.setText(activityModel.getCurRoutine().getValue().getEstimatedTime() + " min");
        }

        return view;
    }
}