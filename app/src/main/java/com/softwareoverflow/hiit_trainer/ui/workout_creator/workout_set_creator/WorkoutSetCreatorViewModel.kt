package com.softwareoverflow.hiit_trainer.ui.workout_creator.workout_set_creator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softwareoverflow.hiit_trainer.repository.IWorkoutRepository
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO
import com.softwareoverflow.hiit_trainer.ui.utils.SortOrder
import kotlinx.coroutines.launch

class WorkoutSetCreatorViewModel (
    workoutSetToEdit: WorkoutSetDTO,
    private val repo: IWorkoutRepository
) :
    ViewModel() {

    val _workoutSet = MutableLiveData(workoutSetToEdit)
    val workoutSet: LiveData<WorkoutSetDTO> get() = _workoutSet

    val sortOrder = MutableLiveData(SortOrder.DESC)
    private val _searchFilter: MutableLiveData<String> = MutableLiveData("")

    private val _allExerciseTypes = repo.getAllExerciseTypes()
    private val _allExerciseTypesOrdered = MediatorLiveData<List<ExerciseTypeDTO>>()
    val allExerciseTypes: LiveData<List<ExerciseTypeDTO>>
        get() = _allExerciseTypesOrdered

    private val _selectedExerciseTypeId = MutableLiveData<Long?>(workoutSet.value?.exerciseTypeDTO?.id)
    val selectedExerciseTypeId: LiveData<Long?> get() = _selectedExerciseTypeId

    private val _unableToDeleteExerciseType = MutableLiveData("")
    val unableToDeleteExerciseType: LiveData<String>
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

    fun unableToDeleteExerciseTypeWarningShown() {
        _unableToDeleteExerciseType.value = null
    }

    private fun getExerciseTypesToDisplay(_exerciseTypes: List<ExerciseTypeDTO>): List<ExerciseTypeDTO> {
        var exerciseTypes = _exerciseTypes

        // Filter
        val filter = _searchFilter.value
        if (!filter.isNullOrBlank())
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

    fun setChosenExerciseTypeId(id: Long?) {

        val exerciseType = _allExerciseTypes.value!!.firstOrNull { it.id == id }
        workoutSet.value!!.exerciseTypeDTO = exerciseType

        _selectedExerciseTypeId.value = id
    }

    fun deleteExerciseTypeById(id: Long, currentWorkoutSets: List<WorkoutSetDTO>) {
        setChosenExerciseTypeId(null)

        viewModelScope.launch {
            val exerciseType = allExerciseTypes.value!!.first { it.id == id }

            val currentExerciseTypes = currentWorkoutSets.map { it.exerciseTypeDTO!! }
            if (currentExerciseTypes.contains(exerciseType)) {
                _unableToDeleteExerciseType.value =
                    "Unable to delete Exercise Type. It's used in this workout!"
                return@launch
            }

            try {
                repo.deleteExerciseType(exerciseType)
            } catch (ex: IllegalStateException) {
                _unableToDeleteExerciseType.value = ex.message
            }
        }
    }
}