package com.softwareoverflow.hiit_trainer.ui.workout

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.ui.getWorkoutCompleteExerciseType
import com.softwareoverflow.hiit_trainer.ui.workout.media.WorkoutMediaManager

class WorkoutViewModelFactory(
    private val context: Context,
    private val workout: WorkoutDTO
) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutViewModel::class.java)) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
            val mediaManager = WorkoutMediaManager(context, prefs)

            return WorkoutViewModel(
                context.applicationContext,
                workout,
                getWorkoutCompleteExerciseType(context.applicationContext),
                mediaManager
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
