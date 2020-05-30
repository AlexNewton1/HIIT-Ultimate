package com.softwareoverflow.hiit_trainer.ui.view.list_adapter.workout

import android.content.Context
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.DataBindingAdapter
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.DataBindingViewHolderBase
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.IAdapterOnClickListener
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.ISelectableEditableListEventListener

class OverwriteWorkoutListAdapter(
    private val context: Context,
    eventListener: ISelectableEditableListEventListener
) :
    DataBindingAdapter<WorkoutOverwriteDomainObject>(
        DiffCallback(),
        AdapterClickListener(eventListener)
    ) {

    private var currentlySelectedId: Long? = null
    lateinit var updatedName: String; private set

    override fun getColorHexForItem(item: WorkoutOverwriteDomainObject) =
        "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.colorPrimary))

    override fun getItemViewType(position: Int): Int {
        return R.layout.list_item_overwrite_workout
    }

    override fun onBindViewHolder(
        holder: DataBindingViewHolderBase<WorkoutOverwriteDomainObject>,
        position: Int
    ) {
        super.onBindViewHolder(holder, position)

        val item = getItem(position)

        val editText = holder.itemView.findViewById<EditText>(R.id.workoutNameET)
        val checkBox = holder.itemView.findViewById<CheckBox>(R.id.savedWorkoutSelect)

        editText.doAfterTextChanged { text ->
            if(item.dto.id == currentlySelectedId)
                updatedName = text.toString()
        }

        holder.itemView.findViewById<ConstraintLayout>(R.id.selectOnClick).setOnClickListener {
            editText.requestFocus()
            checkBox.findViewById<CheckBox>(R.id.savedWorkoutSelect).isChecked = true

            clickListener?.onClick(it, item, position, false)
        }

        checkBox.setOnClickListener {
            editText.requestFocus()
            checkBox.isChecked = true

            clickListener?.onClick(it, item, position, false)
        }

        editText.setOnClickListener {
            if (it.hasFocus() && item.dto.id != currentlySelectedId) {
                editText.requestFocus()
                checkBox.isChecked = true

                clickListener?.onClick(it, item, position, false)

                updatedName = item.dto.name
            }
        }

        editText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus && item.dto.id != currentlySelectedId) {
                checkBox.isChecked = true

                clickListener?.onClick(v, item, position, false)

                currentlySelectedId = item.dto.id
            } else if (!hasFocus) {
                editText.setText(item.dto.name) // Reset the name field
            }
        }
    }

    private fun getPositionForId(id: Long?) = currentList.indexOfFirst { it.dto.id == id }

    fun notifyItemSelected(selected: Long?) {
        notifyForId(currentlySelectedId) // Perform any updates for the old selection

        currentlySelectedId = selected // Update the selection

        notifyForId(currentlySelectedId) // Perform any updates for the new selection

        currentlySelectedId?.let {
            if(it > 0L && getPositionForId(currentlySelectedId) >= 0) {
                updatedName = getItem(getPositionForId(currentlySelectedId)).dto.name

            }
        }
    }

    private fun notifyForId(id: Long?) {
        val oldPosition = getPositionForId(id)
        if (oldPosition != -1)
            try {notifyItemChanged(oldPosition)} catch (ex: IllegalStateException) {} // Swallow ex
    }

    class AdapterClickListener(private val eventListener: ISelectableEditableListEventListener?) :
        IAdapterOnClickListener<WorkoutOverwriteDomainObject> {

        override fun onClick(
            view: View,
            item: WorkoutOverwriteDomainObject,
            position: Int,
            isLongClick: Boolean
        ) {
            if (!isLongClick)
                eventListener?.onItemSelected(item.dto.id)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<WorkoutOverwriteDomainObject>() {
        override fun areItemsTheSame(
            oldItem: WorkoutOverwriteDomainObject,
            newItem: WorkoutOverwriteDomainObject
        ): Boolean {
            return oldItem.dto.id == newItem.dto.id
        }

        override fun areContentsTheSame(
            oldItem: WorkoutOverwriteDomainObject,
            newItem: WorkoutOverwriteDomainObject
        ): Boolean {
            return oldItem == newItem
        }
    }
}

