package com.softwareoverflow.hiit_trainer.ui.consent

import android.content.Context
import androidx.preference.PreferenceManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.softwareoverflow.hiit_trainer.ui.utils.SharedPreferencesManager

class UserConsentManager {

    companion object {
        var analyticsEnabled = false
            private set

        fun userGaveConsent(context: Context) {
            setAnalytics(context, true)
        }


        fun setAnalytics(context: Context, boolean: Boolean) {
            analyticsEnabled = boolean
            FirebaseAnalytics.getInstance(context).setAnalyticsCollectionEnabled(boolean)

            PreferenceManager.getDefaultSharedPreferences(context.applicationContext).edit()
                .putBoolean(SharedPreferencesManager.analyticsEnabled, boolean)
                .apply()
        }

    }
}