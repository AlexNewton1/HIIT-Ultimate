package com.softwareoverflow.hiit_trainer.ui.consent

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.UserProperty.ALLOW_AD_PERSONALIZATION_SIGNALS
import com.softwareoverflow.hiit_trainer.ui.utils.SharedPreferencesManager

class UserConsentManager(context: Context) {

    companion object {
        private lateinit var prefs: SharedPreferences

        var consentEverGiven = false
            private set

        var personalizedAdsEnabled = false
            private set

        var analyticsEnabled = false
            private set

        fun userGaveConsent(context: Context) {
            consentEverGiven = true
            prefs.edit().putBoolean(SharedPreferencesManager.consentGiven, true)
                .apply()

            setPersonalizedAds(context, true)
            setAnalytics(context, true)
        }

        fun setPersonalizedAds(context: Context, boolean: Boolean) {
            personalizedAdsEnabled = boolean
            FirebaseAnalytics.getInstance(context)
                .setUserProperty(ALLOW_AD_PERSONALIZATION_SIGNALS, boolean.toString())

            prefs.edit()
                .putBoolean(SharedPreferencesManager.personalAds, boolean)
                .apply()
        }

        fun setAnalytics(context: Context, boolean: Boolean) {
            analyticsEnabled = boolean
            FirebaseAnalytics.getInstance(context).setAnalyticsCollectionEnabled(boolean)

            prefs.edit().putBoolean(SharedPreferencesManager.analyticsEnabled, boolean)
                .apply()
        }

    }

    init {
        prefs = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

        consentEverGiven =
            prefs.getBoolean(SharedPreferencesManager.consentGiven, false)
        analyticsEnabled =
            prefs.getBoolean(SharedPreferencesManager.analyticsEnabled, false)
        personalizedAdsEnabled =
            prefs.getBoolean(SharedPreferencesManager.personalAds, false)
    }
}