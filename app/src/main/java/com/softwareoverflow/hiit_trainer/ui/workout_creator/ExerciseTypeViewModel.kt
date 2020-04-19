package com.softwareoverflow.hiit_trainer.ui.workout_creator

import android.content.Context
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softwareoverflow.hiit_trainer.repository.IWorkoutRepository
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.ui.view.LoadingSpinner
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

    fun createOrUpdateExerciseType(context: Context, name: String, iconId: Int, colorId: Int) {
        val colorHex = "#${Integer.toHexString(colorId).substring(2)}"
        val iconName = context.resources.getResourceEntryName(iconId)

        val exerciseType = ExerciseTypeDTO(id, name, iconName, colorHex)

        // Launch in the workoutSetCreatorViewModel scope as that will persist longer and allow the application to go back to the WorkoutSetCreator wizard
        workoutSetCreatorViewModel.viewModelScope.launch {
            LoadingSpinner.showLoadingIcon()
            val newId = repository.createOrUpdateExerciseType(exerciseType)
            workoutSetCreatorViewModel.selectedExerciseTypeId.postValue(newId)
            LoadingSpinner.hideLoadingIcon()
        }
    }
}