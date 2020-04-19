package com.softwareoverflow.hiit_trainer.ui.view.workout_set

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO
import com.softwareoverflow.hiit_trainer.ui.getColorId
import com.softwareoverflow.hiit_trainer.ui.getDrawableId
import com.softwareoverflow.hiit_trainer.ui.view.CircularIconImageView
import timber.log.Timber

class WorkoutSetListAdapter :
    ListAdapter<WorkoutSetDTO, WorkoutSetListAdapter.ViewHolder>(
        DiffCallback()
    ) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.list_item_workout_set, parent, false)

        return ViewHolder(view)
    }

    inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val exerciseTypeIcon: CircularIconImageView =
            itemView.findViewById(R.id.exerciseTypeIcon)
        private val exerciseTypeName: TextView = itemView.findViewById(R.id.exerciseTypeName)
        private val workTime: TextView = itemView.findViewById(R.id.workTime)
        private val restTime: TextView = itemView.findViewById(R.id.restTime)
        private val numReps: TextView = itemView.findViewById(R.id.numReps)
        private val recoverTime: TextView = itemView.findViewById(R.id.recoverTime)

        fun bind(dto: WorkoutSetDTO) {
            Timber.d("Workout binding workout set : $dto")

            exerciseTypeIcon.setBackground(dto.exerciseTypeDTO!!.iconName!!.getDrawableId(itemView.context))
            exerciseTypeIcon.setColor(dto.exerciseTypeDTO!!.colorHex!!.getColorId())
            exerciseTypeName.text = dto.exerciseTypeDTO!!.name
            workTime.text = dto.workTime.toString()
            restTime.text = dto.restTime.toString()
            numReps.text = dto.numReps.toString()
            recoverTime.text = dto.recoverTime.toString()
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<WorkoutSetDTO>() {
        override fun areItemsTheSame(oldItem: WorkoutSetDTO, newItem: WorkoutSetDTO): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: WorkoutSetDTO,
            newItem: WorkoutSetDTO
        ): Boolean {
            return oldItem == newItem
        }
    }
}