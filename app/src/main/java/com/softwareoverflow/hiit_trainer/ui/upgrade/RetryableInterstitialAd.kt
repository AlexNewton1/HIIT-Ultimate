package com.softwareoverflow.hiit_trainer.ui.upgrade

import android.content.Context
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import kotlinx.coroutines.*
import timber.log.Timber

class RetryableInterstitialAd(context: Context, adUnitId: String, private val request: AdRequest)  {

    private val maxRetryCount = 5
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

        ad.loadAd(request)
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

                withContext(Dispatchers.Main) {
                    ad.loadAd(request)
                }
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