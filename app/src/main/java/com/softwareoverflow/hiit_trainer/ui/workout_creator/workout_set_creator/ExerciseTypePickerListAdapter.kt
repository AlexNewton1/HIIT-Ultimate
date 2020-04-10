package com.softwareoverflow.hiit_trainer.ui.workout_creator.workout_set_creator

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

class ExerciseTypePickerListAdapter :
    ListAdapter<ExerciseTypeDTO, ExerciseTypePickerListAdapter.ViewHolder>(
        DiffCallback()
    ) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.id ?: -1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position == selectedItemPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    fun notifyItemSelected(id: Long?){
        val newPosition = currentList.indexOfFirst { it.id == id }

        if(selectedItemPosition != newPosition){
            previouslySelectedPosition = selectedItemPosition
            selectedItemPosition = newPosition

            if(previouslySelectedPosition != -1)
                notifyItemChanged(previouslySelectedPosition)

            if (selectedItemPosition != -1)
                notifyItemChanged(selectedItemPosition)
        }
    }

    companion object {
        var previouslySelectedPosition: Int = -1
        var selectedItemPosition: Int = -1
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val selectedItemFrame: ConstraintLayout = itemView.findViewById(R.id.et_selected_frame)

        private val exerciseTypeName: TextView = itemView.findViewById(R.id.exerciseTypeName)
        private val exerciseTypeIcon: CircularIconImageView =
            itemView.findViewById(R.id.exerciseTypeIcon)

        fun bind(item: ExerciseTypeDTO, isSelected: Boolean) {
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