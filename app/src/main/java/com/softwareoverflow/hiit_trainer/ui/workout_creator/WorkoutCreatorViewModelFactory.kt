package com.softwareoverflow.hiit_trainer.ui.workout_creator

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.repository.WorkoutRepositoryFactory
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO

class WorkoutCreatorViewModelFactory(val context: Context, val dto: WorkoutDTO) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(WorkoutCreatorViewModel::class.java)) {

            val repo = WorkoutRepositoryFactory.getInstance(context)

            val showSaveWarning = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
                .getBoolean(context.applicationContext.getString(R.string.pref_unsaved_changes_warning), true)


            return WorkoutCreatorViewModel(repo, dto, showSaveWarning) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
