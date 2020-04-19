package com.softwareoverflow.hiit_trainer.ui.view

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.softwareoverflow.hiit_trainer.R
import kotlin.math.min
import kotlin.math.sqrt


class CircularIconImageView : AppCompatImageView {

    constructor(context: Context) : super(context) {
        initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initialize(context)
    }

    private fun initialize(context: Context){
        background = context.resources.getDrawable(R.drawable.bg_circle, context.theme)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        //We want the image to always be fully contained within the circle
        val paddingPx = (min(w, h) /2f) * (1 - 1/sqrt(2f))
        val padding = paddingPx.toInt()

        setPadding(padding, padding, padding, padding)
    }

    fun setColor(color: Int?){
        if(color == null)
            return

        background.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
    }

    fun setBackground(drawableId: Int?){
        if(drawableId == null)
            return

        setImageResource(drawableId)
    }
}