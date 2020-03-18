package com.softwareoverflow.hiit_trainer.ui

import android.content.res.Resources


public fun dpToPx(value: Int) : Int{
    return (value * Resources.getSystem().displayMetrics.density).toInt()
}