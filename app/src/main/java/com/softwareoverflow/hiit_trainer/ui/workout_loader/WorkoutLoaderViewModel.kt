package com.softwareoverflow.hiit_trainer.ui.workout_loader

import androidx.lifecycle.*
import com.softwareoverflow.hiit_trainer.repository.IWorkoutRepository
import com.softwareoverflow.hiit_trainer.ui.SortOrder
import com.softwareoverflow.hiit_trainer.ui.upgrade.UpgradeManager
import com.softwareoverflow.hiit_trainer.ui.view.LoadingSpinner
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.workout.WorkoutLoaderDomainObject
import kotlinx.coroutines.launch
import timber.log.Timber

class WorkoutLoaderViewModel(private val repo: IWorkoutRepository) : ViewModel() {


    val sortOrder = MutableLiveData(SortOrder.ASC)
    private val _searchFilter = MutableLiveData("")

    private val _workouts = repo.getAllWorkouts()
    private val _workoutsSorted = MediatorLiveData<List<WorkoutLoaderDomainObject>>()
    val workouts: LiveData<List<WorkoutLoaderDomainObject>>
        get() = _workoutsSorted

    init {
        _workoutsSorted.addSource(_workouts) {
            _workoutsSorted.value = getWorkoutsToDisplay()
        }

        _workoutsSorted.addSource(sortOrder) {
            _workoutsSorted.value = getWorkoutsToDisplay()
        }

        _workoutsSorted.addSource(_searchFilter){
            _workoutsSorted.value = getWorkoutsToDisplay()
        }
    }

    fun changeSortOrder() {
        sortOrder.value =
            if (sortOrder.value == SortOrder.ASC)
                SortOrder.DESC
            else SortOrder.ASC
    }

    fun setFilterText(filterText: String) {
        _searchFilter.value = filterText
    }

    private fun getWorkoutsToDisplay() : List<WorkoutLoaderDomainObject>{
        var workouts = _workouts.value ?: arrayListOf()

        // Filter
        val filter = _searchFilter.value
        if(!filter.isNullOrBlank())
        workouts = workouts.filter { it.name.contains(filter, ignoreCase = true) }

        // Sort
        workouts = workouts.sortedBy { it.name }
        if (sortOrder.value == SortOrder.ASC)
            workouts = workouts.reversed()

        val domainObjects = workouts.map { WorkoutLoaderDomainObject(it) }.toMutableList()

        if(!UpgradeManager.hasUserUpgraded && _searchFilter.value.isNullOrEmpty()){
            Timber.d("Loading: not upgraded & empty search. Checking to add some stuff... $domainObjects")
            while(domainObjects.size < UpgradeManager.maxSavedWorkoutsFreeVersion)
                domainObjects.add(WorkoutLoaderDomainObject.placeholderUnlocked)

            Timber.d("Loading: Adding placeholderLocked")
            domainObjects.add(WorkoutLoaderDomainObject.placeholderLocked)
        }

        Timber.d("Loading: Returning domain objects $domainObjects")
        return domainObjects
    }

    fun deleteWorkout(id: Long){
        viewModelScope.launch {
            LoadingSpinner.showLoadingIcon()
            repo.deleteWorkoutById(id)
            LoadingSpinner.hideLoadingIcon()
        }
    }
}

