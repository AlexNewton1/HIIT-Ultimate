/*
package com.softwareoverflow.hiitultimate.viewModel;


*/
/* TODO -- work out if this is needed.
    Should all creating / editing be done from a single view model?
    Should there be a separate fragment for handling WorkoutSet objects

    + -> Makes the ViewMdoels simpler
    + -> Might be able to use some inheritance to handle the checking of Id values passed in and correctly creating / editing based on nullity

    - -> involves extra fragment and required movement between them.
 *//*


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.softwareoverflow.hiitultimate.database.dto.Workout;
import com.softwareoverflow.hiitultimate.database.dto.WorkoutSet;
import com.softwareoverflow.hiitultimate.repository.workout.IWorkoutRepository;
import com.softwareoverflow.hiitultimate.repository.workout.WorkoutRepository;

public class WorkoutSetViewModel extends AndroidViewModel {

    private IWorkoutRepository workoutRepo;
    private Integer workoutId;

    private LiveData<Workout> workoutLiveData;

    public WorkoutSetViewModel(@NonNull Application application, Integer workoutId) {
        super(application);
        workoutRepo = WorkoutRepository.getInstance(application);

        if(workoutId == null)
            workoutLiveData = workoutRepo.createNewWorkout();
        else
            workoutLiveData = workoutRepo.loadWorkoutById(workoutId);
    }


    */
/**
     * @return the LiveData<Workout> object for the Workout. This will be loaded from the
     * repository storage if a non-null workoutId was supplied to the constructor. An empty
     * Workout otherwise.
     *//*

    public LiveData<Workout> getWorkout() {
        return workoutLiveData;
    }

    public void addWorkoutSet(WorkoutSet set){
        workoutRepo.addWorkoutSet(set);
    }

    public void updateWorkoutSet(WorkoutSet newWorkoutSet, int index) {
        workoutRepo.updateWorkoutSet(newWorkoutSet, index);
    }

    public void deleteWorkoutSet (WorkoutSet toDelete) {
        workoutRepo.deleteWorkoutSet(toDelete);
    }
}
*/
