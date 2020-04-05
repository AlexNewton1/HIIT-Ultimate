package com.softwareoverflow.hiit_trainer.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.softwareoverflow.hiit_trainer.data.Workout
import com.softwareoverflow.hiit_trainer.data.WorkoutDatabase
import com.softwareoverflow.hiit_trainer.data.mapper.toDTO
import com.softwareoverflow.hiit_trainer.data.mapper.toEntity
import com.softwareoverflow.hiit_trainer.data.mapper.toExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

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
        return Transformations.switchMap(exerciseTypeDao.getAllExerciseTypes()) {
            MutableLiveData<List<ExerciseTypeDTO>>(it.toExerciseTypeDTO())
        }
    }

    override fun getExerciseTypeById(exerciseTypeId: Long?): LiveData<ExerciseTypeDTO> {
        Timber.d("Repository: Trying to load exercise type with id $exerciseTypeId")

        if(exerciseTypeId == null){
            return MutableLiveData(ExerciseTypeDTO())
        }

        // TODO - fix the case where the number passed doesn't reflect a db record and causes an IllegalStateException
        val et = exerciseTypeDao.getExerciseTypeById(exerciseTypeId)
        return Transformations.switchMap(et) {
            MutableLiveData<ExerciseTypeDTO>(it.toDTO())
        }
    }

    override suspend fun createOrUpdateExerciseType(exerciseTypeDTO: ExerciseTypeDTO) {
        Timber.d("Repository: CREATE $exerciseTypeDao")

        withContext(Dispatchers.IO) {
            launch {
                val id = exerciseTypeDao.createOrUpdate(exerciseTypeDTO.toEntity())
                Timber.d("Repository: Saved with id $id")
            }
        }
    }
}