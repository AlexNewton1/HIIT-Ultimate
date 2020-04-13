package com.softwareoverflow.hiit_trainer.ui.view.exercise_type_picker

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import timber.log.Timber

/*@InverseBindingMethods(
    InverseBindingMethod(
        type = SelectableRecyclerView::class,
        attribute = "selectedItemId",
        event = "selectedItemIdAttrChanged",
        method = "getSelectedItemId"
    )
)*/
class SelectableRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    private var selectedItemId: Long? = null

    init {
        this.adapter = ExerciseTypePickerListAdapter()
        addItemDecoration(GridListDecoration(context))
    }

    fun submitList(items: List<ExerciseTypeDTO>?, currentlySelectedItem: Long?) {
        Timber.d("1waybind: got new items to submit! $items")
        items?.let {
            (adapter as ExerciseTypePickerListAdapter).let { adapter ->
                adapter.submitList(items) {
                    adapter.notifyItemSelected(currentlySelectedItem)
                }
            }
        }
    }

    fun notifyItemSelected(item: Long?) {
        (adapter as ExerciseTypePickerListAdapter).notifyItemSelected(item)
    }

    fun setSelectedItemListener(listener: ISelectableListener) {
        (adapter as ExerciseTypePickerListAdapter).setSelectedItemChangeListener(listener)
    }

    /*fun addSelectedItemChangedListener(listener: InverseBindingListener){
        // TODO find a better way of doing this so it doesn't need a nasty cast
        (adapter as ExerciseTypePickerListAdapter).addSelectedItemChangeListener(this)
        this.listener = listener
    }*/

    /*override fun onItemSelected(selected: Long?) {
        Timber.d("2waybind onItemSelected with id $selected")
        selectedItemId = selected

        Timber.d("2waybind Notifying listener of onChange() with listener $listener")
        listener?.onChange()
    }*/

/*    @BindingAdapter("selectedItemId")
    fun setSelectedItemId(newId: Long?) {
        Timber.d("2waybind Setting selectedItemId to $newId")
        if (selectedItemId != newId) {
            selectedItemId = newId
            (adapter as ExerciseTypePickerListAdapter).notifyItemSelected(selectedItemId)
        }
    }

    @InverseBindingAdapter(attribute = "selectedItemId")
    fun getSelectedItemId(something: Long): Long? {
        Timber.d("2waybind Returning selectedItemId $selectedItemId")
        return selectedItemId
    }*/

    companion object {
        /*@BindingAdapter("selectedItemId")
        @JvmStatic fun setSelectedItemId(view: SelectableRecyclerView, newId: Long?){
            Timber.d("2waybind Setting selectedItemId to $newId on view $view")
            if(view.selectedItemId != newId) {
                view.selectedItemId = newId

                // TODO find a better way of doing this so it doesn't need a nasty cast
                (view.adapter as ExerciseTypePickerListAdapter).notifyItemSelected(newId)
            }
        }

        @BindingAdapter("selectedItemIdAttrChanged")
        @JvmStatic
        fun onSelectedItemIdChanged(
            view: SelectableRecyclerView,
            listener: InverseBindingListener
        ) {
            Timber.d("2waybind trying to set a listener for the selected item... $listener on view $view")
            (view.adapter as ExerciseTypePickerListAdapter).addSelectedItemChangeListener(object :
                ISelectableListener {
                override fun onItemSelected(selected: Long?) {
                    view.selectedItemId = selected
                    Timber.d("2waybind calling listener onChange from the inverse binder on view $view with item ${view.selectedItemId}")
                    listener.onChange()
                }
            })
        }

        @InverseBindingAdapter(attribute = "selectedItemId")
        @JvmStatic fun getSelectedItemId(view: SelectableRecyclerView) : Long? {
            Timber.d("2waybind Returning selectedItemId ${view.selectedItemId}")
            return view.selectedItemId
        }*/
    }
}
