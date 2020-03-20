package com.softwareoverflow.hiit_trainer.repository

import androidx.lifecycle.LiveData
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO

/**
 * Repository layer of abstraction from the backing data source
 */
interface IWorkoutRepository {

    /**
     * Load the workout for a given id
     */
    // TODO consider making these suspend functions to prevent long running tasks blocking the UI thread. See the PERSISTANCE example code
    fun getWorkoutById(workoutId: Long) : LiveData<WorkoutDTO>

    fun getAllExerciseTypes(): LiveData<List<ExerciseTypeDTO>>

    fun getExerciseTypeById(exerciseTypeId: Long) : LiveData<ExerciseTypeDTO>

    fun createOrUpdateExerciseType(exerciseTypeDTO: ExerciseTypeDTO)
}