package com.softwareoverflow.hiit_trainer.ui.view

interface ISelectableEditableListEventListener : IEditableListEventListener {
    fun onItemSelected(selected: Long?)
}