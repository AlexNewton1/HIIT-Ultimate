package com.softwareoverflow.hiit_trainer.ui.workout_creator.workout_set_creator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softwareoverflow.hiit_trainer.repository.IWorkoutRepository
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO
import com.softwareoverflow.hiit_trainer.ui.LoadingSpinner
import kotlinx.coroutines.launch

class WorkoutSetCreatorViewModel(
    workoutSetToEdit: WorkoutSetDTO,
    private val repo: IWorkoutRepository
) :
    ViewModel() {

    val workoutSet = MutableLiveData(workoutSetToEdit)

    val allExerciseTypes = repo.getAllExerciseTypes()

    var selectedExerciseTypeId = MutableLiveData<Long?>(workoutSetToEdit.exerciseTypeDTO?.id)

    fun setChosenExerciseTypeId(id: Long) {
        val exerciseType = allExerciseTypes.value!!.first { it.id == id }
        workoutSet.value!!.exerciseTypeDTO = exerciseType
    }

    fun deleteExerciseTypeById(id: Long){
        viewModelScope.launch {
            LoadingSpinner.showLoadingIcon()

            val exerciseType = allExerciseTypes.value!!.first { it.id == id }
            repo.deleteExerciseType(exerciseType)
            LoadingSpinner.hideLoadingIcon()
        }
    }
}