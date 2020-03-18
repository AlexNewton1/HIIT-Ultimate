package com.softwareoverflow.hiit_trainer.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.softwareoverflow.hiit_trainer.data.entity.WorkoutSetEntity

@Dao
internal interface WorkoutSetDao : BaseDao<WorkoutSetEntity> {

    @Query("SELECT * FROM WorkoutSet")
    fun getAllWorkoutSets(): LiveData<List<WorkoutSetEntity>>
}