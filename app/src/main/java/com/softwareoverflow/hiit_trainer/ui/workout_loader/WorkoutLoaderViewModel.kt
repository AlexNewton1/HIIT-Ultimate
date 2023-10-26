package com.softwareoverflow.hiit_trainer.ui.workout_loader

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softwareoverflow.hiit_trainer.repository.IWorkoutRepository
import com.softwareoverflow.hiit_trainer.repository.billing.BillingRepository
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.workout.WorkoutLoaderDomainObject
import kotlinx.coroutines.launch

class WorkoutLoaderViewModel(
    private val billingRepo: BillingRepository,
    private val workoutRepo: IWorkoutRepository,
    private val placeholderUnlocked: WorkoutLoaderDomainObject,
    private val placeholderLocked: WorkoutLoaderDomainObject,
) :
    ViewModel() {

    private val _workouts = workoutRepo.getAllWorkouts()
    private val _workoutsSorted = MediatorLiveData<List<WorkoutLoaderDomainObject>>()
    val workouts: LiveData<List<WorkoutLoaderDomainObject>>
        get() = _workoutsSorted


    init {
        viewModelScope.launch {
            _workoutsSorted.addSource(_workouts) {
                _workoutsSorted.value = getWorkoutsToDisplay()
            }
        }
    }

    private fun getWorkoutsToDisplay(): List<WorkoutLoaderDomainObject> {
        val workouts = _workouts.value ?: arrayListOf()
        val domainObjects = workouts.map { WorkoutLoaderDomainObject(it) }.toMutableList()

        if ((billingRepo.proUpgradeLiveData.value?.entitled != true)) {
            while (domainObjects.size < billingRepo.getMaxWorkoutSlots())
                domainObjects.add(placeholderUnlocked)

            domainObjects.add(placeholderLocked)
        }

        return domainObjects
    }

    fun deleteWorkout(id: Long) {
        viewModelScope.launch {
            workoutRepo.deleteWorkoutById(id)
        }
    }
}

