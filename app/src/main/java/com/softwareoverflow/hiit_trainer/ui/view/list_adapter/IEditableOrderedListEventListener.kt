package com.softwareoverflow.hiit_trainer.ui.view.list_adapter

import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.IEditableListEventListener

interface IEditableOrderedListEventListener :
    IEditableListEventListener {
    fun triggerItemChangePosition(fromPosition: Int, toPosition: Int)
}