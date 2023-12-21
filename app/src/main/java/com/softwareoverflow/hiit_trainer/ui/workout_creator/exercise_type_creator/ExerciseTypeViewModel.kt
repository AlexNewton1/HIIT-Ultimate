package com.softwareoverflow.hiit_trainer.ui.workout_creator.exercise_type_creator

import android.content.Context
import androidx.lifecycle.ViewModel
import com.softwareoverflow.hiit_trainer.repository.IWorkoutRepository
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import kotlinx.coroutines.runBlocking

class ExerciseTypeViewModel(
    private val repository: IWorkoutRepository,
    private val exerciseType: ExerciseTypeDTO
) : ViewModel() {

    fun createOrUpdateExerciseType(context: Context, name: String, iconId: Int, colorId: Int) : Long {
        val colorHex = "#${Integer.toHexString(colorId).substring(2)}"
        val iconName = context.resources.getResourceEntryName(iconId)

        val exerciseType = ExerciseTypeDTO(exerciseType.id, name, iconName, colorHex)

        return runBlocking {
            return@runBlocking repository.createOrUpdateExerciseType(exerciseType)
        }
    }
}