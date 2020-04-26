package com.softwareoverflow.hiit_trainer.ui.view

interface IEditableListEventListener {
    fun triggerItemDeletion(id: Long)
    fun triggerItemEdit(id: Long)
}