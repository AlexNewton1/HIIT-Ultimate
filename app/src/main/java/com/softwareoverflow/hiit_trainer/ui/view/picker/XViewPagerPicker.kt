package com.softwareoverflow.hiit_trainer.ui.view.picker

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.getResourceIdOrThrow
import androidx.core.view.doOnLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.softwareoverflow.hiit_trainer.R
import kotlinx.android.synthetic.main.x_view_pager_picker.view.*

class XViewPagerPicker : ConstraintLayout {

    private val screenHeightPercentage = 0.175
    private var isInitialised = false

    constructor(context: Context) : super(context) {
        initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initialize(context)
    }

    private fun initialize(context: Context) {
        inflate(context, R.layout.x_view_pager_picker, this)

        setupColorPager()
        setupIconPager()

        colorViewPager.doOnLayout { v -> doOnLayout(v as ViewPager2) }
        iconViewPager.doOnLayout { v -> doOnLayout(v as ViewPager2) }
    }

    private fun doOnLayout(v: ViewPager2) {
        /*v.invalidate()*/

        /*if(!isInitialised) {
            if(v.adapter != null) {
                v.currentItem = v.adapter.itemCount / 2
            } else {
                v.currentItem = 0
            }

            v.currentItem = (v.adapter?.itemCount ?? 0) / 2 // Scroll to the middle
        }*/
    }

    private fun setupColorPager() {
        val ids: MutableList<Int> = resources.getIntArray(R.array.et_colors).toMutableList()
        /* Duplicate all the IDs so when the currentItem is set to halfway it smooth scrolls through
            all the options */
        ids.addAll(ids)
        val colorAdapter = ColorPagerAdapter(ids)
        setupPager(colorViewPager, colorAdapter)
    }

    private fun setupIconPager() {
        val ids: MutableList<Int> = ArrayList()
        val icons = resources.obtainTypedArray(R.array.et_icons)
        try {
            for (i in 0 until icons.length())
                ids.add(icons.getResourceIdOrThrow(i))
        } finally {
            icons.recycle()
        }
        /* Duplicate all the IDs so when the currentItem is set to halfway it smooth scrolls through
            all the options and presents options either side */
        ids.addAll(ids)

        val iconAdapter = IconPagerAdapter(ids)
        setupPager(iconViewPager, iconAdapter)
    }

    private fun <T : RecyclerView.ViewHolder> setupPager(
        pager: ViewPager2,
        adapter: RecyclerView.Adapter<T>
    ) {
        with(pager) {
            this.adapter = adapter

            offscreenPageLimit = 3
            clipToPadding = false
            clipChildren = false

            setPageTransformer(MultipleVisiblePagesTransformer(context))
            registerOnPageChangeCallback(InfiniteScrollPageChangeListener(pager))

            currentItem = adapter.itemCount / 2
        }
    }

    // TODO fix issue with EditText causing soft keyboard to royally screw up the icon pager

    // TODO investigate if DiffUtil and ListAdapter can be used for the ViewPager2 instead of manually removing and adding the items each swipe

    // TODO create some fake data for the database.
    // TODO create recyclerview and listAdapter for showing all of the exercise types
    // TODO use DiffUtil for it

 /*   override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val oldSize = colorViewPager.layoutParams.height

        val newSize = (h * screenHeightPercentage).toInt()
        colorViewPager.updateLayoutParams { height = newSize }
        iconViewPager.updateLayoutParams { width = newSize }

        Timber.d("Changing pager sizes to $newSize from $oldSize")

        *//*colorViewPager.invalidate()
        colorViewPager.requestLayout()*//*

        this.invalidate()
        this.requestLayout()

        // TODO get this working!

*//*        val newPagerLength = sqrt((w*w + h*h).toDouble())
        colorViewPager.x = -newSize / 2f
        colorViewPager.y = -newSize / 2f
        colorViewPager.updateLayoutParams { width = newPagerLength.toInt() }

        iconViewPager.x = w + newSize / 2f
        iconViewPager.y = -newSize / 2f
        iconViewPager.updateLayoutParams { height = newPagerLength.toInt() }

        colorViewPager.invalidate()*//*
    }*/

/*    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        this.invalidate()
        this.requestLayout()
    }*/

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        val widthPc = 285f / width
        val heightPc = 285f / height

        if(!isInitialised) {
            val newSize = (MeasureSpec.getSize(heightMeasureSpec) * screenHeightPercentage).toInt()

            colorViewPager.updateLayoutParams { this.height = newSize }
            iconViewPager.updateLayoutParams { this.width = newSize }

            isInitialised = true
        }
    }
}