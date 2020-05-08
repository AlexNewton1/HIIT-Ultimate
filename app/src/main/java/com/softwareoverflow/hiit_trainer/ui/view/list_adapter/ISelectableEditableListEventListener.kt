package com.softwareoverflow.hiit_trainer.ui.view.list_adapter

import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.IEditableListEventListener

interface ISelectableEditableListEventListener :
    IEditableListEventListener {
    fun onItemSelected(selected: Long?)
}