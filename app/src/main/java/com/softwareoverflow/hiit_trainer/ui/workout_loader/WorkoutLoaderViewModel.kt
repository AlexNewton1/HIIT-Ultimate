package com.softwareoverflow.hiit_trainer.ui.workout_loader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softwareoverflow.hiit_trainer.repository.IWorkoutRepository
import com.softwareoverflow.hiit_trainer.ui.view.LoadingSpinner
import kotlinx.coroutines.launch

class WorkoutLoaderViewModel(private val repo: IWorkoutRepository) : ViewModel() {

    val workouts = repo.getAllWorkouts()

    fun deleteWorkout(id: Long){
        viewModelScope.launch {
            LoadingSpinner.showLoadingIcon()
            repo.deleteWorkoutById(id)
            LoadingSpinner.hideLoadingIcon()
        }
    }
}

