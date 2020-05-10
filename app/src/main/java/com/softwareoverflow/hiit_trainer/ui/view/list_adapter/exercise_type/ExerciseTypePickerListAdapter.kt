package com.softwareoverflow.hiit_trainer.ui.view.list_adapter.exercise_type

import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.DataBindingAdapter
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.IAdapterOnLongClickListener
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.ISelectableEditableListEventListener

class ExerciseTypePickerListAdapter(eventListener: ISelectableEditableListEventListener) :
    DataBindingAdapter<ExerciseTypeDomainObject>(
        DiffCallback(),
        AdapterClickListener(eventListener)
    ) {

    var selectedItemId = -1L

    override fun getItemViewType(position: Int) = R.layout.list_item_exercise_type

    fun submitDTOs(list: List<ExerciseTypeDTO>?, submitCompleteCallback: () -> Unit) {
        val domainObjList = list?.map {
            ExerciseTypeDomainObject(it, it.id == selectedItemId)
        }
        super.submitList(domainObjList?.toMutableList(), submitCompleteCallback)
    }

    override fun getColorHexForItem(item: ExerciseTypeDomainObject) = item.dto.colorHex!!

    fun notifyItemSelected(newId: Long) {
        if (selectedItemId != newId) {
            val previouslySelectedId = selectedItemId
            selectedItemId = newId

            if (previouslySelectedId != -1L) {
                currentList.singleOrNull { it.dto.id == previouslySelectedId }?.isSelected = false
                notifyItemChanged(getPositionFromId(previouslySelectedId))
            }

            if (selectedItemId != -1L) {
                currentList.singleOrNull { it.dto.id == selectedItemId }?.isSelected = true
                notifyItemChanged(getPositionFromId(selectedItemId))
            }
        }
    }

    private fun getPositionFromId(id: Long): Int = currentList.indexOfFirst { it.dto.id == id }

    class AdapterClickListener(private val eventListener: ISelectableEditableListEventListener?) :
        IAdapterOnLongClickListener<ExerciseTypeDomainObject>,
        PopupMenu.OnMenuItemClickListener {

        private var selectedItemId: Long = -1L

        override fun onLongClick(
            view: View,
            item: ExerciseTypeDomainObject,
            position: Int,
            isLongClick: Boolean
        ) {
            selectedItemId = item.dto.id!!

            if (isLongClick) {
                PopupMenu(view.context, view).apply {
                    setOnMenuItemClickListener(this@AdapterClickListener)
                    inflate(R.menu.et_actions)
                    show()
                }
            } else {
                eventListener?.onItemSelected(selectedItemId)
            }
        }

        override fun onMenuItemClick(item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.et_menu_edit -> {
                    eventListener?.triggerItemEdit(selectedItemId)
                    true
                }
                R.id.et_menu_delete -> {
                    eventListener?.triggerItemDeletion(selectedItemId)
                    true
                }
                else -> false
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ExerciseTypeDomainObject>() {
        override fun areItemsTheSame(
            oldItem: ExerciseTypeDomainObject,
            newItem: ExerciseTypeDomainObject
        ): Boolean {
            return oldItem.dto.id == newItem.dto.id
        }

        override fun areContentsTheSame(
            oldItem: ExerciseTypeDomainObject,
            newItem: ExerciseTypeDomainObject
        ): Boolean {
            return oldItem == newItem
        }
    }
}