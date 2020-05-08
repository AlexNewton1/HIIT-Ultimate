package com.softwareoverflow.hiit_trainer.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.softwareoverflow.hiit_trainer.data.WorkoutDatabase
import com.softwareoverflow.hiit_trainer.data.mapper.*
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class WorkoutRepositoryRoomDb(val context: Context) : IWorkoutRepository {

    private val database = WorkoutDatabase.getInstance(context.applicationContext)
    private val workoutDao = database.workoutDao
    private val exerciseTypeDao = database.exerciseTypeDao

    override fun getAllWorkouts(): LiveData<List<WorkoutDTO>> {
        return Transformations.switchMap(workoutDao.getAllWorkouts()) {
            MutableLiveData(it.toDTO())
        }
    }

    override suspend fun deleteWorkoutById(workoutId: Long) {
        val entity = getWorkoutById(workoutId).toWorkoutEntity()
        workoutDao.delete(entity)
    }

    override suspend fun getWorkoutById(workoutId: Long): WorkoutDTO {
        val workout = workoutDao.getWorkoutById(workoutId)
        Timber.d("getWorkoutById: id $workoutId, $workout")
        return workout.toDTO()
    }

    override suspend fun createOrUpdateWorkout(dto: WorkoutDTO) : Long {
        val workoutEntity = dto.toWorkoutEntity()
        val workoutSetEntityList = dto.workoutSets.toWorkoutSetEntity()

        return workoutDao.createOrUpdate(workoutEntity, workoutSetEntityList)
    }

    // TODO maybe have a separate Repository object for exercise types if this one becomes cluttered
    override fun getAllExerciseTypes(): LiveData<List<ExerciseTypeDTO>> {
        return Transformations.switchMap(exerciseTypeDao.getAllExerciseTypes()) {
            MutableLiveData(it.toExerciseTypeDTO())
        }
    }

    override fun getExerciseTypeById(exerciseTypeId: Long?): LiveData<ExerciseTypeDTO> {
        if (exerciseTypeId == null) {
            return MutableLiveData(ExerciseTypeDTO())
        }

        // TODO - fix the case where the number passed doesn't reflect a db record and causes an IllegalStateException
        return Transformations.switchMap(exerciseTypeDao.getExerciseTypeById(exerciseTypeId)) {
            MutableLiveData(it.toDTO())
        }
    }

    override suspend fun createOrUpdateExerciseType(exerciseTypeDTO: ExerciseTypeDTO): Long {
       return exerciseTypeDao.createOrUpdate(exerciseTypeDTO.toEntity())
    }

    override suspend fun deleteExerciseType(dto: ExerciseTypeDTO) {
        withContext(Dispatchers.IO){
            exerciseTypeDao.delete(dto.toEntity())
        }
    }
}