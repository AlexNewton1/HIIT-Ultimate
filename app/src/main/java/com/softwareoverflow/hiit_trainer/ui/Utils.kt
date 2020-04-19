package com.softwareoverflow.hiit_trainer.ui

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import kotlin.math.abs


fun dpToPx(value: Int): Int {
    return (value * Resources.getSystem().displayMetrics.density).toInt()
}

fun pxToDp(context: Context, value: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.resources.displayMetrics)
}

fun String.getDrawableId(context: Context): Int {
    return context.resources.getIdentifier(this, "drawable", context.packageName)
}

fun String.getColorId(): Int {
    return Color.parseColor(this)
}

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