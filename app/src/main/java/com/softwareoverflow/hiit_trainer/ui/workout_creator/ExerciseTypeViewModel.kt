package com.softwareoverflow.hiit_trainer.ui.workout_creator

import android.content.Context
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.softwareoverflow.hiit_trainer.repository.IWorkoutRepository
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class ExerciseTypeViewModel(val repository: IWorkoutRepository, val id: Long?) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _exerciseType = repository.getExerciseTypeById(id)

    val exerciseTypeName = Transformations.map(_exerciseType) { it?.name }
    val iconName = Transformations.map(_exerciseType) { it?.iconName }
    val colorHex = Transformations.map(_exerciseType) { it?.colorHex }

    fun createOrUpdateExerciseType(context: Context, name: String, iconId: Int, colorId: Int) {
        val colorHex = "#${Integer.toHexString(colorId).substring(2)}"
        val iconName = context.resources.getResourceEntryName(iconId)

        val exerciseType = ExerciseTypeDTO(id, name, iconName, colorHex)

        uiScope.launch {
            Timber.d("Saving exerciseType: $exerciseType")
            repository.createOrUpdateExerciseType(exerciseType)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}