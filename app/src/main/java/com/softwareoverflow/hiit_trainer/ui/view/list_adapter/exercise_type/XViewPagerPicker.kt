package com.softwareoverflow.hiit_trainer.ui.view.list_adapter.exercise_type

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.getResourceIdOrThrow
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.ui.utils.getColorId
import com.softwareoverflow.hiit_trainer.ui.utils.getDrawableId
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.exercise_type.ExerciseTypeCreatorPagerAdapter.Companion
import kotlinx.android.synthetic.main.x_view_pager_picker.view.*

class XViewPagerPicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    init {
        inflate(context, R.layout.x_view_pager_picker, this)

        setupColorPager()
        setupIconPager()
    }

    private fun setupColorPager() {
        val colorHexIds: MutableList<String> = resources.getStringArray(R.array.et_colors).toMutableList()
        val ids = colorHexIds.map{
            it.getColorId()
        }.toMutableList()

        val colorAdapter =
            ExerciseTypeCreatorPagerAdapter(
                Companion.ExerciseTypeAdapter.COLOR,
                ids
            )

        setupPager(colorViewPager, colorAdapter)
    }

    private fun setupIconPager() {
        val ids: MutableList<Int> = ArrayList()
        val icons = resources.obtainTypedArray(R.array.et_icons)
        try {
            for (i in 0 until icons.length()) {
                ids.add(icons.getResourceIdOrThrow(i))
            }
        } finally {
            icons.recycle()
        }

        val iconAdapter =
            ExerciseTypeCreatorPagerAdapter(
                Companion.ExerciseTypeAdapter.ICON,
                ids
            )

        setupPager(iconViewPager, iconAdapter)
    }

    /* TODO FUTURE VERSION investigate the issues caused by swiping too quickly when the view is already scrolling to the initial position
        This is deemed to be an unlikely use case, and as the only issue is UI issue which is solved by leaving and returning to the page
    */
    private fun <T : RecyclerView.ViewHolder> setupPager(
        pager: ViewPager2,
        adapter: RecyclerView.Adapter<T>
    ) {
        pager.isUserInputEnabled = false

        with(pager) {
            this.adapter = adapter

            clipToPadding = false
            clipChildren = false
            currentItem = adapter.itemCount / 2 // Default to starting at the middle item
            offscreenPageLimit = 4

            setPageTransformer(MultipleVisiblePagesTransformer())
            registerOnPageChangeCallback(
                InfiniteScrollPageChangeListener(
                    this
                )
            )
        }

        pager.isUserInputEnabled = true
    }

    fun setIconByName(iconName: String) {
        val adapter = (iconViewPager.adapter as ExerciseTypeCreatorPagerAdapter)

        val iconId = iconName.getDrawableId(context)
        adapter.moveItemToCenter(iconId)
        adapter.notifyDataSetChanged()

        // Set the current item to half way through now the required item has been centered.
        iconViewPager.currentItem = adapter.itemCount / 2
    }

    fun setColorByHex(colorHex: String?) {
        // TODO FUTURE VERSION change this to avoid having to cast
        val adapter = (colorViewPager.adapter as ExerciseTypeCreatorPagerAdapter)

        colorHex?.let {
            val colorId = colorHex.getColorId()
            adapter.moveItemToCenter(colorId)
            adapter.notifyDataSetChanged()
        }

        colorViewPager.currentItem = adapter.itemCount / 2
    }

    fun getColorId(): Int {
        return colorViewPager.adapter!!.getItemId(colorViewPager.currentItem).toInt()
    }

    fun getIconId(): Int {
        return iconViewPager.adapter!!.getItemId(iconViewPager.currentItem).toInt()
    }

}