package com.softwareoverflow.hiit_trainer.ui.view.list_adapter

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import androidx.core.graphics.ColorUtils
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.softwareoverflow.hiit_trainer.R

class DataBindingViewHolderBase<T>(
    val binding: ViewDataBinding
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: T, color: Int) {

        val fadeColor = ColorUtils.setAlphaComponent(color, 100)

        // Fade the background
        binding.root.findViewById<View>(R.id.colorFade).background.colorFilter =
            PorterDuffColorFilter(fadeColor, PorterDuff.Mode.SRC_IN)

        // set the binding value
        binding.setVariable(BR.dto, item)
        binding.executePendingBindings()
    }
}