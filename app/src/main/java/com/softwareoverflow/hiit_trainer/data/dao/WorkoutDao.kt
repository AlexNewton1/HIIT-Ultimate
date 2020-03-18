package com.softwareoverflow.hiit_trainer.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.softwareoverflow.hiit_trainer.data.Workout
import com.softwareoverflow.hiit_trainer.data.entity.WorkoutEntity

@Dao
interface WorkoutDao :
    BaseDao<WorkoutEntity> {

    @Query("SELECT * FROM Workout WHERE id = :workoutId")
    fun getWorkoutById(workoutId: Long) : LiveData<Workout>

    @Query("SELECT * FROM Workout")
    fun getAllWorkouts(): LiveData<List<Workout>>
}