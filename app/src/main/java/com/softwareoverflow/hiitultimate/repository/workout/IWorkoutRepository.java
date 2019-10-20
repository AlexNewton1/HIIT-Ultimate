package com.softwareoverflow.hiitultimate.repository.workout;

import androidx.lifecycle.MutableLiveData;

import com.softwareoverflow.hiitultimate.database.dto.Workout;
import com.softwareoverflow.hiitultimate.database.dto.WorkoutSet;

public interface IWorkoutRepository {

    /**
     * @return a new in memory Workout object which can be edited
     */
    MutableLiveData<Workout> createNewWorkout();

    /**
     *
     * @param workoutId - the id of the Workout to copy from the storage.
     * @return - a copy of the Workout object from the storage which can be edited
     */
    MutableLiveData<Workout> loadWorkoutById(int workoutId);

    /**
     * Deletes the Workout from the persistent store. This cannot be undone
     */
    void deleteWorkout(Workout workout);

    /**
     * Adds a new WorkoutSet object to the in memory Workout object.
     * Use saveChanges to persist to storage
     */
    void addWorkoutSet(WorkoutSet workoutSet);

    /**
     * @param newWorkoutSet - the new WorkoutSet object
     * @param index - the index te replace
     */
    void updateWorkoutSet(WorkoutSet newWorkoutSet, int index);

    /**
     * @param workoutSet - the WorkoutSet to be removed from the in memory Workout object
     */
    void deleteWorkoutSet(WorkoutSet workoutSet);

    /**
     * @param index - the index of the WorkoutSet to delete from the in memory Workout object
     */
    void deleteWorkoutSet(int index);

    /**
     * @param workout - the Workout object to persist to storage. This will perform and update if
     *                the Workout is found in the persistent storage, or insert it if not.
     */
    void saveChanges(Workout workout);
}
