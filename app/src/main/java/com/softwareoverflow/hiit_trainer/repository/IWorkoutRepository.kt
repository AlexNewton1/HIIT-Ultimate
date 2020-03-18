package com.softwareoverflow.hiit_trainer.repository

import androidx.lifecycle.LiveData
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO

/**
 * Repository layer of abstraction from the backing data source
 */
abstract class WorkoutRepository {

    /**
     * Load the workout for a given id
     */
    abstract fun getWorkoutById(workoutId: Long) : LiveData<WorkoutDTO>
}