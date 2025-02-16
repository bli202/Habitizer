package edu.ucsd.cse110.habitizer.app.ui.routine;

import static android.graphics.Paint.STRIKE_THRU_TEXT_FLAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import android.graphics.Paint;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.ucsd.cse110.habitizer.app.MainViewModel;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.AddTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.DeleteTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.ui.routine.dialog.EditTaskDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoutineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoutineFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String routine_title = "param1";
//    private static final String estimate_time = "param2";

    private MainViewModel activityModel;
    private RoutineAdapter adapter;

    public RoutineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RoutineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public RoutineFragment newInstance() {
        RoutineFragment fragment = new RoutineFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            String routineTitle = getArguments().getString(routine_title);
//            String routineDuration = getArguments().getString(estimate_time);
        }

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

//        this.adapter = new CardListAdapter(requireContext(), List.of(), id -> {
//            var dialogFragment = ConfirmDeleteCardDialogFragment.newInstance(id);
//            dialogFragment.show(getParentFragmentManager(), "ConfirmDeleteCardDialogFragment");
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_routine, container, false);
        Log.d("Routine Fragment", "onCreateView called and adapter made with " + activityModel.getCurRoutine().getValue());
        this.adapter = new RoutineAdapter(requireContext(),
                activityModel.getCurRoutine().getValue(),
                name -> {
                    var EditTaskdialogFragment = EditTaskDialogFragment.newInstance(name);
                    EditTaskdialogFragment.show(getParentFragmentManager(), "EditCardDialogFragment");},
                taskName -> {
                    var DeleteTaskdialogFragment = DeleteTaskDialogFragment.newInstance(taskName);
                    DeleteTaskdialogFragment.show(getParentFragmentManager(), "ConfirmDeleteCardDialogFragment");

        });


        activityModel.getOrderedTasks().observe(tasks -> {
            if (tasks == null) return;
            adapter.notifyDataSetChanged();
        });

        ListView taskList = view.findViewById(R.id.task_list_view);
        taskList.setAdapter(adapter);

        TextView titleView = view.findViewById(R.id.routine_title);
        TextView timeView = view.findViewById(R.id.estimated_time);
        TextView actualTimeView = view.findViewById(R.id.actual_time);

        Button addTask = view.findViewById(R.id.add_task_button);
        Button startRoutine = view.findViewById(R.id.start_routine_button);
        FloatingActionButton ffButton = view.findViewById(R.id.fast_forward_timer_button);
        FloatingActionButton pauseTimerButton = view.findViewById(R.id.pause_timer_button);

        activityModel.getCompleted().observe(completed -> {
            Log.d("HabitizerApplication", "COMPLETED OSVEVSIUDUDSIXOJH!!!");
            if(completed == null) {
                Log.d("HabitizerApplication", "COMPLETED = NULL");
                return;
            } else {
                Log.d("HabitizerApplication", "COMPLETION: " + completed);
            }
//            Log.d("HabitizerApplication",completed.toString());
            if(completed) {
                addTask.setVisibility(View.VISIBLE);
                startRoutine.setVisibility(View.VISIBLE);
            }
        });

        addTask.setOnClickListener(x -> {
            var dialogFragment = new AddTaskDialogFragment();
            dialogFragment.show(getChildFragmentManager(), "AddTaskDialogFragment");
        });

        startRoutine.setOnClickListener(x -> {
            var routine = activityModel.getCurRoutine().getValue();
//            routine.startRoutine();

            activityModel.startTime();
            addTask.setVisibility(View.INVISIBLE);
            startRoutine.setVisibility(View.INVISIBLE);


            new CountDownTimer(Integer.MAX_VALUE, 1000) {

                @Override
                public void onTick(long l) {
                    actualTimeView.setText(String.valueOf(routine.getElapsedTimeSecs()));
                    if(!routine.getongoing()) {
                        addTask.setVisibility(View.VISIBLE);
                        startRoutine.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFinish() {

                }
            }.start();
        });

        ffButton.setOnClickListener(x -> {
            if(!activityModel.getCurRoutine().getValue().getongoing()) return;
            activityModel.getCurRoutine().getValue().manualAddTime(30);
        });

        pauseTimerButton.setOnClickListener(x -> {
            if(!activityModel.getCurRoutine().getValue().getongoing()) return;
            activityModel.getCurRoutine().getValue().pauseRoutineTimer();
        });

        if (getArguments() != null) {
//            String routineTitle = getArguments().getString(routine_title);
//            String routineDuration = getArguments().getString(estimate_time);
//            activityModel.switchRoutine(routineTitle);

            titleView.setText(activityModel.getCurRoutine().getValue().getName());
            timeView.setText(activityModel.getCurRoutine().getValue().getEstimatedTime() + "min");
        }

        return view;
    }
}