package com.softwareoverflow.hiit_trainer.ui.view.list_adapter.workout

import android.content.Context
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.ui.view.IconPopupMenuBuilder
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.*

class SavedWorkoutListAdapter(private val context: Context) :
    DataBindingAdapter<WorkoutLoaderDomainObject>(
        DiffCallbackBase(),
        AdapterClickListener()
    ) {

    override fun getItemViewType(position: Int): Int {
        return if (currentList[position].type == WorkoutLoaderDomainObjectType.USER)
            R.layout.list_item_workout
        else
            R.layout.list_item_workout_placeholder
    }

    override fun getColorHexForItem(item: WorkoutLoaderDomainObject): String {
        return if (item.type == WorkoutLoaderDomainObjectType.USER)
            "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.colorPrimary))
        else
            "#000000" // Black
    }

    fun setEventListener(listener: IEditableListEventListener) {
        AdapterClickListener.setEventListener(listener)
    }

    override fun onBindViewHolder(
        holder: DataBindingViewHolderBase<WorkoutLoaderDomainObject>,
        position: Int
    ) {
        super.onBindViewHolder(holder, position)

        holder.itemView.findViewById<View>(R.id.startWorkoutButton)?.setOnClickListener {
            clickListener?.onClick(it, getItem(position), position, false)
        }
    }

    class AdapterClickListener : IAdapterOnClickListener<WorkoutLoaderDomainObject>,
        PopupMenu.OnMenuItemClickListener {
        companion object {
            private var eventListener: IEditableListEventListener? = null

            fun setEventListener(listener: IEditableListEventListener) {
                eventListener = listener
            }
        }

        private lateinit var clickedItem: WorkoutDTO

        override fun onClick(
            view: View,
            item: WorkoutLoaderDomainObject,
            position: Int,
            isLongClick: Boolean
        ) {
            if (item.type == WorkoutLoaderDomainObjectType.USER) {
                clickedItem = item.dto
                when {
                    isLongClick -> {
                        IconPopupMenuBuilder(view.context, view).apply {
                            setOnMenuItemClickListener(this@AdapterClickListener)
                            setMenuResource(R.menu.workout_actions)
                        }.build().show()
                    }
                    view.id == R.id.startWorkoutButton -> {
                        eventListener?.onItemSelected(item.dto.id)
                    }
                    else -> {
                        eventListener?.triggerItemEdit(clickedItem.id!!)
                    }
                }
            } else {
                eventListener?.onItemSelected(item.dto.id)
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
}