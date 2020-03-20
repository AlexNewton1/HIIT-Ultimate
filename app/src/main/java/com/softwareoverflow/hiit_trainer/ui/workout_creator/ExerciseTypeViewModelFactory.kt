package com.softwareoverflow.hiit_trainer.ui.workout_creator

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.softwareoverflow.hiit_trainer.repository.WorkoutRepositoryRoomDb

class ExerciseTypeViewModelFactory(
    private val context: Context,
    val id: Long?
) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExerciseTypeViewModel::class.java)) {
            val repo = WorkoutRepositoryRoomDb(context)
            return ExerciseTypeViewModel(repo, id) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}