package com.softwareoverflow.hiit_trainer.ui.view.list_adapter

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView

class DataBindingViewHolderBase<T>(
    val binding: ViewDataBinding
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: T, fadeColor: Int) {
        // Fade the background
        binding.root.background.colorFilter =
            PorterDuffColorFilter(fadeColor, PorterDuff.Mode.SRC_IN)
        binding.root.background.alpha = 75

        // set the binding value
        binding.setVariable(BR.dto, item)
        binding.executePendingBindings()
    }
}