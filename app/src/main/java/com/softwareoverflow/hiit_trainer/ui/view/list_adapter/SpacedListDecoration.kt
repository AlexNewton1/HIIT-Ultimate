package com.softwareoverflow.hiit_trainer.ui.view.list_adapter

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.softwareoverflow.hiit_trainer.R

/**
 * Spaces items recycler view items. Works for a single list or an item grid by specifying
 * the numColumns parameter.
 * @param context The context
 * @param numColumns The number of columns in the RecyclerView. Defaults to 1.
 */
class SpacedListDecoration(context: Context, private val numColumns: Int = 1) :
    RecyclerView.ItemDecoration() {

    private var gridSpacingPx: Int = 0
    private var needsLeftSpacing = false

    init {
        gridSpacingPx = context.resources.getDimensionPixelSize(R.dimen.grid_layout_spacing)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val frameWidth = ((parent.width - gridSpacingPx * (numColumns - 1)) / numColumns)
        val padding = parent.width / numColumns - frameWidth

        val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition

        if (itemPosition < numColumns) // 0 top border for first row
            outRect.top = 0
        else
            outRect.top = gridSpacingPx

        if (itemPosition % numColumns == 0) {
            outRect.left = 0
            outRect.right = padding
            needsLeftSpacing = true
        } else if ((itemPosition + 1) % numColumns == 0) {
            needsLeftSpacing = false
            outRect.right = 0
            outRect.left = padding
        } else if (needsLeftSpacing) {
            needsLeftSpacing = false
            outRect.left = gridSpacingPx - padding
            if ((itemPosition + 2) % numColumns == 0)
                outRect.right = gridSpacingPx - padding
            else
                outRect.right = gridSpacingPx / 2
        } else if ((itemPosition + 2) % numColumns == 0) {
            needsLeftSpacing = false
            outRect.left = gridSpacingPx / 2
            outRect.right = gridSpacingPx - padding
        } else {
            needsLeftSpacing = false
            outRect.left = gridSpacingPx / 2
            outRect.right = gridSpacingPx / 2
        }
        outRect.bottom = 0
    }


}