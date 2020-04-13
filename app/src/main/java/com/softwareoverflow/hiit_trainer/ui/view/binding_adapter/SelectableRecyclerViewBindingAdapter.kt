package com.softwareoverflow.hiit_trainer.ui.view.binding_adapter

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.ui.view.exercise_type_picker.ExerciseTypePickerListAdapter
import com.softwareoverflow.hiit_trainer.ui.view.exercise_type_picker.ISelectableListener
import com.softwareoverflow.hiit_trainer.ui.view.exercise_type_picker.SelectableRecyclerView

/**
 * Binding adapter for [SelectableRecyclerView]
 */
@BindingAdapter("data")
fun SelectableRecyclerView.setData(items: List<ExerciseTypeDTO>?) {
    if (this.adapter is ExerciseTypePickerListAdapter && items != null) {
        (this.adapter as ExerciseTypePickerListAdapter).submitList(items)
    }
}

/**
 * Binding adapter for [SelectableRecyclerView]
 */
@BindingAdapter("selectedItemId")
fun SelectableRecyclerView.setSelectedItemId(newId: Long?) {
    if (this.selectedItemId != newId) {
        // TODO find a better way of doing this so it doesn't need a nasty cast
        (this.adapter as ExerciseTypePickerListAdapter).notifyItemSelected(newId)
    }
}

/**
 * Binding adapter for [SelectableRecyclerView]
 */
@BindingAdapter("selectedItemIdAttrChanged")
fun SelectableRecyclerView.onSelectedItemIdChanged(listener: InverseBindingListener) {
    (this.adapter as ExerciseTypePickerListAdapter).setSelectedItemChangeListener(object :
        ISelectableListener {
        override fun onItemSelected(selected: Long?) {
            listener.onChange()
        }
    })
}

/**
 * Binding adapter for [SelectableRecyclerView]
 */
@InverseBindingAdapter(attribute = "selectedItemId")
fun SelectableRecyclerView.getSelectedItemId(): Long? {
    return this.selectedItemId
}