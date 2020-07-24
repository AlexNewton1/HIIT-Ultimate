package com.softwareoverflow.hiit_trainer.ui.workout_creator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softwareoverflow.hiit_trainer.repository.IWorkoutRepository
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * ViewModel for creating / editing workouts.
 *
 * @param repo the repository to use for loading the workout
 * @param id the Id of the workout to load, or null if creating new
 */
class WorkoutCreatorViewModel(private val repo: IWorkoutRepository, id: Long) : ViewModel() {

    private var _workout: MutableLiveData<WorkoutDTO> = MutableLiveData()
    val workout: LiveData<WorkoutDTO>
        get() = _workout

    private var _workoutSetIndex: Int? = null
    private val _workoutSet: MutableLiveData<WorkoutSetDTO> = MutableLiveData()
    val workoutSet: WorkoutSetDTO
        get() = _workoutSet.value ?: WorkoutSetDTO()

    var isNewWorkoutSet = true
        private set

    var showUnsavedChangesWarning = true
        private set
    private var _forceNavigateUp = MutableLiveData(false)
    val forceNavigateUp: LiveData<Boolean>
        get() = _forceNavigateUp

    init {
        viewModelScope.launch {
            if (id > 0L)
                _workout.value = repo.getWorkoutById(id).apply {
                    this.workoutSets.sortBy { it.orderInWorkout }
                }
            else
                _workout.value = WorkoutDTO()
        }
    }

    fun setUnsavedChangesWarningAccepted() {
        showUnsavedChangesWarning = false
    }

    fun forceNavigateUp(){
        _forceNavigateUp.value = true
    }

    fun getNumSavedWorkouts(): Int {
        return runBlocking {
            return@runBlocking repo.getWorkoutCount()
        }
    }

    fun removeWorkoutSetFromWorkout(position: Int) {
        val workoutSets = _workout.value!!.workoutSets
        val dto = workoutSets[position]
        workoutSets.remove(dto)

        _workout.postValue(_workout.value) // Reassign the current value as the change is not automatically observed via LiveData
    }

    fun changeWorkoutSetOrder(fromPosition: Int, toPosition: Int) {
        val currentWorkout = _workout.value!!.copy()

        if (fromPosition < 0 || toPosition < 0 ||
            fromPosition >= currentWorkout.workoutSets.size || toPosition >= currentWorkout.workoutSets.size
        )
            return // Just ignore any attempts to reorder items in an impossible fashion. See the [WorkoutSetListAdapter] for a to-do to stop this options being enabled.

        val workoutSets = currentWorkout.workoutSets
        val dto = workoutSets.removeAt(fromPosition)
        workoutSets.add(toPosition, dto)

        _workout.value = currentWorkout
    }

    fun setWorkoutSetToEdit(position: Int?) {
        if (position == null) {
            isNewWorkoutSet = true

            // Default the values to the previous WorkoutSet when creating a new WorkoutSet
            _workoutSet.value = WorkoutSetDTO().apply {
                val existingSets = _workout.value?.workoutSets
                if(existingSets?.any() == true){
                    val mostRecentSet = existingSets.last()
                    workTime = mostRecentSet.workTime
                    restTime = mostRecentSet.restTime
                    numReps = mostRecentSet.numReps
                    recoverTime = mostRecentSet.recoverTime
                }
            }

            _workoutSetIndex = null
        } else {
            isNewWorkoutSet = false

            _workoutSetIndex = position
            _workoutSet.value = _workout.value!!.workoutSets[position]
        }
    }

    /**
     * If the workoutSet has an id already present in the list, that entry will be updated.
     * If the workoutSet has an id not already in the list, the item will be appended to the list
     */
    fun addOrUpdateWorkoutSet(dto: WorkoutSetDTO) {
        val currentWorkout = _workout.value!!.copy()

        _workoutSetIndex.let {
            when {
                it != null -> {
                    val workoutSets = currentWorkout.workoutSets.toCollection(mutableListOf())
                    workoutSets.removeAt(it)
                    workoutSets.add(it, dto)
                }
                else -> {
                    currentWorkout.workoutSets.add(dto)
                }
            }
        }
        _workout.postValue(currentWorkout)

        _workoutSetIndex = null
        _workoutSet.value = null
    }

    fun setRepeatCount(repeatCount: Int, recoveryTime: Int){
        val workoutToEdit = workout.value!!
        workoutToEdit.numReps = repeatCount
        workoutToEdit.recoveryTime = recoveryTime

        _workout.value = workoutToEdit
    }
}

