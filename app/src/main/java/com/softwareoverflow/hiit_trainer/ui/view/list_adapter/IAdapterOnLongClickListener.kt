package com.softwareoverflow.hiit_trainer.ui.view.list_adapter

import android.view.View

interface IAdapterOnLongClickListener<T> {
    fun onLongClick(view: View, item: T, position: Int, isLongClick: Boolean)
}