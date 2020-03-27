package com.softwareoverflow.hiit_trainer.ui.view

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.ui.dpToPx


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
        val padding = dpToPx(24)
        this.setPadding(padding, padding, padding, padding)

        background = context.resources.getDrawable(R.drawable.bg_circle, context.theme)
    }

    fun setColor(color: Int){
        background.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
    }

    fun setBackground(drawableId: Int){
        setImageResource(drawableId)
    }
}