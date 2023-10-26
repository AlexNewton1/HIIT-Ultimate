package com.softwareoverflow.hiit_trainer.ui.workout_saver

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softwareoverflow.hiit_trainer.repository.IWorkoutRepository
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.ui.upgrade.BillingViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@HiltViewModel
open class WorkoutSaverViewModel @Inject constructor(
    private val billingViewModel: BillingViewModel,
    private val workoutRepo: IWorkoutRepository,
) : ViewModel() {
    val savedWorkouts = workoutRepo.getAllWorkouts()

    private val _currentSelectedId = MutableStateFlow<Long?>(null)
    val currentSelectedId: StateFlow<Long?> get() = _currentSelectedId

    private var isInitialized = false

    fun setCurrentlySelectedId(selected: Long?) {
        _currentSelectedId.value = selected
    }

    fun initialize(idToOverwrite: Long?) {
        if (!isInitialized) {
            setCurrentlySelectedId(idToOverwrite)
            isInitialized = true
        }
    }

    private val numWorkoutsSaved: MutableLiveData<Int> = MutableLiveData(Int.MAX_VALUE)

    init {
        viewModelScope.launch {
            numWorkoutsSaved.value = workoutRepo.getWorkoutCount()
        }
    }

    fun canSaveNewWorkout(): Boolean {
        val isNewWorkout = currentSelectedId.value == null
        val maxSlots = billingViewModel.getMaxWorkoutSlots()
        val noSlotsLeft = numWorkoutsSaved.value!!.toInt() >= maxSlots
        return  !(isNewWorkout && noSlotsLeft)
    }

    open fun saveWorkout(
        workout: WorkoutDTO,
        newWorkoutName: String,
        idToOverwrite: Long? = null
    ): Boolean {
        if (idToOverwrite == null && numWorkoutsSaved.value!!.toInt() >= billingViewModel.getMaxWorkoutSlots()) {
            return false
        }

        if (newWorkoutName.length > 30 || newWorkoutName.isBlank()) {
            return false
        }


        val workoutSets = workout.workoutSets.sortedBy { it.orderInWorkout }.toMutableList()
        for (i in 0 until workoutSets.size) {
            workoutSets[i].orderInWorkout = i
        }

        val workoutToSave = workout.copy(
            id = idToOverwrite,
            name = newWorkoutName,
            workoutSets = workoutSets
        )
        return runBlocking {
            workoutRepo.createOrUpdateWorkout(workoutToSave)
            return@runBlocking true
        }
    }

    fun upgrade(activity: Activity) {
        billingViewModel.purchasePro(activity)
    }
}