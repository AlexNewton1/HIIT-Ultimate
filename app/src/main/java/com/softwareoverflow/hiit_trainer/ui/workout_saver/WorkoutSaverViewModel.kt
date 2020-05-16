package com.softwareoverflow.hiit_trainer.ui.workout_saver

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softwareoverflow.hiit_trainer.repository.IWorkoutRepository
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.ui.view.LoadingSpinner
import kotlinx.coroutines.launch

class WorkoutSaverViewModel(val repo: IWorkoutRepository, val workout: WorkoutDTO) : ViewModel() {

    val workoutName = MutableLiveData(workout.name)

    private val _emptyNameWarning = MutableLiveData(false)
    val emptyNameWarning: LiveData<Boolean>
        get() = _emptyNameWarning

    private val _workoutSaved= MutableLiveData(false)
    val workoutSaved: LiveData<Boolean>
        get() = _workoutSaved

    fun emptyNameWarningShown(){
        _emptyNameWarning.value = false
    }

    fun saveWorkout() {
        val name = workoutName.value
        if(name.isNullOrBlank()) {
            _emptyNameWarning.value = true
        }
        else {
            workout.name = name
            viewModelScope.launch {
                LoadingSpinner.showLoadingIcon()

                val workoutSets = workout.workoutSets
                for (i in 0 until workoutSets.size) {
                    workoutSets[i].orderInWorkout = i
                }

                repo.createOrUpdateWorkout(workout)

                LoadingSpinner.hideLoadingIcon()

                _workoutSaved.value = true
            }
        }
    }
}