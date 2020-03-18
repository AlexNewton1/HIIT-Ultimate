package com.softwareoverflow.hiit_trainer.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.softwareoverflow.hiit_trainer.data.entity.ExerciseTypeEntity

@Dao
interface ExerciseTypeDao :
    BaseDao<ExerciseTypeEntity> {

    @Query("SELECT * FROM ExerciseType")
    fun getAllExerciseTypes(): LiveData<List<ExerciseTypeEntity>>
}