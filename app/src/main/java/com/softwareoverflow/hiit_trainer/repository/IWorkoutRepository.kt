package com.softwareoverflow.hiit_trainer.repository

import androidx.lifecycle.LiveData
import com.softwareoverflow.hiit_trainer.data.dao.ExerciseTypeDao
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO

/**
 * Repository layer of abstraction from the backing data source
 */
interface IWorkoutRepository {

    // TODO consider making these suspend functions to prevent long running tasks blocking the UI thread. See the PERSISTANCE example code

    fun getExerciseTypeDao(): ExerciseTypeDao

    /**
     * Load the workout for a given id
     */
    fun getWorkoutById(workoutId: Long) : LiveData<WorkoutDTO>

    fun getAllExerciseTypes(): LiveData<List<ExerciseTypeDTO>>

    fun getExerciseTypeById(exerciseTypeId: Long) : LiveData<ExerciseTypeDTO>

    suspend fun createOrUpdateExerciseType(exerciseTypeDTO: ExerciseTypeDTO)
}