package com.softwareoverflow.hiit_trainer.ui.upgrade

import android.content.Context
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class AdsManager(context: Context, private val bannerAd: AdView) {

    private var isInitialized = false

    private var hasUserUpgraded = false

    init {
        MobileAds.initialize(context) {
            // TODO
        }

        val request = AdRequest.Builder().build()
        bannerAd.loadAd(request)
    }

    fun setUserUpgraded(upgraded: Boolean){
        hasUserUpgraded = upgraded
    }

    // TODO - add checking for if the user is upgared.
    // TODO - probably cancel the current request?
    fun hideBanner(){
        bannerAd.pause()
        bannerAd.visibility = View.GONE
    }

    fun showBanner() {
        if(!hasUserUpgraded) {
            bannerAd.resume()
            bannerAd.visibility = View.VISIBLE
        }
    }

    fun destroy() {
        bannerAd.destroy()
    }
}