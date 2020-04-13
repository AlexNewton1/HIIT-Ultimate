package com.softwareoverflow.hiit_trainer.ui.view.exercise_type_creator

import androidx.viewpager2.widget.ViewPager2

class InfiniteScrollPageChangeListener(private val viewPager: ViewPager2) :
    ViewPager2.OnPageChangeCallback() {

    private val pagerAdapter = viewPager.adapter as ExerciseTypeCreatorPagerAdapter
    private var previouslySelectedPage = viewPager.currentItem
    private var selectedPage = viewPager.currentItem

    private var internalPageChange = false

    override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
        selectedPage = position

        if(!internalPageChange)
            notifyPageChange()
    }

    private fun notifyPageChange(){
        // Post the changes to the adapter to avoid calling directly during the scroll callback
        viewPager.post {
            internalPageChange = true
            if (selectedPage < previouslySelectedPage) {// User is swiping right
                pagerAdapter.loopBackward()

                val newPage = selectedPage + 1
                previouslySelectedPage = newPage
                viewPager.setCurrentItem(newPage, false)
            } else if (selectedPage > previouslySelectedPage) { // User is swiping left
                pagerAdapter.loopForward()

                val newPage = selectedPage - 1
                previouslySelectedPage = newPage
                viewPager.setCurrentItem(newPage, false)
            }

            internalPageChange = false
        }
    }
}