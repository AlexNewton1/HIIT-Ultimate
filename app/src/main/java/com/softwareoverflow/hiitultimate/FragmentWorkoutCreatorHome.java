package com.softwareoverflow.hiitultimate;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.softwareoverflow.hiitultimate.database.dto.Workout;
import com.softwareoverflow.hiitultimate.viewModel.WorkoutViewModel;
import com.softwareoverflow.hiitultimate.viewModel.WorkoutViewModelFactory;

// TODO - handle bundles of data from intents coming into the ActivityWorkoutCreator
public class FragmentWorkoutCreatorHome extends Fragment implements IWizardFragment{

    private WorkoutViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout_creator_home, container, false);

        View.OnClickListener listener = Navigation.createNavigateOnClickListener(R.id.action_createWorkoutSet);
        FloatingActionButton fab = view.findViewById(R.id.fab_create_new_workout_set);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_createWorkoutSet);
            }
        });

        registerViewModel();

        return view;
    }

    private void registerViewModel() {
        WorkoutViewModelFactory factory = new WorkoutViewModelFactory(
                this.getActivity().getApplication(), null);
        viewModel = ViewModelProviders.of(getActivity(), factory).get(WorkoutViewModel.class);

        viewModel.getWorkout().observe(getViewLifecycleOwner(), this::onWorkoutChanged);
    }

    private void onWorkoutChanged(Workout workout) {
        Log.d("debugger2", "The workout has changed!");
    }

    @Override
    public void onWizardStepComplete() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override //TODO - handle setting hasUnsavedChanges - perhaps superclass almost everything!
    public boolean hasUnsavedChanges() {
        return false;
    }
}
