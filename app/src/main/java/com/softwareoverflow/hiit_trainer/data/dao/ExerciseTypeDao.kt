package com.softwareoverflow.hiit_trainer.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.softwareoverflow.hiit_trainer.data.entity.ExerciseTypeEntity

@Dao
interface ExerciseTypeDao :
    BaseDao<ExerciseTypeEntity> {

    @Query("SELECT * FROM ExerciseType order by [name] asc")
    fun getAllExerciseTypes(): LiveData<List<ExerciseTypeEntity>>

    @Query("SELECT * FROM ExerciseType WHERE [id] = :exerciseTypeId")
    fun getExerciseTypeById(exerciseTypeId: Long) : LiveData<ExerciseTypeEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: ExerciseTypeEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(entity: ExerciseTypeEntity)

    @Transaction
    override suspend fun createOrUpdate(obj: ExerciseTypeEntity): Long {
        var insertedId = insert(obj)
        if (insertedId == -1L) {
            update(obj)
            insertedId = obj.id
        }

        return insertedId
    }

    @Query("SELECT workout.name FROM Workout workout JOIN WorkoutSet workoutSet WHERE workoutSet.exerciseTypeId = :exerciseTypeId ")
    suspend fun getWorkoutNamesUsingExerciseType(exerciseTypeId: Long) : List<String>
}