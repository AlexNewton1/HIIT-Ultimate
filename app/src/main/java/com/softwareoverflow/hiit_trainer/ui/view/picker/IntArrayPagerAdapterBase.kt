package com.softwareoverflow.hiit_trainer.ui.view.picker

import androidx.recyclerview.widget.RecyclerView

abstract class IntArrayPagerAdapterBase<T : RecyclerView.ViewHolder>(private val ids: MutableList<Int>) :
    RecyclerView.Adapter<T>() {

    final override fun getItemCount(): Int = ids.size

    final override fun onBindViewHolder(holder: T, position: Int) {
        val item = ids[position]
        bindDataToViewHolder(holder, item)
    }

    final override fun getItemId(position: Int): Long {
        return ids[position].toLong()
    }

    abstract fun bindDataToViewHolder(holder: T, item: Int)

    /**
     * Loops the data set as if the user is moving forward. Takes the first item and move to the
     * last item
     */
    fun loopForward() {
        val removed = ids.removeAt(0)
        ids.add(removed)

        notifyItemMoved(0, itemCount - 1)
    }

    /**
     * Loops the data set as if the user is moving backward. Takes the last item and move to
     * front
     */
    fun loopBackward() {
        val removed = ids.removeAt(ids.size - 1)
        ids.add(0, removed)

        notifyItemMoved(itemCount - 1, 0)
    }
}