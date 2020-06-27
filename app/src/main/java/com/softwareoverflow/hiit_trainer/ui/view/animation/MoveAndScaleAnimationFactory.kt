package com.softwareoverflow.hiit_trainer.ui.view.animation

import android.animation.ValueAnimator
import android.view.View
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.view.updateLayoutParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class MoveAndScaleAnimationFactory {

    private val _floatAmount = 1000F

    private var viewToScale: View? = null
    private var viewToAlpha: View? = null
    private var textViewToScale: TextView? = null

    private var animDuration = 1000L

    private var fromY: Float? = null
    private var toY: Float? = null

    private var fromHeight: Int? = null
    private var toHeight: Int? = null

    private var fromTextSize: Float? = null
    private var toTextSize: Float? = null

    private var fromAlpha: Float? = null
    private var toAlpha: Float? = null

    private val _animator: ValueAnimator by lazy {
        Timber.d("Anim: Creating ValueAnimator")
        ValueAnimator.ofFloat(_floatAmount).apply {
            duration = animDuration
            addUpdateListener { animation -> updateValues(animation.animatedFraction) }

            /* TODO FUTURE VERSION can I get property (e.g alpha) from a view by the name "alpha" or some equivalent, to simply create many animations with a from, to and view object and start them all together.
                otherwise this class is becoming slightly god-classy as it's controlling multiple views simultaneously
             */
        }
    }

    private fun updateValues(fraction: Float) {
        viewToAlpha?.alpha =
            getIntermediateValue(fromAlpha, toAlpha, fraction) ?: viewToAlpha!!.alpha

        viewToScale?.y = getIntermediateValue(fromY, toY, fraction)
            ?: viewToScale!!.y


        viewToScale?.updateLayoutParams {
            height =
                (getIntermediateValue(fromHeight?.toFloat(), toHeight?.toFloat(), fraction)?.toInt()
                    ?: height)
        }

        textViewToScale?.pivotY = 0f
        textViewToScale?.pivotX = 0f
        textViewToScale?.scaleX = getIntermediateValue(1f, 1.1f, fraction) ?: textViewToScale!!.scaleX
        textViewToScale?.scaleY = getIntermediateValue(1f, 1.1f, fraction) ?: textViewToScale!!.scaleY

        Timber.d("Anim: Set text size to ${textViewToScale?.textSize} at fraction $fraction")
    }

    private fun getIntermediateValue(from: Float?, to: Float?, fraction: Float): Float? {
        return if (from != null && to != null)
            (from + (to - from) * fraction)
        else null
    }

    fun setViewToScale(view: View) {
        this.viewToScale = view
    }

    fun setTextViewToScale(view: TextView) {
        this.textViewToScale = view
    }

    fun setDuration(duration: Long) {
        this.animDuration = duration
    }

    fun setMoveY(fromY: Float, toY: Float) {
        Timber.d("anim: fromY = $fromY, toY=$toY")
        this.fromY = fromY
        this.toY = toY
    }

    fun setScaleHeight(fromHeight: Int, toHeight: Int) {
        this.fromHeight = fromHeight
        this.toHeight = toHeight
    }

    fun setScaleText(fromTextSize: Float, toTextSize: Float) {
        this.fromTextSize = fromTextSize
        this.toTextSize = toTextSize
    }

    fun setAlphaAnimation(view: View, fromAlpha: Float, toAlpha: Float) {
        viewToAlpha = view
        this.fromAlpha = fromAlpha
        this.toAlpha = toAlpha
    }

    fun create(doOnEndAction: (() -> Unit)?): ValueAnimator = _animator.apply{
        doOnEnd {
            doOnEndAction?.invoke()

            CoroutineScope(Dispatchers.Main).launch {
                delay(250)
                viewToAlpha?.alpha = 1f

                // Delay the resetting of values to allow the custom end action to complete
                delay(200)
                updateValues(0F)
            }
        }
    }
}