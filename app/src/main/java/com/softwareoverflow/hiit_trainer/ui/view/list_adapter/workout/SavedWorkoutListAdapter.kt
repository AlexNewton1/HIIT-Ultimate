package com.softwareoverflow.hiit_trainer.ui.view.list_adapter.workout

import android.content.Context
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.DataBindingAdapter
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.IAdapterOnLongClickListener
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.ISelectableEditableListEventListener
import timber.log.Timber

class SavedWorkoutListAdapter(private val context: Context) :
    DataBindingAdapter<WorkoutDTO>(
        DiffCallback(),
        AdapterLongClickListener()
    ) {

    override fun getItemViewType(position: Int): Int {
        Timber.d("LoadWorkout: SavedWorkoutListAdapter getItemViewType called")
        return R.layout.list_item_workout
    }

    override fun getColorHexForItem(item: WorkoutDTO): String =
        "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.colorPrimary))

    fun setEventListener(listener: ISelectableEditableListEventListener) {
        AdapterLongClickListener.setEventListener(
            listener
        )
    }

    class AdapterLongClickListener : IAdapterOnLongClickListener<WorkoutDTO>, PopupMenu.OnMenuItemClickListener {
        companion object {
            private var eventListener: ISelectableEditableListEventListener? = null

            fun setEventListener(listener: ISelectableEditableListEventListener){
                eventListener = listener
            }
        }

        private lateinit var clickedItem: WorkoutDTO

        override fun onLongClick(view: View,  item: WorkoutDTO, position: Int) {
            clickedItem = item

            PopupMenu(view.context, view).apply {
                setOnMenuItemClickListener(this@AdapterLongClickListener)
                inflate(R.menu.workout_actions)
                show()
            }
        }

        override fun onMenuItemClick(item: MenuItem): Boolean {
            val id = clickedItem.id
            return when (item.itemId) {
                R.id.workout_menu_start -> {
                    eventListener?.onItemSelected(id)
                    true
                }
                R.id.workout_menu_edit -> {
                    eventListener?.triggerItemEdit(id!!)
                    true
                }
                R.id.workout_menu_delete -> {
                    eventListener?.triggerItemDeletion(id!!)
                    true
                }
                else -> false
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<WorkoutDTO>() {
        override fun areItemsTheSame(oldItem: WorkoutDTO, newItem: WorkoutDTO): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WorkoutDTO, newItem: WorkoutDTO): Boolean {
            return oldItem == newItem
        }
    }
}