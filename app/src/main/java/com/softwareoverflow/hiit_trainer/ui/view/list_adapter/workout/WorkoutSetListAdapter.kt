package com.softwareoverflow.hiit_trainer.ui.view.list_adapter.workout

import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.DataBindingAdapter
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.DiffCallbackBase
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.IAdapterOnLongClickListener
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.IEditableOrderedListEventListener

class WorkoutSetListAdapter :
    DataBindingAdapter<WorkoutSetDTO>(
        DiffCallbackBase(),
        AdapterClickListener()
    ) {

    override fun submitList(list: MutableList<WorkoutSetDTO>?) {
        super.submitList(list?.sortedBy { it.orderInWorkout }?.toMutableList())
    }

    override fun submitList(list: MutableList<WorkoutSetDTO>?, commitCallback: Runnable?) {
        super.submitList(list?.sortedBy { it.orderInWorkout }, commitCallback)
    }

    fun setEventListener(listener: IEditableOrderedListEventListener) = AdapterClickListener.setEventListener(listener)

    override fun getItemViewType(position: Int) = R.layout.list_item_workout_set

    override fun getColorHexForItem(item: WorkoutSetDTO) =  item.exerciseTypeDTO!!.colorHex!!

    class AdapterClickListener : IAdapterOnLongClickListener<WorkoutSetDTO>,
        PopupMenu.OnMenuItemClickListener {

        companion object {
            private var eventListener: IEditableOrderedListEventListener? = null

            fun setEventListener(listener: IEditableOrderedListEventListener) {
                eventListener = listener
            }
        }

        private var clickedPosition: Int = -1

        override fun onLongClick(view: View, item: WorkoutSetDTO, position: Int, isLongClick: Boolean) {
            if(!isLongClick) return

            clickedPosition = position

            /* TODO possible future version, find a way of enabling / disabling the move up / move down menu items based on position.
                This was attempted by changing the interface, although the viewHolders aren't recreated so passing static values to them
                at creation is not feasible. Will need to think up a solution to this. For now, this all options are allowed and the logic
                is in the view model to prevent any indexing errors.
            */
            PopupMenu(view.context, view).apply {
                setOnMenuItemClickListener(this@AdapterClickListener)
                inflate(R.menu.workout_set_actions)
                show()
            }
        }

        override fun onMenuItemClick(item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.workout_set_menu_move_down -> {
                    eventListener?.triggerItemChangePosition(clickedPosition, clickedPosition + 1)
                    true
                }
                R.id.workout_set_menu_move_up -> {
                    eventListener?.triggerItemChangePosition(clickedPosition, clickedPosition - 1)
                    true
                }
                R.id.workout_set_menu_edit -> {
                    eventListener?.triggerItemEdit(clickedPosition.toLong())
                    true
                }
                R.id.workout_set_menu_delete -> {
                    eventListener?.triggerItemDeletion(clickedPosition.toLong())
                    true
                }
                else -> false
            }
        }
    }
}