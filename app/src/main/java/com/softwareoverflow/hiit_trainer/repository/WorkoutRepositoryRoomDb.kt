package com.softwareoverflow.hiit_trainer.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.softwareoverflow.hiit_trainer.data.Workout
import com.softwareoverflow.hiit_trainer.data.WorkoutDatabase
import com.softwareoverflow.hiit_trainer.data.mapper.toDTO
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO

class WorkoutRepositoryRoomDb(val context: Context) : IWorkoutRepository {

    private val database = WorkoutDatabase.getInstance(context.applicationContext)
    private val workoutDao = database.workoutDao
    private val exerciseTypeDao = database.exerciseTypeDao

    override fun getWorkoutById(workoutId: Long): LiveData<WorkoutDTO> {
        val liveData = MutableLiveData<WorkoutDTO>()

        Transformations.map(workoutDao.getWorkoutById(workoutId)) { workout: Workout ->
            liveData.value = workout.toDTO()
        }

        return liveData
    }

    // TODO maybe have a separate Repository object for exercise types if this one becomes cluttered
    override fun getAllExerciseTypes(): LiveData<List<ExerciseTypeDTO>> {
        val list: MutableList<ExerciseTypeDTO> = ArrayList()

        Transformations.map(exerciseTypeDao.getAllExerciseTypes()) {
            it.forEach {entity ->
                list.add(entity.toDTO())
            }
        }

        return MutableLiveData<List<ExerciseTypeDTO>>(list)
    }
}