package com.softwareoverflow.hiit_trainer.ui.view.list_adapter.exercise_type

import android.view.View
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class MultipleVisiblePagesTransformer : ViewPager2.PageTransformer {
    private val numExtraPages = 6

    private val minScaleFactor = 0f
    private val scalePerPage = (1f - minScaleFactor) / (numExtraPages / 2)

    override fun transformPage(page: View, position: Float) {
        val viewPager = page.parent.parent as ViewPager2

        if (position <= numExtraPages / 2f) {
            val margin = if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL)
                page.marginLeft + page.marginRight
            else
                page.marginTop + page.marginBottom

            val iconSize = min(page.width, page.height) - margin
            val pagerSize = max(page.width, page.height)

            val absPositionInt = abs(position.toInt())

            // Start at the center and then move outwards
            var translation = -position * pagerSize

            var delta = 0f
            for (i in 0 until absPositionInt) {
                delta += iconSize * getScaleFactor(i.toFloat())
            }
            if (position < 0f) delta *= -1
            delta += position % 1f * iconSize * getScaleFactor(absPositionInt.toFloat())

            translation += delta

            if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL)
                page.translationX = translation
            else if (viewPager.orientation == ViewPager2.ORIENTATION_VERTICAL)
                page.translationY = translation

            val scaleFactor = getScaleFactor(position)

            page.scaleX = scaleFactor
            page.scaleY = scaleFactor
            page.alpha = scaleFactor
        } else {
            page.alpha = 0f
        }
    }

    private fun getScaleFactor(position: Float): Float {
        return max(1f - (scalePerPage * (abs(position))), minScaleFactor)
    }
}