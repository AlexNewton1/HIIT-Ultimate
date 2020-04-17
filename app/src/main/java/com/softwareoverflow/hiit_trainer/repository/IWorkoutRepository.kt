package com.softwareoverflow.hiit_trainer.repository

import androidx.lifecycle.LiveData
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO

/**
 * Repository layer of abstraction from the backing data source
 */
interface IWorkoutRepository {

    // TODO consider making these suspend functions to prevent long running tasks blocking the UI thread. See the PERSISTANCE example code

    //region Workout
    /**
     * Load the workout for a given id
     */
    fun getWorkoutById(workoutId: Long) : LiveData<WorkoutDTO>
    //endregion


    // region WorkoutSet
    fun getWorkoutSetById(workoutSetId: Long?) : LiveData<WorkoutSetDTO>

    //endRegion

    // region ExerciseType
    /**
     * Load all the saved exercise types
     */
    fun getAllExerciseTypes(): LiveData<List<ExerciseTypeDTO>>

    /**
     * Load the exercise type for a given id
     */
    fun getExerciseTypeById(exerciseTypeId: Long?) : LiveData<ExerciseTypeDTO>

    /**
     * Persists the exercise type to the database. Overwrites existing entry if present
     */
    suspend fun createOrUpdateExerciseType(exerciseTypeDTO: ExerciseTypeDTO) :Long

    suspend fun deleteExerciseType(dto: ExerciseTypeDTO)
    //endregion
}