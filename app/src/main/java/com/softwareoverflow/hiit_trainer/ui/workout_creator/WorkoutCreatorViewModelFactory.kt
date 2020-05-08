package com.softwareoverflow.hiit_trainer.ui.workout_creator

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.softwareoverflow.hiit_trainer.repository.WorkoutRepositoryFactory

class WorkoutCreatorViewModelFactory(val context: Context, val id: Long) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(WorkoutCreatorViewModel::class.java)) {

            val repo = WorkoutRepositoryFactory.getInstance(context)
            return WorkoutCreatorViewModel(repo, id) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}