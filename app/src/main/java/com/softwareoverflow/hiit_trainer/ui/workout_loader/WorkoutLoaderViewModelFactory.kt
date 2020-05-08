package com.softwareoverflow.hiit_trainer.ui.workout_loader

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.softwareoverflow.hiit_trainer.repository.WorkoutRepositoryFactory

class WorkoutLoaderViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    // TODO looks like theres getting a lot of repition in these methods. Could possibly try and have a few preset factories which pass (repo), (repo, id)... and then only have special factories
    // for special cases?

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutLoaderViewModel::class.java)) {
            val repo = WorkoutRepositoryFactory.getInstance(context)
            return WorkoutLoaderViewModel(repo) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}