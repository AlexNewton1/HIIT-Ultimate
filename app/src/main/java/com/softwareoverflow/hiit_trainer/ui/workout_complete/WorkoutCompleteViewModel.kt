package com.softwareoverflow.hiit_trainer.ui.workout_complete

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.repository.IWorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class WorkoutCompleteViewModel @Inject constructor(@ApplicationContext context: Context, private val repo: IWorkoutRepository) : ViewModel() {

    private var advertShown = false

    val showUnsavedChangesWarning = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        .getBoolean(context.applicationContext.getString(R.string.pref_unsaved_changes_warning), true)

    fun shouldShowAdvert() : Boolean = !advertShown

    fun setAdvertShown() {
        advertShown = true
    }
}