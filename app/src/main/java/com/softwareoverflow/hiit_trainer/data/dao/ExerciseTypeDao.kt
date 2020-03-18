package com.softwareoverflow.hiit_trainer.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.softwareoverflow.hiit_trainer.data.entity.ExerciseTypeEntity

@Dao
internal interface ExerciseTypeDao : BaseDao<ExerciseTypeEntity> {

    @Query("SELECT * FROM ExerciseType")
    fun getAllExerciseTypes(): LiveData<List<ExerciseTypeEntity>>
}