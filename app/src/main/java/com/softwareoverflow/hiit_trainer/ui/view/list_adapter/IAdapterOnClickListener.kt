package com.softwareoverflow.hiit_trainer.ui.view.list_adapter

import android.view.View

interface IAdapterOnClickListener<T> {
    fun onClick(view: View, item: T, position: Int, isLongClick: Boolean)
}