package com.softwareoverflow.hiit_trainer.ui.view.exercise_type_picker

interface IListAdapterEventListener {
    fun onItemSelected(selected: Long?)
    fun triggerItemDeletion(id: Long)
    fun triggerItemEdit(id: Long)
}