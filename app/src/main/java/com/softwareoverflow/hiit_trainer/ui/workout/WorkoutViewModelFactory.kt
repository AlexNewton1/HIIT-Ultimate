package com.softwareoverflow.hiit_trainer.ui.workout

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.softwareoverflow.hiit_trainer.repository.WorkoutRepositoryFactory
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import kotlinx.coroutines.runBlocking

class WorkoutViewModelFactory(
    private val application: Application,
    private val context: Context,
    private val id: Long?,
    private val workout: WorkoutDTO?
) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutViewModel::class.java)) {
            when {
                (id != null && id != -1L) -> {
                    return runBlocking {
                        val repo = WorkoutRepositoryFactory.getInstance(context)
                        val workout = repo.getWorkoutById(id)

                        return@runBlocking WorkoutViewModel(application, workout) as T
                    }
                }
                workout != null -> {
                    return WorkoutViewModel(application, workout) as T
                }
                else -> {
                    throw IllegalArgumentException("Cannot instantiate workout fragment without either an id or a dto.")
                }
            }
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}