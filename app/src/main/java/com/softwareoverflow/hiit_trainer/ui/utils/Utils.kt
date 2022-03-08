package com.softwareoverflow.hiit_trainer.ui.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import timber.log.Timber
import kotlin.math.abs

val Int.dpToPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun String.getDrawableId(context: Context) =
    context.resources.getIdentifier(this, "drawable", context.packageName)

fun String.getColorId() = Color.parseColor(this)

fun <T> List<T>.takeN(n: Int): List<T> {
    return when {
        n < 0 -> this.takeLast(abs(n))
        n > 0 -> this.take(n)
        else -> emptyList()
    }
}

fun hideKeyboard(activity: Activity) {
    val inputManager =
        (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
    val currentlyFocusedView = activity.currentFocus
    if (currentlyFocusedView != null)
        inputManager.hideSoftInputFromWindow(
            currentlyFocusedView.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
}

/**
 * Allows 'safe' navigation.
 * Prevents crash when navigation attempts to navigate to a destination unknown to the nav controller
 * by catching all @exception [IllegalArgumentException].
 */
fun NavController.safeNavigate(@IdRes navId: Int) {
    try {
        this.navigate(navId)
    } catch (ex: IllegalArgumentException) {
        Timber.w(ex)
    }
}

/**
 * @see [safeNavigate]
 */
fun NavController.safeNavigate(action: NavDirections){
    try {
        this.navigate(action)
    } catch (ex: IllegalArgumentException) {
        Timber.w(ex)
    }
}