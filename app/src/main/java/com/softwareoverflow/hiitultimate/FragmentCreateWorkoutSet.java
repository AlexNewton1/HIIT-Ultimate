package com.softwareoverflow.hiitultimate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.softwareoverflow.hiitultimate.database.dto.WorkoutSet;
import com.softwareoverflow.hiitultimate.database.entity.ExerciseTypeEntity;
import com.softwareoverflow.hiitultimate.database.entity.WorkoutSetEntity;
import com.softwareoverflow.hiitultimate.viewModel.ExerciseTypesViewModel;
import com.softwareoverflow.hiitultimate.viewModel.WorkoutViewModel;
import com.softwareoverflow.hiitultimate.viewModel.WorkoutViewModelFactory;
import com.softwareoverflow.hiitultimate.workout.ui.CreateWorkout_NumberPickerView;
import com.softwareoverflow.hiitultimate.workout.ui.ExerciseTypeViewPagerAdapter;

public class FragmentCreateWorkoutSet extends Fragment implements IWizardFragment {

    private ExerciseTypesViewModel exerciseTypesViewModel;
    private WorkoutViewModel workoutViewModel;

    private ViewPager exerciseTypeViewPager;
    private ExerciseTypeViewPagerAdapter exerciseTypePagerAdapter;

    private CreateWorkout_NumberPickerView workTimePicker, restTimePicker,
            numRepsPicker, recoveryTimePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_workout_set, container, false);

        setupViews(view);
        setupViewModel();

        return view;
    }

    private void setupViews(View view) {
        workTimePicker = view.findViewById(R.id.layout_workTime);
        restTimePicker = view.findViewById(R.id.layout_restTime);
        numRepsPicker = view.findViewById(R.id.layout_numReps);
        recoveryTimePicker = view.findViewById(R.id.layout_recoveryTime);
    }

    private void setupViewModel() {
        exerciseTypesViewModel = ViewModelProviders.of(this).get(ExerciseTypesViewModel.class);
        exerciseTypesViewModel.loadAllExerciseTypes().observe(this, exerciseTypeEntities -> {
            exerciseTypeViewPager = getView().findViewById(R.id.viewPager_exerciseType);
            exerciseTypePagerAdapter =
                    new ExerciseTypeViewPagerAdapter(getContext(), exerciseTypeEntities,
                            exerciseTypeViewPager.getWidth(), exerciseTypeViewPager.getHeight());
            exerciseTypeViewPager.setAdapter(exerciseTypePagerAdapter);
        });

        WorkoutViewModelFactory factory = new WorkoutViewModelFactory(
                this.getActivity().getApplication(), null);
        workoutViewModel = ViewModelProviders.of(getActivity(), factory).get(WorkoutViewModel.class);
    }



    @Override
    public void onWizardStepComplete() {
        WorkoutSetEntity workoutSetEntity;
        try {
             workoutSetEntity = new WorkoutSetEntity(
                    workTimePicker.getValue(),
                    restTimePicker.getValue(),
                    numRepsPicker.getValue(),
                    recoveryTimePicker.getValue());
        } catch (NumberFormatException nfe) {
            // TODO -- handle showing user the error message

            return;
        }

        ExerciseTypeEntity exerciseType = exerciseTypePagerAdapter.getItemAtPosition(
                exerciseTypeViewPager.getCurrentItem());

        WorkoutSet workoutSet = new WorkoutSet();
        workoutSet.setWorkoutSet(workoutSetEntity);
        workoutSet.setExerciseTypeEntity(exerciseType);

        workoutViewModel.addWorkoutSet(workoutSet);
    }

    @Override //TODO - handle setting hasUnsavedChanges - perhaps superclass almost everything!
    public boolean hasUnsavedChanges() {
        return false;
    }
}
