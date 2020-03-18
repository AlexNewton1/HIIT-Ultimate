package com.softwareoverflow.hiit_trainer.ui.view.picker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.ui.view.CircularImageView

class IconPagerAdapter(ids: MutableList<Int>) :
    IntArrayPagerAdapterBase<IconPagerAdapter.IconViewHolder>(ids) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        return IconViewHolder.from(parent)
    }

    override fun bindDataToViewHolder(holder: IconViewHolder, item: Int) {
        holder.bind(item)
    }

    class IconViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: CircularImageView = itemView.findViewById(R.id.list_color_image)

        fun bind(drawableId: Int) {
            imageView.setColor(android.R.color.background_dark)
            imageView.setBackground(drawableId)
        }

        companion object {
            fun from(parent: ViewGroup): IconViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.list_item_icon, parent, false)

                return IconViewHolder(view)
            }
        }
    }

}