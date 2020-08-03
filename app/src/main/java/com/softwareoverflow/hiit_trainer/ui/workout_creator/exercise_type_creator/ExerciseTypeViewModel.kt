package com.softwareoverflow.hiit_trainer.ui.workout_creator.exercise_type_creator

import android.content.Context
import androidx.lifecycle.*
import com.softwareoverflow.hiit_trainer.repository.IWorkoutRepository
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.ui.workout_creator.workout_set_creator.WorkoutSetCreatorViewModel
import kotlinx.coroutines.launch

class ExerciseTypeViewModel(
    val repository: IWorkoutRepository,
    val id: Long?,
    private val workoutSetCreatorViewModel: WorkoutSetCreatorViewModel
) : ViewModel() {

    private val _exerciseType = repository.getExerciseTypeById(id)

    val exerciseTypeName = Transformations.map(_exerciseType) { it?.name }
    val iconName = Transformations.map(_exerciseType) { it?.iconName }
    val colorHex = Transformations.map(_exerciseType) { it?.colorHex }

    private var attemptedSave = false
    private val _savedExerciseTypeId: MutableLiveData<Long?> = MutableLiveData(null)
    private var exerciseTypesChanged = false

    val newExerciseTypeSaved = MediatorLiveData<Long?>()

    init {
        newExerciseTypeSaved.addSource(_savedExerciseTypeId) {
            if(exerciseTypesChanged) {
                newExerciseTypeSaved.value = it
            }
        }

        newExerciseTypeSaved.addSource(workoutSetCreatorViewModel.allExerciseTypes) {
            if(attemptedSave) {
                exerciseTypesChanged = true

                newExerciseTypeSaved.value = _savedExerciseTypeId.value
            }
        }
    }

    fun createOrUpdateExerciseType(context: Context, name: String, iconId: Int, colorId: Int) {
        val colorHex = "#${Integer.toHexString(colorId).substring(2)}"
        val iconName = context.resources.getResourceEntryName(iconId)

        val exerciseType = ExerciseTypeDTO(id, name, iconName, colorHex)

        // Launch in the workoutSetCreatorViewModel scope as that will persist longer and allow the application to go back to the WorkoutSetCreator wizard
        viewModelScope.launch {
            attemptedSave = true
            val newId = repository.createOrUpdateExerciseType(exerciseType)
            _savedExerciseTypeId.postValue(newId)
        }
    }

    fun savedExerciseTypeHandled() {
        _savedExerciseTypeId.value = null
    }
}