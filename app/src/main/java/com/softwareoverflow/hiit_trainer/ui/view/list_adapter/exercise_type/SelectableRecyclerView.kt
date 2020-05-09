package com.softwareoverflow.hiit_trainer.ui.view.list_adapter.exercise_type

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView which supports item selection and contains [selectedItemId]
 */
class SelectableRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    // TODO do something with the adapter and use an interface to avoid having the casting. Also remove casting from the binding adapters

    /*init {
        this.adapter =
            ExerciseTypePickerListAdapter()
        addItemDecoration(
            SpacedListDecoration(
                context,
                (this.layoutManager as GridLayoutManager).spanCount
            )
        )
    }

    override fun getAdapter(): ExerciseTypePickerListAdapter {
        return super.getAdapter() as ExerciseTypePickerListAdapter
    }

    fun setAdapterListener(eventListener: ISelectableEditableListEventListener){
        adapter.setEventListener(eventListener)
    }

    fun submitList(items: List<ExerciseTypeDTO>) {
        adapter.submitList(items.toMutableList())
    }

    fun setSelectedItem(id: Long){
        adapter.notifyItemSelected(id)
    }*/
}
