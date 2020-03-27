package com.softwareoverflow.hiit_trainer.ui.workout_creator

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.ui.view.CircularIconImageView

class ExerciseTypeListAdapter :
    ListAdapter<ExerciseTypeDTO, ExerciseTypeListAdapter.ViewHolder>(DiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val exerciseTypeName: TextView = itemView.findViewById(R.id.exerciseTypeName)
        private val exerciseTypeIcon: CircularIconImageView =
            itemView.findViewById(R.id.exerciseTypeIcon)

        fun bind(item: ExerciseTypeDTO) {
            exerciseTypeName.text = item.name

            val context = itemView.context
            val drawableId =
                context.resources.getIdentifier(item.iconName, "drawable", context.packageName)
            exerciseTypeIcon.setBackground(drawableId)

            exerciseTypeIcon.setColor(Color.parseColor(item.colorHex))
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