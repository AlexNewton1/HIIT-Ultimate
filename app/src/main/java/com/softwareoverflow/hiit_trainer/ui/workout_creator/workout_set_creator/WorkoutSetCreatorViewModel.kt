package com.softwareoverflow.hiit_trainer.ui.workout_creator.workout_set_creator

import androidx.lifecycle.*
import com.softwareoverflow.hiit_trainer.repository.IWorkoutRepository
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO
import com.softwareoverflow.hiit_trainer.ui.SortOrder
import com.softwareoverflow.hiit_trainer.ui.view.LoadingSpinner
import kotlinx.coroutines.launch

class WorkoutSetCreatorViewModel(
    workoutSetToEdit: WorkoutSetDTO,
    private val repo: IWorkoutRepository
) :
    ViewModel() {

    val workoutSet = MutableLiveData(workoutSetToEdit)

    val sortOrder = MutableLiveData(SortOrder.DESC)
    private val _searchFilter: MutableLiveData<String> = MutableLiveData("")

    private val _allExerciseTypes = repo.getAllExerciseTypes()
    private val _allExerciseTypesOrdered = MediatorLiveData<List<ExerciseTypeDTO>>()
    val allExerciseTypes: LiveData<List<ExerciseTypeDTO>>
        get() = _allExerciseTypesOrdered

    var selectedExerciseTypeId = MutableLiveData<Long?>(workoutSetToEdit.exerciseTypeDTO?.id)

    private val _unableToDeleteExerciseType = MutableLiveData("");
    val unableToDeleteExerciseType : LiveData<String>
        get() = _unableToDeleteExerciseType

    init {
        _allExerciseTypesOrdered.addSource(_allExerciseTypes) { exerciseTypes ->
            exerciseTypes?.let {
                _allExerciseTypesOrdered.value = getExerciseTypesToDisplay(exerciseTypes)
            }
        }

        _allExerciseTypesOrdered.addSource(sortOrder) {
            _allExerciseTypesOrdered.value =
                getExerciseTypesToDisplay(_allExerciseTypes.value ?: arrayListOf())
        }

        _allExerciseTypesOrdered.addSource(_searchFilter) {
            _allExerciseTypesOrdered.value =
                getExerciseTypesToDisplay(_allExerciseTypes.value ?: arrayListOf())
        }
    }

    fun unableToDeleteExerciseTypeWarningShown(){ _unableToDeleteExerciseType.value = null }

    private fun getExerciseTypesToDisplay(_exerciseTypes: List<ExerciseTypeDTO>): List<ExerciseTypeDTO> {
        var exerciseTypes = _exerciseTypes

        // Filter
        val filter = _searchFilter.value
        if(!filter.isNullOrBlank())
            exerciseTypes = exerciseTypes.filter { it.name!!.contains(filter, ignoreCase = true) }

        // Sort
        var sortedList = exerciseTypes.sortedBy { it.name }
        if (sortOrder.value == SortOrder.ASC)
            sortedList = sortedList.reversed()

        return sortedList
    }

    fun changeSortOrder() {
        sortOrder.value =
            if (sortOrder.value == SortOrder.ASC)
                SortOrder.DESC
            else SortOrder.ASC
    }

    fun setFilterText(filter: String) {
        _searchFilter.value = filter
    }

    fun setChosenExerciseTypeId(id: Long) {
        val exerciseType = _allExerciseTypes.value!!.first { it.id == id }
        workoutSet.value!!.exerciseTypeDTO = exerciseType
    }

    fun deleteExerciseTypeById(id: Long) {
        viewModelScope.launch {
            LoadingSpinner.showLoadingIcon()

            val exerciseType = allExerciseTypes.value!!.first { it.id == id }
            try {
                repo.deleteExerciseType(exerciseType)
            } catch (ex: IllegalStateException) {
                _unableToDeleteExerciseType.value = ex.message
            } finally {
                LoadingSpinner.hideLoadingIcon()
            }
        }
    }
}