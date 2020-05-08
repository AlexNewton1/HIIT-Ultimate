package com.softwareoverflow.hiit_trainer.ui.view.list_adapter

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.softwareoverflow.hiit_trainer.ui.getColorId
import timber.log.Timber

/**
 * Helper class to help with the boilerplate code for using data binding in a [ListAdapter] with [DiffUtil]
 * NOTE - to use this class the layout provided by [getItemViewType] MUST contain a data binding variable named "dto"
 */
abstract class DataBindingAdapter<T>(
    diffCallback: DiffUtil.ItemCallback<T>,
    private val longClickListener: IAdapterOnLongClickListener<T>?
) :
    ListAdapter<T, DataBindingAdapter.DataBindingViewHolder<T>>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return DataBindingViewHolder(
            binding,
            longClickListener
        )
    }

    override fun submitList(list: MutableList<T>?) {
        Timber.d("LoadWorkout: submitList called in DataBindingAdapter: $list")
        super.submitList(list) {
            Timber.d("LoadWorkout: submit list completed!")
        }
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<T>, position: Int) {
        Timber.d("LoadWorkout: onBindViewHolder called for position $position")
        val item = getItem(position)
        val color = getColorHexForItem(item)
        holder.bind(item, color.getColorId())
    }

    abstract fun getColorHexForItem(item: T): String

    abstract override fun getItemViewType(position: Int): Int

    class DataBindingViewHolder<T>(
        private val binding: ViewDataBinding,
        private val longClickListener: IAdapterOnLongClickListener<T>?
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: T, fadeColor: Int) {
            // Fade the background
            binding.root.background.colorFilter =
                PorterDuffColorFilter(fadeColor, PorterDuff.Mode.SRC_IN)
            binding.root.background.alpha = 75

            longClickListener?.let {
                binding.root.setOnLongClickListener{
                    longClickListener.onLongClick(binding.root, item, adapterPosition)
                    return@setOnLongClickListener true
                }
            }

            // set the binding value
            binding.setVariable(BR.dto, item)
            binding.executePendingBindings()

            binding
        }
    }
}