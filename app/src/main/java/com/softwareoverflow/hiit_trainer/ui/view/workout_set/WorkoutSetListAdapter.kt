package com.softwareoverflow.hiit_trainer.ui.view.workout_set

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO
import com.softwareoverflow.hiit_trainer.ui.getColorId
import com.softwareoverflow.hiit_trainer.ui.getDrawableId
import com.softwareoverflow.hiit_trainer.ui.view.CircularIconImageView
import com.softwareoverflow.hiit_trainer.ui.view.IEditableOrderedListEventListener
import timber.log.Timber

class WorkoutSetListAdapter :
    ListAdapter<WorkoutSetDTO, WorkoutSetListAdapter.ViewHolder>(
        DiffCallback()
    ) {

    private var eventListener: IEditableOrderedListEventListener? = null

    override fun submitList(list: MutableList<WorkoutSetDTO>?) {
        super.submitList(list?.sortedBy { it.orderInWorkout })
    }

    override fun submitList(list: MutableList<WorkoutSetDTO>?, commitCallback: Runnable?) {
        super.submitList(list?.sortedBy { it.orderInWorkout }, commitCallback)
    }

    fun setEventListener(listener: IEditableOrderedListEventListener) {
        eventListener = listener
    }

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

    inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        PopupMenu.OnMenuItemClickListener {
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

            itemView.setOnLongClickListener {
                PopupMenu(itemView.context, it).apply {
                    setOnMenuItemClickListener(this@ViewHolder)
                    inflate(R.menu.workout_set_actions)
                    menu.findItem(R.id.workout_set_menu_move_up).isEnabled = (adapterPosition > 0)
                    menu.findItem(R.id.workout_set_menu_move_down).isEnabled =
                        adapterPosition < currentList.size - 1
                    show()
                }

                return@setOnLongClickListener true
            }
        }


        // TODO - create an event listener
        override fun onMenuItemClick(item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.workout_set_menu_move_down -> {
                    eventListener?.triggerItemChangePosition(layoutPosition, layoutPosition + 1)
                    true
                }
                R.id.workout_set_menu_move_up -> {
                    eventListener?.triggerItemChangePosition(layoutPosition, layoutPosition - 1)
                    true
                }
                R.id.workout_set_menu_edit -> {
                    eventListener?.triggerItemEdit(layoutPosition.toLong())
                    true
                }
                R.id.workout_set_menu_delete -> {
                    eventListener?.triggerItemDeletion(layoutPosition.toLong())
                    true
                }
                else -> false
            }
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