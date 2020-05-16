package com.softwareoverflow.hiit_trainer.ui.workout_saver

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.softwareoverflow.hiit_trainer.repository.WorkoutRepositoryFactory
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO

class WorkoutSaverViewModelFactory(private val context: Context, private val workout: WorkoutDTO, private val saveAsNew: Boolean) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(WorkoutSaverViewModel::class.java)) {
            val repo = WorkoutRepositoryFactory.getInstance(context)

            if(saveAsNew)
                workout.id = null

            return WorkoutSaverViewModel(repo, workout) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}