package com.softwareoverflow.hiit_trainer.ui.workout_creator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softwareoverflow.hiit_trainer.repository.IWorkoutRepository
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO
import com.softwareoverflow.hiit_trainer.ui.view.LoadingSpinner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    val workoutSet: WorkoutSetDTO
        get() = _workoutSet.value ?: WorkoutSetDTO()

    init {
        viewModelScope.launch {
            LoadingSpinner.showLoadingIcon()

            if (id == null)
                _workout.value = WorkoutDTO()
            else
                _workout.value = repo.getWorkoutById(id)

            LoadingSpinner.hideLoadingIcon()
        }
    }

    fun removeWorkoutSetFromWorkout(position: Int){
        val workoutSets = _workout.value!!.workoutSets
        val dto =workoutSets.single { it.orderInWorkout == position }
       workoutSets.remove(dto)

        workoutSets.forEach {
            if(it.orderInWorkout!! > position)
                it.orderInWorkout = it.orderInWorkout!!-1
        }

        _workout.postValue(_workout.value) // Reassign the current value as the change is not automatically observed via LiveData
    }

    fun changeWorkoutSetOrder(fromPosition: Int, toPosition: Int){
        val currentWorkout = _workout.value!!.copy()

        val fromDTO = currentWorkout.workoutSets.single { it.orderInWorkout == fromPosition }
        val toDTO = currentWorkout.workoutSets.single { it.orderInWorkout == toPosition }

        val oldOrder = fromDTO.orderInWorkout
        fromDTO.orderInWorkout = toDTO.orderInWorkout
        toDTO.orderInWorkout = oldOrder

        _workout.postValue(currentWorkout)
    }

    fun setWorkoutSetToEdit(position: Int){
        _workoutSet.value = _workout.value!!.workoutSets.single { it.orderInWorkout == position }.copy()
    }

    fun setWorkoutName(name: String){
        _workout.value!!.name = name
    }

    /**
     * If the workoutSet has an id already present in the list, that entry will be updated.
     * If the workoutSet has an id not already in the list, the item will be appended to the list
     */
    fun addOrUpdateWorkoutSet(dto: WorkoutSetDTO) {
        val currentWorkout = _workout.value!!.copy()

        val index = currentWorkout.workoutSets.indexOfFirst {it.orderInWorkout == dto.orderInWorkout}
        if (index >= 0) {
            val workoutSets = currentWorkout.workoutSets.toCollection(mutableListOf())
            workoutSets.removeAt(index)
            workoutSets.add(index, dto)
            currentWorkout.workoutSets = workoutSets
        } else {
            dto.orderInWorkout = currentWorkout.workoutSets.size
            currentWorkout.workoutSets.add(dto)
        }

        _workout.postValue(currentWorkout)
        _workoutSet.value = null
    }

    fun createOrUpdateWorkout(onSave: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            LoadingSpinner.showLoadingIcon()
            repo.createOrUpdateWorkout(_workout.value!!)
            onSave()
            LoadingSpinner.hideLoadingIcon()
        }
    }
}
