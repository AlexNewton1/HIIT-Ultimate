package com.softwareoverflow.hiit_trainer.ui.view.list_adapter.workout

import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.BasicDiffCallback
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.DataBindingAdapter
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.IAdapterOnLongClickListener
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.IEditableOrderedListEventListener

class WorkoutSetListAdapter :
    DataBindingAdapter<WorkoutSetDTO>(
        BasicDiffCallback(),
        AdapterLongClickListener()
    ) {

    override fun submitList(list: MutableList<WorkoutSetDTO>?) {
        super.submitList(list?.sortedBy { it.orderInWorkout }?.toMutableList())
    }

    override fun submitList(list: MutableList<WorkoutSetDTO>?, commitCallback: Runnable?) {
        super.submitList(list?.sortedBy { it.orderInWorkout }, commitCallback)
    }

    fun setEventListener(listener: IEditableOrderedListEventListener) = AdapterLongClickListener.setEventListener(listener)

    override fun getItemViewType(position: Int) = R.layout.list_item_workout_set

    override fun getColorHexForItem(item: WorkoutSetDTO) =  item.exerciseTypeDTO!!.colorHex!!

    // TODO convert all of this to use the newer way of binding with the base adapter (once it's working!)
    /*inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
    PopupMenu.OnMenuItemClickListener {
    private val exerciseTypeIcon: CircularIconImageView =
        itemView.findViewById(R.id.exerciseTypeIcon)
    private val exerciseTypeName: TextView = itemView.findViewById(R.id.exerciseTypeName)
    private val workTime: TextView = itemView.findViewById(R.id.workTime)
    private val restTime: TextView = itemView.findViewById(R.id.restTime)
    private val numReps: TextView = itemView.findViewById(R.id.numReps)
    private val recoverTime: TextView = itemView.findViewById(R.id.recoverTime)

    fun bind(dto: WorkoutSetDTO) {
        val color = dto.exerciseTypeDTO!!.colorHex!!.getColorId()
        itemView.background.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        itemView.background.alpha = 75

        exerciseTypeIcon.setBackground(dto.exerciseTypeDTO!!.iconName!!.getDrawableId(itemView.context))
        exerciseTypeIcon.setColor(color)
        exerciseTypeName.text = dto.exerciseTypeDTO!!.name
        workTime.text = dto.workTime.toString()
        restTime.text = dto.restTime.toString()
        numReps.text = dto.numReps.toString()
        recoverTime.text = dto.recoverTime.toString()
    }
}*/

    class AdapterLongClickListener : IAdapterOnLongClickListener<WorkoutSetDTO>,
        PopupMenu.OnMenuItemClickListener {

        companion object {
            private var eventListener: IEditableOrderedListEventListener? = null

            fun setEventListener(listener: IEditableOrderedListEventListener) {
                eventListener = listener
            }
        }

        private var clickedPosition: Int = -1

        override fun onLongClick(view: View, item: WorkoutSetDTO, position: Int, isLastItem: Boolean) {
            clickedPosition = position

            PopupMenu(view.context, view).apply {
                setOnMenuItemClickListener(this@AdapterLongClickListener)
                inflate(R.menu.workout_set_actions)
                menu.findItem(R.id.workout_set_menu_move_up).isEnabled = (position > 0)
                menu.findItem(R.id.workout_set_menu_move_down).isEnabled = !isLastItem
                show()
            }
        }

        override fun onMenuItemClick(item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.workout_set_menu_move_down -> {
                    eventListener?.triggerItemChangePosition(clickedPosition, clickedPosition + 1)
                    true
                }
                R.id.workout_set_menu_move_up -> {
                    eventListener?.triggerItemChangePosition(clickedPosition, clickedPosition - 1)
                    true
                }
                R.id.workout_set_menu_edit -> {
                    eventListener?.triggerItemEdit(clickedPosition.toLong())
                    true
                }
                R.id.workout_set_menu_delete -> {
                    eventListener?.triggerItemDeletion(clickedPosition.toLong())
                    true
                }
                else -> false
            }
        }
    }
}