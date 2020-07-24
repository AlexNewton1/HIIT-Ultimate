package com.softwareoverflow.hiit_trainer.ui.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.view.inputmethod.InputMethodManager
import kotlin.math.abs

val Int.pxToDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

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