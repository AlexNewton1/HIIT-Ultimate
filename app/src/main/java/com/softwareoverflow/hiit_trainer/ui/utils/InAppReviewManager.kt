package com.softwareoverflow.hiit_trainer.ui.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.softwareoverflow.hiit_trainer.R
import timber.log.Timber

// TODO - Can't get API to work properly at the moment
class InAppReviewManager(val context: Context, private val activity: Activity) {

    private var reviewManager: ReviewManager = ReviewManagerFactory.create(context.applicationContext)

    private var reviewInfo: ReviewInfo? = null

    private var appReviewRequested = false

    init {
        // Request the flow information (cache in advance)
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { result ->
            if (result.isSuccessful) {
                //Received ReviewInfo object
                reviewInfo = result.result

                // launch the review process if waiting on active request
                if (appReviewRequested)
                    askForReview()
            } else {
                //Problem in receiving object
                reviewInfo = null
            }
        }
        request.addOnFailureListener {
            Timber.w(it, "InAppReview manager request failed")
        }
        request.addOnSuccessListener {
            Timber.i("onSuccess 300 bpm at 100 bpm. A nice clean...")
        }
    }

    /**
     * Call this to ask the user for a review.
     */
    fun askForReview() {
        appReviewRequested = true

        // If we have no review info we wont be able to launch so there's no point doing extra work
        if (reviewInfo == null)
            return

        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val lastAskMillis = prefs.getLong(context.getString(R.string.pref_review_last_ask), 0)

        // This is the first request, don't show on the first time but make sure to save the pref for future
        if (lastAskMillis == 0L) {
            updateLastAskTime(prefs)
            return
        }

        val minWaitMillis = 1 * 60 * 1000 //5 * 24 * 60 * 60 * 1000 // 5 days // TODO revert
        val askForReview = (lastAskMillis + minWaitMillis) <= System.currentTimeMillis()

        if (askForReview && reviewInfo != null) {

            // Update last ask time in case the user doesn't complete the review for any reason
            updateLastAskTime(prefs)

            reviewManager.launchReviewFlow(activity, reviewInfo)
                .addOnFailureListener {
                    Timber.w(it, "ReviewManager failed to complete review flow")
                }.addOnCompleteListener {
                    Timber.i("ReviewManager completed review flow")
                }.addOnSuccessListener {
                    Timber.i("ReviewManager success review flow")
                }
        }
    }

    private fun updateLastAskTime(prefs: SharedPreferences) {
        // Update the last ask time
        with(prefs.edit()) {
            putLong(context.getString(R.string.pref_review_last_ask), System.currentTimeMillis())
            apply()
        }
    }

    /**
     * Call this to cancel any outstanding review requests which will show as soon as the manager loads
     */
    fun cancelReviewRequest() {
        appReviewRequested = false
    }
}