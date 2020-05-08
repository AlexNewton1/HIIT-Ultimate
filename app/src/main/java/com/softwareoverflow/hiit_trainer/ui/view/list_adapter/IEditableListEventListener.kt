package com.softwareoverflow.hiit_trainer.ui.view.list_adapter

interface IEditableListEventListener {
    fun triggerItemDeletion(id: Long)
    fun triggerItemEdit(id: Long)
}