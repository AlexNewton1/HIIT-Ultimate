package com.softwareoverflow.hiit_trainer.ui

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import kotlin.math.abs


fun dpToPx(value: Int) : Int{
    return (value * Resources.getSystem().displayMetrics.density).toInt()
}

fun String.getDrawableId(context: Context): Int {
    return context.resources.getIdentifier(this, "drawable", context.packageName)
}

fun String.getColorId(): Int {
    return Color.parseColor(this)
}

fun <T> List<T>.takeN(n: Int) : List<T>{
    return when {
        n < 0 -> this.takeLast(abs(n))
        n > 0 -> this.take(n)
        else -> emptyList()
    }
}