package com.softwareoverflow.hiit_trainer.ui.view.picker

import androidx.viewpager2.widget.ViewPager2

class InfiniteScrollPageChangeListener(private val viewPager: ViewPager2) :
    ViewPager2.OnPageChangeCallback() {

    private var previouslySelectedPage = 0
    private var selectedPage = 0
    private val pagerAdapter = viewPager.adapter as IntArrayPagerAdapterBase

    private var internalPageChange = false

    override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
        selectedPage = position

        if(!internalPageChange)
            notifyPageChange()
    }

    private fun notifyPageChange(){
        internalPageChange = true

        if( selectedPage < previouslySelectedPage) {// User is swiping right
            pagerAdapter.loopBackward()

            val newPage = selectedPage + 1
            previouslySelectedPage = newPage
            viewPager.setCurrentItem(newPage, false)
        }
        else { // User is swiping left
            pagerAdapter.loopForward()

            val newPage = selectedPage - 1
            previouslySelectedPage = newPage
            viewPager.setCurrentItem(newPage, false)
        }

        internalPageChange = false
    }
}