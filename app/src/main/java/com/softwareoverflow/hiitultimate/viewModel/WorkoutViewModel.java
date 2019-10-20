package com.softwareoverflow.hiitultimate.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.softwareoverflow.hiitultimate.database.dto.Workout;
import com.softwareoverflow.hiitultimate.database.dto.WorkoutSet;
import com.softwareoverflow.hiitultimate.repository.workout.WorkoutRepository;

public class WorkoutViewModel extends AndroidViewModel {

    private String LOG_TAG = WorkoutViewModel.class.getName();

    private WorkoutRepository repository;

    private LiveData<Workout> workoutLiveData;

    /**
     * The AndroidViewModel for creating and editing Workout objects.
     *
     * @param application - the Application object
     * @param workoutId - the id of the workout. Null if creating a new Workout object
     */
    public WorkoutViewModel(@NonNull Application application, Integer workoutId) {
        super(application);
        repository = WorkoutRepository.getInstance(application);

        if (workoutId != null) // load the workout if we have it
            workoutLiveData = repository.loadWorkoutById(workoutId);
        else
            workoutLiveData = repository.createNewWorkout();
    }


    /**
     * @return the LiveData<Workout> object for the Workout. This will be loaded from the
     * repository storage if a non-null workoutId was supplied to the constructor. An empty
     * Workout otherwise.
     */
    public LiveData<Workout> getWorkout() {
        return workoutLiveData;
    }

    public void addWorkoutSet(WorkoutSet set){
        repository.addWorkoutSet(set);
    }

    public void updateWorkoutSet(WorkoutSet newWorkoutSet, int index) {
        repository.updateWorkoutSet(newWorkoutSet, index);
    }

    public void deleteWorkoutSet (int index) {
        repository.deleteWorkoutSet(index);
    }
}
