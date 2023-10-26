package com.softwareoverflow.hiit_trainer.ui.workout_creator

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import com.softwareoverflow.hiit_trainer.R

class UnsavedChangesViewModel : ViewModel() {

    fun setDisableWarningState(disableWarning: Boolean, context: Context){
        PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
            .edit()
            .putBoolean(context.getString(R.string.pref_unsaved_changes_warning), !disableWarning)
            .apply()
    }
}
