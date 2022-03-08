package com.softwareoverflow.hiit_trainer.ui.view.list_adapter

import androidx.recyclerview.widget.DiffUtil

open class DiffCallbackBase<T> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}