package com.softwareoverflow.hiit_trainer.ui.view.list_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.ui.getColorId
import timber.log.Timber

/**
 * Helper class to help with the boilerplate code for using data binding in a [ListAdapter] with [DiffUtil]
 * NOTE - to use this class the layout provided by [getItemViewType] MUST contain a data binding variable named "dto"
 * A [View.OnLongClickListener] will automatically be added for long clicks on any row.
 * A [View.OnClickListener] will automatically be added on a view with id "selectOnClick" within the view.
 */
abstract class DataBindingAdapter<T>(
    diffCallback: DiffUtil.ItemCallback<T>,
    private val longClickListener: IAdapterOnLongClickListener<T>?
) :
    ListAdapter<T, DataBindingViewHolderBase<T>>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolderBase<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return DataBindingViewHolderBase(binding)
    }

    override fun submitList(list: MutableList<T>?) {
        Timber.d("LoadWorkout: submitList called in DataBindingAdapter: $list")
        super.submitList(list) {
            Timber.d("LoadWorkout: submit list completed!")
        }
    }

    override fun onBindViewHolder(holder: DataBindingViewHolderBase<T>, position: Int) {
        val item = getItem(position)

        holder.itemView.findViewById<View>(R.id.selectOnClick)?.setOnClickListener {
            longClickListener?.onLongClick(it, item, position, false)
        }
        holder.itemView.setOnLongClickListener {
            longClickListener?.onLongClick(it, item, position, true)
            return@setOnLongClickListener true
        }

        val color = getColorHexForItem(item)
        holder.bind(item, color.getColorId())
    }

    abstract fun getColorHexForItem(item: T): String

    abstract override fun getItemViewType(position: Int): Int
}