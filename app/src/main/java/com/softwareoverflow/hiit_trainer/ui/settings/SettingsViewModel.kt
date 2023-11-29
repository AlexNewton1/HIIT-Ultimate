package com.softwareoverflow.hiit_trainer.ui.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import com.softwareoverflow.hiit_trainer.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(@ApplicationContext context: Context) : ViewModel() {
    private val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)

    private val _prepSetEnabled = MutableStateFlow(
        sharedPrefs.getBoolean(
            context.getString(R.string.key_preparation_set_enabled), true
        )
    )
    val prepSetEnabled: StateFlow<Boolean> get() = _prepSetEnabled


    private val _prepSetDuration= MutableStateFlow(sharedPrefs.getString(
        context.getString(R.string.key_preparation_set_time), "5"
    )?.toInt() ?: 5)
    val prepSetDuration: StateFlow<Int> get() = _prepSetDuration


    private val _finalSeconds = MutableStateFlow(
        sharedPrefs.getStringSet(
            context.getString(R.string.key_final_seconds_vocal),
            setOf("5", "10", "15")
        )!!.toMutableSet()
    )
    val finalSeconds: StateFlow<Set<String>> get() = _finalSeconds

    private val _personalAds = MutableStateFlow(
        sharedPrefs.getBoolean(
            context.getString(R.string.key_personalized_ads_enabled),
            true
        )
    )
    val personalAds: StateFlow<Boolean> get() = _personalAds


    private val _analytics = MutableStateFlow(
        sharedPrefs.getBoolean(
            context.getString(R.string.key_analytics_enabled),
            true
        )
    )
    val analytics: StateFlow<Boolean> get() = _analytics


    fun onPrepSetEnabledChange(enabled: Boolean) {
        _prepSetEnabled.value = enabled
    }

    fun onPrepDurationChange(value: Int) {
        _prepSetDuration.value = value
    }

    fun onFinalSecondsChange(value: Set<String>) {
        _finalSeconds.value = value.toMutableSet()
    }

    fun onPersonalAdsChange(enabled: Boolean) {
        _personalAds.value = enabled
    }

    fun onAnalyticsChange(enabled: Boolean) {
        _analytics.value = enabled
    }


    fun saveSettings(c: Context, onUpdateComplete: () -> Unit) {
        val context = c.applicationContext

        sharedPrefs.edit()
            .putBoolean(
                context.getString(R.string.key_preparation_set_enabled),
                prepSetEnabled.value
            )
            .putString(context.getString(R.string.key_preparation_set_time), prepSetDuration.value.toString())
            .putStringSet(context.getString(R.string.key_final_seconds_vocal), finalSeconds.value)
            .putBoolean(context.getString(R.string.key_personalized_ads_enabled), personalAds.value)
            .putBoolean(context.getString(R.string.key_analytics_enabled), analytics.value)
            .apply()

        onUpdateComplete()
    }
}