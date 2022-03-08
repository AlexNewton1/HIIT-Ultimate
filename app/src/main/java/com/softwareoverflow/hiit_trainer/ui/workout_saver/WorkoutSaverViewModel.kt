package com.softwareoverflow.hiit_trainer.ui.workout_saver

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softwareoverflow.hiit_trainer.repository.IWorkoutRepository
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.ui.upgrade.BillingViewModel
import kotlinx.coroutines.launch


open class WorkoutSaverViewModel(
    private val billingViewModel: BillingViewModel,
    private val workoutRepo: IWorkoutRepository,
    val workout: WorkoutDTO
) : ViewModel() {

    val newWorkoutName = MutableLiveData(workout.name)

    private val _emptyNameWarning = MutableLiveData(false)
    val emptyNameWarning: LiveData<Boolean>
        get() = _emptyNameWarning

    private val _workoutSaved = MutableLiveData(false)
    val workoutSaved: LiveData<Boolean>
        get() = _workoutSaved

    private val _noWorkoutSlotsRemainingWarning = MutableLiveData(false)
    val noWorkoutSlotsRemainingWarning: LiveData<Boolean>
        get() = _noWorkoutSlotsRemainingWarning

    private val _nameTooLongWarning = MutableLiveData(false)
    val nameTooLongWarning: LiveData<Boolean>
        get() = _nameTooLongWarning

    private val numWorkoutsSaved: MutableLiveData<Int> = MutableLiveData(Int.MAX_VALUE)

    init {
        viewModelScope.launch {
            numWorkoutsSaved.value = workoutRepo.getWorkoutCount()
        }
    }

    fun emptyNameWarningShown() {
        _emptyNameWarning.value = false
    }

    fun noWorkoutSlotsWarningShown() {
        _noWorkoutSlotsRemainingWarning.value = false
    }

    fun nameTooLongWarningShown() {
        _nameTooLongWarning.value = false
    }

    open fun saveWorkout(isOverwriting: Boolean = false) {
        if (!isOverwriting && numWorkoutsSaved.value!!.toInt() >= billingViewModel.getMaxWorkoutSlots()) {
            _noWorkoutSlotsRemainingWarning.value = true
            return
        }

        if (newWorkoutName.value!!.length > 30) {
            _nameTooLongWarning.value = true
            return
        }

        val name = newWorkoutName.value
        if (name.isNullOrBlank()) {
            _emptyNameWarning.value = true
        } else {
            workout.name = name
            viewModelScope.launch {
                val workoutSets = workout.workoutSets
                for (i in 0 until workoutSets.size) {
                    workoutSets[i].orderInWorkout = i
                }

                workoutRepo.createOrUpdateWorkout(workout)
                _workoutSaved.value = true
            }
        }
    }

    fun upgrade(activity: Activity) {
        billingViewModel.purchasePro(activity)
    }
}