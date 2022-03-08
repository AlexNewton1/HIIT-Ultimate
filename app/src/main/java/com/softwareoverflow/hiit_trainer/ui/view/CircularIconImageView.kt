package com.softwareoverflow.hiit_trainer.ui.view

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.softwareoverflow.hiit_trainer.R
import kotlin.math.min
import kotlin.math.sqrt


class CircularIconImageView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle) {

    private var color: Int = 0

    init {
        background = context.resources.getDrawable(R.drawable.bg_circle, context.theme)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // We want the image to always be fully contained within the circle
        val paddingPx = (min(w, h) /2f) * (1 - 1/sqrt(2f))
        val padding = paddingPx.toInt()

        setPadding(padding, padding, padding, padding)
    }

    fun getColor() = color

    fun setColor(color: Int?){
        if(color == null)
            return

        this.color = color
        background.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
    }

    fun setBackground(drawableId: Int?){
        if(drawableId == null)
            return

        setImageResource(drawableId)
    }
}