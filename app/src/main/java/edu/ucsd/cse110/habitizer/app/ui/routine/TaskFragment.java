package edu.ucsd.cse110.habitizer.app.ui.routine;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentTasklistViewBinding;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.AddTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.DeleteTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.EditTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.EditEstimatedTimeDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.InvalidStartDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.EditRoutineDialogFragment;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.observables.Observer;
import edu.ucsd.cse110.observables.Subject;

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
    private Subject<List<Task>> curTasksSubject;
    private Observer<? super Routine> routineObserver;
    private Observer<? super List<Task>> tasksObserver;
    private Subject<Routine> curRoutineSubject;
    private Observer<? super Boolean> onGoingObserver;
    private Subject<Boolean> currentRoutineOngoing;
    private boolean isOngoing;
    private Routine curRoutine;
    private Boolean firstTimeStarting = true;


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

        // Initialize MainViewModel
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        // Set variables for observers
        curRoutineSubject = MainViewModel.getCurrentRoutine();
        curTasksSubject = activityModel.getCurrentRoutineTasksSubject();
        currentRoutineOngoing = activityModel.isCurrentRoutineOngoing();
        curRoutine = curRoutineSubject.getValue();
        isOngoing = false;
        assert curRoutine != null;

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = FragmentTasklistViewBinding.inflate(inflater, container, false);

        /*
         * For updating routine name when routine name is changed
         */
        this.routineObserver = curRoutineSubject.observe(routine -> {
            if (routine == null) return;
            curRoutine = routine;
            view.routineTitle.setText(curRoutine.getName());
            String timeText = curRoutine.getEstimatedTime() + "m";
            view.estimatedTime.setText(timeText);
        });

        /*
         * For updating task list when task list is changed
         */
        this.tasksObserver = curTasksSubject.observe(tasks -> {
            if (tasks == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(tasks)); // remember the mutable copy here!
            adapter.notifyDataSetChanged();
        });

        /*
         * For updating buttons when routine is started or stopped
         */
        this.onGoingObserver = currentRoutineOngoing.observe(ongoing -> {
            isOngoing = Boolean.TRUE.equals(ongoing);
            if (ongoing == null) {
                Log.d(TAG, "Routine ongoing is NULL");
                return;
            } else {
                Log.d(TAG, "Routine ongoing is: " + ongoing);
            }
            if (ongoing) {

                timer = new CountDownTimer(Integer.MAX_VALUE, 1000) {


                    @Override
                    public void onTick(long l) {
                        String timeText;
                        if(!isOngoing) {
                            timeText = "Cumulative: " + (int) Math.ceil(activityModel.getCumulativeTime() / 60.0) + "m\n"
                                    + "Task: " + (int) Math.ceil((activityModel.getTaskTimeNoReset() + 0.0001) / 60.0) + "m";
                        } else {
                            timeText = "Cumulative: " + (activityModel.getCumulativeTime() / 60) + "m\n"
                                    + "Task: " + (activityModel.getTaskTimeNoReset() / 60) + "m";
                        }
                        view.actualTime.setText(timeText);
                        if (!isOngoing) {
                            view.stopRoutineButton.setVisibility(View.INVISIBLE);
                            view.addTaskButton.setVisibility(View.VISIBLE);
                            view.startRoutineButton.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();

                if(firstTimeStarting) {
                    view.pauseTimerButton.setVisibility(View.INVISIBLE);
                    view.restartTimerButton.setVisibility(View.VISIBLE);
                    firstTimeStarting = false;
                }

                view.stopRoutineButton.setVisibility(View.VISIBLE);
                view.addTaskButton.setVisibility(View.INVISIBLE);
                view.startRoutineButton.setVisibility(View.INVISIBLE);
                view.editRoutineButton.setVisibility(View.INVISIBLE);
            } else {
                String timeText;
                if(!isOngoing) {
                    timeText = "Cumulative: " + (int) Math.ceil(activityModel.getCumulativeTime() / 60.0) + "m\n"
                            + "Task: " + (int) Math.ceil((activityModel.getTaskTimeNoReset() + 0.0001) / 60.0) + "m";
                } else {
                    timeText = "Cumulative: " + (activityModel.getCumulativeTime() / 60) + "m\n"
                            + "Task: " + (activityModel.getTaskTimeNoReset() / 60) + "m";
                }
                view.actualTime.setText(timeText);

                view.stopRoutineButton.setVisibility(View.INVISIBLE);
                view.addTaskButton.setVisibility(View.VISIBLE);
                view.startRoutineButton.setVisibility(View.VISIBLE);
                view.editRoutineButton.setVisibility(View.VISIBLE);
            }
            adapter.notifyDataSetChanged();
        });

        /*
         * Initialize TaskAdapter
         */
        this.adapter = new TaskAdapter(requireContext(),
                activityModel.getCurrentRoutineTasks(),
                activityModel.isCurrentRoutineOngoing(),
                name -> {
                    var EditTaskdialogFragment = EditTaskDialogFragment.newInstance(name);
                    EditTaskdialogFragment.show(getParentFragmentManager(), "EditCardDialogFragment");
                },
                taskName -> {
                    var DeleteTaskdialogFragment = DeleteTaskDialogFragment.newInstance(taskName);
                    DeleteTaskdialogFragment.show(getParentFragmentManager(), "ConfirmDeleteCardDialogFragment");
                }, task -> {
            if (curRoutine.getOngoing() && !activityModel.getTaskCompleted(task.getName()) && activityModel.isTimerOngoing()) {
                activityModel.setTaskTime(curRoutine.getId(), task.getName(), activityModel.checkOffTask(task));
            }
        }, task -> {
                adapter.setTaskCompletionState(activityModel.getTaskCompleted(task.getName()));
                adapter.setTaskTime(activityModel.getTaskTime(curRoutine.getId(), task.getName()));
        }, task -> {
            activityModel.moveUp(curRoutine.getId(), task.getOrder());
        }, task -> {
            activityModel.moveDown(curRoutine.getId(), task.getOrder());
        });

        // Set the adapter on the ListView
        view.taskListView.setAdapter(adapter);

        /*
         * Set up button listeners
         */
        view.editRoutineButton.setOnClickListener(x -> {
            var dialogFragment = EditRoutineDialogFragment.newInstance(curRoutine.getName());
            dialogFragment.show(getChildFragmentManager(), "EditRoutineDialogFragment");
            Log.d(TAG, "Edit routine button pressed");
        });

        view.addTaskButton.setOnClickListener(x -> {
            var dialogFragment = new AddTaskDialogFragment();
            dialogFragment.show(getChildFragmentManager(), "AddTaskDialogFragment");
        });

        view.estimatedTime.setOnClickListener(v -> {
            if (!isOngoing) {
                var dialogFragment = new EditEstimatedTimeDialogFragment();
                dialogFragment.show(getChildFragmentManager(), "EditTimeDialogFragment");
            }
        });

        view.startRoutineButton.setOnClickListener(x -> {
            if (activityModel.getCurrentRoutineTasks().isEmpty()) {
                var dialogFragment = InvalidStartDialogFragment.newInstance();
                dialogFragment.show(getParentFragmentManager(), "InvalidStartDialogFragment");
                return;
            }

            firstTimeStarting = false;

            activityModel.startCurrentRoutine();
            Log.d(TAG, "Start routine button pressed");

            timerRunning[0] = true;
            if (timer != null) {
                timer.cancel();
            }

            timer = new CountDownTimer(Integer.MAX_VALUE, 1000) {


                @Override
                public void onTick(long l) {
                    String timeText;
                    if(!isOngoing) {
                        timeText = "Cumulative: " + (int) Math.ceil(activityModel.getCumulativeTime() / 60.0) + "m\n"
                                + "Task: " + (int) Math.ceil((activityModel.getTaskTimeNoReset() + 0.0001) / 60.0) + "m";
                    } else {
                        timeText = "Cumulative: " + (activityModel.getCumulativeTime() / 60) + "m\n"
                                + "Task: " + (activityModel.getTaskTimeNoReset() / 60) + "m";
                    }
                    view.actualTime.setText(timeText);
                    if (!isOngoing) {
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
            if (!isOngoing) return;
            Log.d(TAG, "Fast forward timer button pressed");
            activityModel.addSeconds(15);
            curRoutine.manualAddTime(15);
        });

        view.pauseTimerButton.setOnClickListener(x -> {
            if (!curRoutine.getTimer().getOngoing()) return;
            activityModel.pauseTimer();
            curRoutine.pauseRoutineTimer();
            view.pauseTimerButton.setVisibility(View.INVISIBLE);
            view.restartTimerButton.setVisibility(View.VISIBLE);
        });

        view.restartTimerButton.setOnClickListener(v -> {
            if (curRoutine.getTimer().getOngoing()) return;
            activityModel.resumeTimer();
            curRoutine.pauseRoutineTimer();
            view.restartTimerButton.setVisibility(View.INVISIBLE);
            view.pauseTimerButton.setVisibility(View.VISIBLE);
        });

        view.stopRoutineButton.setOnClickListener(v -> {
            if (!isOngoing) return;
            activityModel.endCurrentRoutine();
            view.restartTimerButton.setVisibility(View.INVISIBLE);
            view.pauseTimerButton.setVisibility(View.VISIBLE);
        });

        return view.getRoot();
    }

    @Override
    public void onDestroyView() {
        curTasksSubject.removeObserver(tasksObserver);
        curRoutineSubject.removeObserver(routineObserver);
        currentRoutineOngoing.removeObserver(onGoingObserver);
        super.onDestroyView();
    }
}