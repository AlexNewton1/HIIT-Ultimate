package com.softwareoverflow.hiit_trainer.ui.view.picker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.ui.view.CircularImageView

// TODO convert this into a more reusable form so that the InfiniteScrollPageChangeListener can be used across multiple

class ColorPagerAdapter(ids: MutableList<Int>) :
    IntArrayPagerAdapterBase<ColorPagerAdapter.ColorViewHolder>(ids) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        return ColorViewHolder.from(parent)
    }

    override fun bindDataToViewHolder(holder: ColorViewHolder, item: Int) {
        holder.bind(item)
    }

    class ColorViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: CircularImageView = itemView.findViewById(R.id.list_color_image)

        fun bind(colorId: Int) {
            imageView.setColor(colorId)
        }

        companion object {
            fun from(parent: ViewGroup): ColorViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.list_item_color, parent, false)

                return ColorViewHolder(view)
            }
        }
    }
}