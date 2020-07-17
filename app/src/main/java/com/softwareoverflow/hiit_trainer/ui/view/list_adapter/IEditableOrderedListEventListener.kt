package com.softwareoverflow.hiit_trainer.ui.view.list_adapter

interface IEditableOrderedListEventListener :
    IEditableListEventListener {
    fun triggerItemChangePosition(fromPosition: Int, toPosition: Int)
}