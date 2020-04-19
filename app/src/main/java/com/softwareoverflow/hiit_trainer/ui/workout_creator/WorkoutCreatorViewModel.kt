package com.softwareoverflow.hiit_trainer.ui.workout_creator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softwareoverflow.hiit_trainer.repository.IWorkoutRepository
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel for creating / editing workouts.
 *
 * @param repo the repository to use for loading the workout
 * @param id the Id of the workout to load, or null if creating new
 */
class WorkoutCreatorViewModel(private val repo: IWorkoutRepository, id: Long?) : ViewModel() {

    private var _workout: MutableLiveData<WorkoutDTO> = MutableLiveData()
    val workout: LiveData<WorkoutDTO>
        get() = _workout

    private val _workoutSet: MutableLiveData<WorkoutSetDTO> = MutableLiveData()
    var workoutSet: WorkoutSetDTO = WorkoutSetDTO()
        get() {
            val dto = _workoutSet.value ?: WorkoutSetDTO()
            Timber.d("Workout workoutSet from MutableLiveData is $dto")
            return dto
        }
        private set

    init {
        if (id == null)
            _workout.value = WorkoutDTO()
        else
            _workout.value = repo.getWorkoutById(id).value
    }

    // TODO - 2 way bind the workout name edit text

    /**
     * If the workoutSet has an id already present in the list, that entry will be updated.
     * If the workoutSet has an id not already in the list, the item will be appended to the list
     */
    fun addOrUpdateWorkoutSet(dto: WorkoutSetDTO) {
        Timber.d("Workout addOrUpdate set with dto $dto")

        val currentWorkout = _workout.value!!.copy()

        Timber.d("Workout addOrUpdate set, currently we have ${currentWorkout.workoutSets}")
// TODO fix issue where the dto will get overwritten if you try and 2 workoutSet objects which are the same. Also need to think about how to track the workout set ordering within a workout when saving.
        // TODO might need to have some form of "workoutOrderIndex" to track the order...
        val index = currentWorkout.workoutSets.indexOf(dto)
        if (index >= 0) {
            val workoutSets = currentWorkout.workoutSets.toCollection(mutableListOf())
            workoutSets.removeAt(index)
            workoutSets.add(index, dto)
            currentWorkout.workoutSets = workoutSets
        } else {
            currentWorkout.workoutSets.add(dto)
        }

        Timber.d("Workout addOrUpdate set, now we have ${currentWorkout.workoutSets}")

        _workout.postValue(currentWorkout)
    }

    fun createOrUpdateWorkout() {
        CoroutineScope(Dispatchers.IO).launch {
            repo.createOrUpdateWorkout(_workout.value!!)
        }
    }
}
