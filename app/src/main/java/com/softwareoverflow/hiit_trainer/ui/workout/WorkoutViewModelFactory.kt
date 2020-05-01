package com.softwareoverflow.hiit_trainer.ui.workout

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.softwareoverflow.hiit_trainer.repository.WorkoutRepositoryRoomDb

class WorkoutViewModelFactory(
    private val context: Context,
    private val id: Long
) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutViewModel::class.java)) {
            val repo = WorkoutRepositoryRoomDb(context)
            return WorkoutViewModel(id, repo) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}