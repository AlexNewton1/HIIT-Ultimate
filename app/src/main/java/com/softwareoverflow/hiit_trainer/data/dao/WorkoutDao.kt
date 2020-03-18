package com.softwareoverflow.hiit_trainer.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.softwareoverflow.hiit_trainer.data.entity.WorkoutEntity

@Dao
internal interface WorkoutDao : BaseDao<WorkoutEntity> {

    @Query("SELECT * FROM Workout")
    fun getAllWorkouts(): LiveData<List<WorkoutEntity>>
}