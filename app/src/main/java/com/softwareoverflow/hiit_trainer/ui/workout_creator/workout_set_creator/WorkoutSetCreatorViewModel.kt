package com.softwareoverflow.hiit_trainer.ui.workout_creator.workout_set_creator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softwareoverflow.hiit_trainer.repository.IWorkoutRepository
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO

class WorkoutSetCreatorViewModel(
    workoutSetId: Long?,
    private val repo: IWorkoutRepository
) :
    ViewModel() {

    val workoutSet = MutableLiveData<WorkoutSetDTO?>(null)
        get() {
            if (field.value?.exerciseTypeDTO?.id != selectedExerciseTypeId.value)
                field.value?.exerciseTypeDTO =
                    allExerciseTypes.value?.first { it.id == selectedExerciseTypeId.value }

            return field
        }

    val allExerciseTypes = repo.getAllExerciseTypes()
    var selectedExerciseTypeId = MutableLiveData<Long?>(null)


    init {
        val savedWorkoutSet = repo.getWorkoutSetById(workoutSetId)
        selectedExerciseTypeId.postValue(savedWorkoutSet.value?.exerciseTypeDTO?.id)
        workoutSet.postValue(savedWorkoutSet.value)
    }

    // TODO When the user accepts the values, need to pass the DTO back to the FragmentWorkoutCreatorHome, or update the value in the workout creator view model. TBD!

}