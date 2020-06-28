package com.softwareoverflow.hiit_trainer.ui.upgrade

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceManager
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.softwareoverflow.hiit_trainer.BuildConfig
import com.softwareoverflow.hiit_trainer.R
import kotlin.random.Random


class AdsManager(private val context: Context, private val bannerAd: AdView) {

    companion object {

        var hasUserUpgraded = false
        private set

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

        fun showAdAfterWorkout(onAdClosedCallback: () -> Unit) {
            if (!hasUserUpgraded && workoutEndInterstitial.isLoaded()) {
                workoutEndInterstitial.setOnClosedAction(onAdClosedCallback)
                workoutEndInterstitial.show() // Always show the end
            } else {
                onAdClosedCallback.invoke()
            }
        }
    }


    init {
        MobileAds.initialize(context)

        initialize()
    }

    private fun initialize() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val personalizedAds = prefs.getBoolean(context.getString(R.string.personalized_ads_enabled), false)

        val adRequest = AdRequest.Builder().apply {
            if(!personalizedAds){
                val bundle = Bundle()
                bundle.putString("npa", "1")
                addNetworkExtrasBundle(AdMobAdapter::class.java, bundle)
            }
        }.build()


        bannerAd.loadAd(adRequest)
        workoutStartInterstitial = RetryableInterstitialAd(
            context.applicationContext,
            BuildConfig.AD_INTERSTITIAL_WORKOUT_START,
            adRequest
        )
        workoutEndInterstitial = RetryableInterstitialAd(
            context.applicationContext,
            BuildConfig.AD_INTERSTITIAL_WORKOUT_END,
            adRequest
        )
    }

    fun setUserUpgraded(upgraded: Boolean) {
        hasUserUpgraded = upgraded

        if(upgraded)
            hideBanner()
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