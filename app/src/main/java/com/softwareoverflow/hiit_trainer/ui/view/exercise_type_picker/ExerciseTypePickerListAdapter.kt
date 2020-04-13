package com.softwareoverflow.hiit_trainer.ui.view.exercise_type_picker

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.ui.view.CircularIconImageView
import timber.log.Timber

class ExerciseTypePickerListAdapter :
    ListAdapter<ExerciseTypeDTO, ExerciseTypePickerListAdapter.ViewHolder>(
        DiffCallback()
    ) {

    // TODO this can probably be handled elsehwere to prevent having multiple variables tracking the same value
    var selectedItemPosition: Int = -1
        private set

    private var selectedItemChangeListener :ISelectableListener? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position == selectedItemPosition)
        holder.itemView.setOnClickListener {
            Timber.d("1waybind new item selected with id ${holder.getExerciseTypeId()} and position $position")
            notifyItemSelected(holder.getExerciseTypeId())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    fun notifyItemSelected(id: Long?) {
        val newPosition = currentList.indexOfFirst { it.id == id }

        if (selectedItemPosition != newPosition) {
            val previouslySelectedPosition =
                selectedItemPosition
            selectedItemPosition = newPosition

            if (previouslySelectedPosition != -1)
                notifyItemChanged(previouslySelectedPosition)

            if (selectedItemPosition != -1){
                notifyItemChanged(selectedItemPosition)

                selectedItemChangeListener?.onItemSelected(id)
            }
        }
    }

    fun setSelectedItemChangeListener(listener: ISelectableListener) {
        Timber.d("1waybind Setting listener inside adapter")
        selectedItemChangeListener = listener
    }

    fun getSelectedItemId(): Long? {
        if(selectedItemPosition >= 0)
            return currentList[selectedItemPosition].id

        return null
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val selectedItemFrame: ConstraintLayout =
            itemView.findViewById(R.id.et_selected_frame)

        private var _exerciseTypeId: Long? = -1L

        private val exerciseTypeName: TextView = itemView.findViewById(R.id.exerciseTypeName)
        private val exerciseTypeIcon: CircularIconImageView =
            itemView.findViewById(R.id.exerciseTypeIcon)

        fun bind(item: ExerciseTypeDTO, isSelected: Boolean) {
            _exerciseTypeId = item.id

            exerciseTypeName.text = item.name

            val context = itemView.context
            val drawableId =
                context.resources.getIdentifier(item.iconName, "drawable", context.packageName)
            exerciseTypeIcon.setBackground(drawableId)

            exerciseTypeIcon.setColor(Color.parseColor(item.colorHex))

            selectedItemFrame.visibility = when {
                isSelected -> View.VISIBLE
                else -> View.GONE
            }
        }

        fun getExerciseTypeId() = _exerciseTypeId

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.list_item_exercise_type, parent, false)

                return ViewHolder(view)
            }
        }
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