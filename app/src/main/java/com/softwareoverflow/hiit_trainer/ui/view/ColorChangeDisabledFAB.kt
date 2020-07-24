package com.softwareoverflow.hiit_trainer.ui.view

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.softwareoverflow.hiit_trainer.R

/**
 * Floating Action Button which changes background color to grey when disabled, and colorAccent when enabled.
 * NOTE: This does not call the super setEnabled method and so the button WILL remain active to clicks, to allow for any error message handling.
 */
class ColorChangeDisabledFAB @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FloatingActionButton(context, attrs, defStyle) {

    override fun setEnabled(enabled: Boolean) {
        val bgColor =
            if (enabled) context.getColor(R.color.colorAccent)
            else context.getColor(R.color.grey)

        backgroundTintList = ColorStateList.valueOf(bgColor)
    }
}