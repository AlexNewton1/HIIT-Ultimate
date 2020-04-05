package com.softwareoverflow.hiit_trainer.ui.view.picker

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.getResourceIdOrThrow
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.ui.getColorId
import com.softwareoverflow.hiit_trainer.ui.getDrawableId
import com.softwareoverflow.hiit_trainer.ui.view.picker.ExerciseTypePagerAdapter.Companion
import kotlinx.android.synthetic.main.x_view_pager_picker.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class XViewPagerPicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private var job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    init {
        inflate(context, R.layout.x_view_pager_picker, this)

        setupColorPager()
        setupIconPager()
    }

    // TODO maybe make a custom view extending ViewPager2 which can handle the setting by name, moving forward / backward etc etc instead of handling iconPager and ColorPager here

    private fun setupColorPager() {
        val colorHexIds: MutableList<String> = resources.getStringArray(R.array.et_colors).toMutableList()
        val ids = colorHexIds.map{
            it.getColorId()
        }.toMutableList()

        val colorAdapter = ExerciseTypePagerAdapter(Companion.ExerciseTypeAdapter.COLOR, ids)

        // TODO change this if it works for iconAdapter
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

        val iconAdapter = ExerciseTypePagerAdapter(Companion.ExerciseTypeAdapter.ICON, ids)

        setupPager(iconViewPager, iconAdapter)
    }

    private fun <T : RecyclerView.ViewHolder> setupPager(
        pager: ViewPager2,
        adapter: RecyclerView.Adapter<T>
    ) {
        with(pager) {
            this.adapter = adapter

            clipToPadding = false
            clipChildren = false
            currentItem = adapter.itemCount / 2 // Default to starting at the middle item
            offscreenPageLimit = 5

            setPageTransformer(MultipleVisiblePagesTransformer(context))
            registerOnPageChangeCallback(InfiniteScrollPageChangeListener(this))
        }
    }

    fun setIconByName(iconName: String) {
        val adapter = (iconViewPager.adapter as ExerciseTypePagerAdapter)

        val iconId = iconName.getDrawableId(context)
        adapter.moveItemToCenter(iconId)
        adapter.notifyDataSetChanged()

        uiScope.launch {
            // Set the current item to half way through now the required item has been centered.
            // Account for 0 indexing and odd-length lists
            iconViewPager.currentItem = adapter.itemCount / 2
        }
    }

    fun setColorByHex(colorHex: String?) {
        // TODO - change this to be an interface!
        val adapter = (colorViewPager.adapter as ExerciseTypePagerAdapter)

        colorHex?.let {
            val colorId = colorHex.getColorId()
            adapter.moveItemToCenter(colorId)
            adapter.notifyDataSetChanged()
        }

        uiScope.launch {
            // Set the current item to half way through now the required item has been centered.
            // Account for 0 indexing and odd-length lists
            colorViewPager.currentItem = adapter.itemCount / 2
        }
    }

    fun getColorId(): Int {
        return colorViewPager.adapter!!.getItemId(colorViewPager.currentItem).toInt()
    }

    fun getIconId(): Int {
        return iconViewPager.adapter!!.getItemId(iconViewPager.currentItem).toInt()
    }

    /*
    TODO investigate use cases architecture further as a potential alternative to the DTO model

    TODO investigate if DiffUtil and ListAdapter can be used for the ViewPager2 instead of manually removing and adding the items each swipe

    TODO create some fake data for the database.
    TODO create recyclerview and listAdapter for showing all of the exercise types
    TODO use DiffUtil for it
     */
}