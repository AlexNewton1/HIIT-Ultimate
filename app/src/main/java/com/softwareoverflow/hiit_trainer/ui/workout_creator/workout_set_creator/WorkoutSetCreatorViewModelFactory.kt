package com.softwareoverflow.hiit_trainer.ui.workout_creator.workout_set_creator

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.softwareoverflow.hiit_trainer.repository.WorkoutRepositoryRoomDb

class WorkoutSetCreatorViewModelFactory(
    private val workoutSetId: Long?,
    private val context: Context
) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutSetCreatorViewModel::class.java)) {
            val repo = WorkoutRepositoryRoomDb(context)
            return WorkoutSetCreatorViewModel(workoutSetId, repo) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}