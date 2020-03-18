package com.softwareoverflow.hiit_trainer.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.softwareoverflow.hiit_trainer.data.WorkoutSet
import com.softwareoverflow.hiit_trainer.data.entity.WorkoutSetEntity

@Dao
interface WorkoutSetDao :
    BaseDao<WorkoutSetEntity> {

    @Query("SELECT * FROM WorkoutSet")
    fun getAllWorkoutSets(): LiveData<List<WorkoutSet>>
}