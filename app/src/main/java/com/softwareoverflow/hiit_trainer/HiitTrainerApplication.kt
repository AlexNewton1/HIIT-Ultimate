package com.softwareoverflow.hiit_trainer

import android.app.Application
import timber.log.Timber

/**
 * Application class referenced in the AndroidManifest class to set up Timber logging
 */
class HiitTrainerApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }
}