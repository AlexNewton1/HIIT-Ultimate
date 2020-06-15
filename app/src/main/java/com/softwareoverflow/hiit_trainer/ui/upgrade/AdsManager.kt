package com.softwareoverflow.hiit_trainer.ui.upgrade

import android.content.Context
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.softwareoverflow.hiit_trainer.BuildConfig
import kotlin.random.Random

class AdsManager(context: Context, private val bannerAd: AdView) {

    companion object {
        private var hasUserUpgraded = false

        var isFirstRun = false

        private lateinit var workoutStartInterstitial: RetryableInterstitialAd
        private lateinit var workoutEndInterstitial: RetryableInterstitialAd

        fun showAdBeforeWorkout(onAdClosedCallback: () -> Unit) {
            if (!isFirstRun && !hasUserUpgraded && workoutStartInterstitial.isLoaded() && Random.nextBoolean()) { // Only show the ad 50% of the time before a workout (on average)
                workoutStartInterstitial.setOnClosedAction(onAdClosedCallback)
                workoutStartInterstitial.show()
            } else {
                onAdClosedCallback.invoke()
            }
        }
    }


    init {
        MobileAds.initialize(context)

        bannerAd.loadAd(AdRequest.Builder().build())
        workoutStartInterstitial = RetryableInterstitialAd(
            context.applicationContext,
            BuildConfig.AD_INTERSTITIAL_WORKOUT_START
        )
        workoutEndInterstitial = RetryableInterstitialAd(
            context.applicationContext,
            BuildConfig.AD_INTERSTITIAL_WORKOUT_END
        )
    }

    fun showAdAfterWorkout() {
        if (!hasUserUpgraded && workoutEndInterstitial.isLoaded())
            workoutEndInterstitial.show() // Always show the end
    }

    fun setUserUpgraded(upgraded: Boolean) {
        hasUserUpgraded = upgraded
    }

    fun hideBanner() {
        bannerAd.pause()
        bannerAd.visibility = View.GONE
    }

    fun showBanner() {
        if (!hasUserUpgraded) {
            bannerAd.resume()
            bannerAd.visibility = View.VISIBLE
        }
    }

    fun destroy() {
        bannerAd.destroy()
        workoutEndInterstitial.destroy()
        workoutStartInterstitial.destroy()
    }
}