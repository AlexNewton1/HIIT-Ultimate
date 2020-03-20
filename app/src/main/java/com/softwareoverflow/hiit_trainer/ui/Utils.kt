package com.softwareoverflow.hiit_trainer.ui

import android.content.Context
import android.content.res.Resources


fun dpToPx(value: Int) : Int{
    return (value * Resources.getSystem().displayMetrics.density).toInt()
}

fun String.getDrawableId(context: Context): Int {
    return context.resources.getIdentifier(this, "drawable", context.packageName)
}

fun String.getColorId(context: Context): Int {
    return context.resources.getIdentifier(this, "color", context.packageName)
}