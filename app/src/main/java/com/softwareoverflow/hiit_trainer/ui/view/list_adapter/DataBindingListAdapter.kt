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

/**
 * Helper class to help with the boilerplate code for using data binding in a [ListAdapter] with [DiffUtil]
 * NOTE - to use this class the layout provided by [getItemViewType] must contain a data binding variable named "dto"
 * A [View.OnLongClickListener] will automatically be added for long clicks on any row.
 * A [View.OnClickListener] will automatically be added on a view with id "selectOnClick" within the layout returned from [getItemViewType].
 */
abstract class DataBindingAdapter<T>(
    diffCallback: DiffUtil.ItemCallback<T>,
    internal val clickListener: IAdapterOnClickListener<T>?
) :
    ListAdapter<T, DataBindingViewHolderBase<T>>(diffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataBindingViewHolderBase<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return DataBindingViewHolderBase(binding)
    }

    internal fun getPositionForItem(dto: T) =
        currentList.indexOf(dto)

    override fun onBindViewHolder(holder: DataBindingViewHolderBase<T>, position: Int) {
        val item = getItem(position)

        holder.itemView.findViewById<View>(R.id.selectOnClick)?.setOnClickListener {
            clickListener?.onClick(it, item, getPositionForItem(item), false)
        }
        holder.itemView.setOnLongClickListener {
            clickListener?.onClick(it, item, getPositionForItem(item), true)
            return@setOnLongClickListener true
        }

        val color = getColorHexForItem(item)
        holder.bind(item, color.getColorId())
    }

    /**
     * Get the hex color string to use for this item in the list
     */
    abstract fun getColorHexForItem(item: T): String

    /**
     * gets the layout id for the layout to be used for the item at this position
     */
    abstract override fun getItemViewType(position: Int): Int
}