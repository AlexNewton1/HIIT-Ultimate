package com.softwareoverflow.hiit_trainer.ui.workout_creator.exercise_type_creator

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.softwareoverflow.hiit_trainer.repository.WorkoutRepositoryFactory
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO

class ExerciseTypeViewModelFactory(
    private val context: Context,
    private val exerciseTypeDTO: ExerciseTypeDTO
) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExerciseTypeViewModel::class.java)) {
            val repo = WorkoutRepositoryFactory.getInstance(context)

            return ExerciseTypeViewModel(repo, exerciseTypeDTO.copy()) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
