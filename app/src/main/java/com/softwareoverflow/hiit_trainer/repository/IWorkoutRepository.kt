package com.softwareoverflow.hiit_trainer.repository

import androidx.lifecycle.LiveData
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO

/**
 * Repository layer of abstraction from the backing data source
 */
interface IWorkoutRepository {

    //region Workout
    fun getAllWorkouts() : LiveData<List<WorkoutDTO>>

    suspend fun getWorkoutCount(): Int

    suspend fun deleteWorkoutById(workoutId: Long)

    suspend fun getWorkoutById(workoutId: Long) : WorkoutDTO

    suspend fun createOrUpdateWorkout(dto: WorkoutDTO) : Long
    //endregion

    // region ExerciseType
    fun getAllExerciseTypes(): LiveData<List<ExerciseTypeDTO>>

    fun getExerciseTypeById(exerciseTypeId: Long?) : LiveData<ExerciseTypeDTO>

    suspend fun createOrUpdateExerciseType(exerciseTypeDTO: ExerciseTypeDTO) :Long

    @Throws(IllegalStateException::class)
    suspend fun deleteExerciseType(dto: ExerciseTypeDTO)
    //endregion
}