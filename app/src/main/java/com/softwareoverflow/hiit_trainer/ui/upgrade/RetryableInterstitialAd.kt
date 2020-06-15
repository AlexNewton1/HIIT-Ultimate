package com.softwareoverflow.hiit_trainer.ui.upgrade

import android.content.Context
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class RetryableInterstitialAd(context: Context, adUnitId: String)  {

    private val maxRetryCount = 3
    private var retryCount = 0

    private val ad = InterstitialAd(context)

    private val job = Job()

    private var onAdClosedAction: (() -> Unit)? = null

    init {
        ad.adUnitId = adUnitId
        ad.adListener = object : AdListener() {
            override fun onAdFailedToLoad(p0: Int) {
                super.onAdFailedToLoad(p0)
                retryAdLoad()
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                retryCount = 0
            }

            override fun onAdClosed() {
                super.onAdClosed()
                onAdClosedAction?.invoke()
            }
        }

        ad.loadAd(AdRequest.Builder().build())
    }

    fun setOnClosedAction(onClose: () -> Unit) {
        onAdClosedAction = onClose
    }

    private fun retryAdLoad() {
        if(retryCount < maxRetryCount){
            retryCount++

            Timber.i("Advert failed to load. Retry count: $retryCount")
            CoroutineScope(job).launch {
                delay(retryCount * 1000L)
                ad.loadAd(AdRequest.Builder().build())
            }
        } else {
            Timber.w("Retry advert load failed $maxRetryCount times. Aborting advert loading")
        }
    }

    fun isLoaded() = ad.isLoaded

    fun show(){
        ad.show()
    }


    fun destroy() {
        job.cancel()
    }
}