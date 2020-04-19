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
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WorkoutRepositoryRoomDb(val context: Context) : IWorkoutRepository {

    private val database = WorkoutDatabase.getInstance(context.applicationContext)
    private val workoutDao = database.workoutDao
    private val workoutSetDao = database.workoutSetDao
    private val exerciseTypeDao = database.exerciseTypeDao

    override fun getWorkoutById(workoutId: Long): LiveData<WorkoutDTO> {
        val liveData = MutableLiveData<WorkoutDTO>()

        Transformations.map(workoutDao.getWorkoutById(workoutId)) { workout: Workout ->
            liveData.value = workout.toDTO()
        }

        return liveData
    }

    override suspend fun createOrUpdateWorkout(dto: WorkoutDTO) {
        return workoutDao.createOrUpdate(dto.toEntity())
    }

    override fun getWorkoutSetById(workoutSetId: Long?): LiveData<WorkoutSetDTO> {
        if (workoutSetId == null)
            return MutableLiveData(WorkoutSetDTO())

        return Transformations.switchMap(workoutSetDao.getWorkoutSetById(workoutSetId)) {
            MutableLiveData(it.toDTO())
        }
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