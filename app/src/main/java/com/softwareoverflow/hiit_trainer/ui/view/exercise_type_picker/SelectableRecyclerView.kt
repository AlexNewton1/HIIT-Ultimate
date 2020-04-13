package com.softwareoverflow.hiit_trainer.ui.view.exercise_type_picker

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

    val selectedItemId: Long?
        get() = (adapter as ExerciseTypePickerListAdapter).getSelectedItemId()

    init {
        this.adapter = ExerciseTypePickerListAdapter()
        addItemDecoration(GridListDecoration(context))
    }
}
