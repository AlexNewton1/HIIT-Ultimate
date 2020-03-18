package com.softwareoverflow.hiit_trainer.ui.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.softwareoverflow.hiit_trainer.R

class ColorPagerAdapter(context: Context) :
    RecyclerView.Adapter<ColorPagerAdapter.ColorViewHolder>() {

    private val colorIds: MutableList<Int> =
        context.resources.getIntArray(R.array.et_colors).toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        return ColorViewHolder.from(parent)
    }

    override fun getItemCount(): Int = colorIds.size

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val colorId = colorIds[position]
        holder.bind(colorId)
    }

    /**
     * Loops the data set as if the user is moving forward. Takes the first item(s) and move to the
     * last item(s)
     */
    fun loopForward(numPages: Int) {
        for(i in 0 until numPages){
            val removed = colorIds.removeAt(0)
            colorIds.add(removed)
        }
    }

    /**
     * Loops the data set as if the user is moving backward. Takes the last item(s) and move to
     * front
     */
    fun loopBackward(numPages: Int) {
        for(i in 0 until numPages){
            val removed = colorIds.removeAt(colorIds.size -1)
            colorIds.add(0, removed)
        }
    }

    override fun getItemId(position: Int): Long {
        return colorIds[position].toLong()
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