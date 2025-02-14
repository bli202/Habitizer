package edu.ucsd.cse110.habitizer.app.ui.routine;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
    private static final String routine_title = "param1";
    private static final String estimate_time = "param2";

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
    public static RoutineFragment newInstance(String param1, String param2) {
        RoutineFragment fragment = new RoutineFragment();
        Bundle args = new Bundle();
        args.putString(routine_title, param1);
        args.putString(estimate_time, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String routineTitle = getArguments().getString(routine_title);
            String routineDuration = getArguments().getString(estimate_time);
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
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routine, container, false);
        this.adapter = new RoutineAdapter(requireContext(), List.of(), name -> {
            var EditTaskdialogFragment = EditTaskDialogFragment.newInstance(name);
            EditTaskdialogFragment.show(getParentFragmentManager(), "EditCardDialogFragment");},
            taskName -> {
                var DeleteTaskdialogFragment = DeleteTaskDialogFragment.newInstance(taskName);
                DeleteTaskdialogFragment.show(getParentFragmentManager(), "ConfirmDeleteCardDialogFragment");

        });

        activityModel.getOrderedTasks().observe(tasks -> {
            if (tasks == null) return;
            Log.d("RoutineFragment", "Updating adapter with " + tasks.size() + " tasks.");
            Log.d("RoutineFragment", "Before clearing, adapter has " + adapter.getCount() + " items.");
            adapter.clear();
            Log.d("RoutineFragment", "After clearing, adapter has " + adapter.getCount() + " items.");
            adapter.addAll(new ArrayList<>(tasks));
            Log.d("RoutineFragment", "After adding, adapter has " + adapter.getCount() + " items.");
            adapter.notifyDataSetChanged();
        });

        ListView taskList = view.findViewById(R.id.task_list_view);
        taskList.setAdapter(adapter);

        TextView titleView = view.findViewById(R.id.routine_title);
        TextView timeView = view.findViewById(R.id.estimated_time);

        Button addTask = view.findViewById(R.id.add_task_button);

        addTask.setOnClickListener(x -> {
            var dialogFragment = new AddTaskDialogFragment();
            Log.d("Routine fragment created", "hi");
            dialogFragment.show(getChildFragmentManager(), "AddTaskDialogFragment");
        });

        if (getArguments() != null) {
            String routineTitle = getArguments().getString(routine_title);
            String routineDuration = getArguments().getString(estimate_time);

            titleView.setText(routineTitle);
            timeView.setText(routineDuration + " min");
        }

        return view;
    }
}