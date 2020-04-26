package com.softwareoverflow.hiit_trainer.ui.view

interface IEditableOrderedListEventListener : IEditableListEventListener {
    fun triggerItemChangePosition(fromPosition: Int, toPosition: Int)
}