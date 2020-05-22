package com.softwareoverflow.hiit_trainer.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.softwareoverflow.hiit_trainer.data.entity.ExerciseTypeEntity

@Dao
interface ExerciseTypeDao :
    BaseDao<ExerciseTypeEntity> {

    @Query("SELECT * FROM ExerciseType order by [name] asc")
    fun getAllExerciseTypes(): LiveData<List<ExerciseTypeEntity>>

    @Query("SELECT * FROM ExerciseType WHERE [id] = :exerciseTypeId")
    fun getExerciseTypeById(exerciseTypeId: Long) : LiveData<ExerciseTypeEntity>

    @Transaction
    suspend fun safeDelete(obj: ExerciseTypeEntity) {


    }

    @Query("SELECT workout.name FROM Workout workout JOIN WorkoutSet workoutSet WHERE workoutSet.exerciseTypeId = :exerciseTypeId ")
    suspend fun getWorkoutNamesUsingExerciseType(exerciseTypeId: Long) : List<String>
}