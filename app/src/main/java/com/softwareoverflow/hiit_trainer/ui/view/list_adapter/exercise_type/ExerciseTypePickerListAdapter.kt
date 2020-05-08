package com.softwareoverflow.hiit_trainer.ui.view.list_adapter.exercise_type

import android.graphics.Color
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.ui.view.CircularIconImageView
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.ISelectableEditableListEventListener

class ExerciseTypePickerListAdapter :
    ListAdapter<ExerciseTypeDTO, ExerciseTypePickerListAdapter.ViewHolder>(
        DiffCallback()
    ) {

    // TODO this can probably be handled elsewhere to prevent having multiple variables tracking the same value
    var selectedItemId: Long = -1
        private set

    private var eventEventListener: ISelectableEditableListEventListener? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            notifyItemSelected(holder.getExerciseTypeId())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.list_item_exercise_type, parent, false)

        return ViewHolder(view)
    }

    fun notifyItemSelected(newId: Long) {
        if (selectedItemId != newId) {
            val previouslySelectedId =
                selectedItemId
            selectedItemId = newId

            if (previouslySelectedId != -1L)
                notifyItemChanged(getPositionFromId(previouslySelectedId))

            if (selectedItemId != -1L) {
                notifyItemChanged(getPositionFromId(selectedItemId))

                eventEventListener?.onItemSelected(selectedItemId)
            }
        }
    }

    fun setEventListener(listAdapterEventListener: ISelectableEditableListEventListener) {
        eventEventListener = listAdapterEventListener
    }

    private fun getPositionFromId(id: Long): Int = currentList.indexOfFirst { it.id == id }

    inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        PopupMenu.OnMenuItemClickListener {
        private val selectedItemFrame: ConstraintLayout =
            itemView.findViewById(R.id.et_selected_frame)

        private var _exerciseTypeId: Long? = -1L

        private val exerciseTypeName: TextView = itemView.findViewById(R.id.exerciseTypeName)
        private val exerciseTypeIcon: CircularIconImageView =
            itemView.findViewById(R.id.exerciseTypeIcon)


        fun bind(item: ExerciseTypeDTO) {
            _exerciseTypeId = item.id

            exerciseTypeName.text = item.name

            val context = itemView.context
            val drawableId =
                context.resources.getIdentifier(item.iconName, "drawable", context.packageName)
            exerciseTypeIcon.setBackground(drawableId)

            exerciseTypeIcon.setColor(Color.parseColor(item.colorHex))

            selectedItemFrame.visibility =
                if (_exerciseTypeId == selectedItemId) View.VISIBLE
                else View.GONE

            itemView.setOnLongClickListener {
                PopupMenu(context, it).apply {
                    setOnMenuItemClickListener(this@ViewHolder)
                    inflate(R.menu.et_actions)
                    show()
                }
                return@setOnLongClickListener true
            }
        }

        override fun onMenuItemClick(item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.et_menu_edit -> {
                    eventEventListener?.triggerItemEdit(_exerciseTypeId!!)
                    true
                }
                R.id.et_menu_delete -> {
                    eventEventListener?.triggerItemDeletion(_exerciseTypeId!!)
                    true
                }
                else -> false
            }
        }

        fun getExerciseTypeId() = _exerciseTypeId!!
    }

    class DiffCallback : DiffUtil.ItemCallback<ExerciseTypeDTO>() {
        override fun areItemsTheSame(oldItem: ExerciseTypeDTO, newItem: ExerciseTypeDTO): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ExerciseTypeDTO,
            newItem: ExerciseTypeDTO
        ): Boolean {
            return oldItem == newItem
        }
    }
}