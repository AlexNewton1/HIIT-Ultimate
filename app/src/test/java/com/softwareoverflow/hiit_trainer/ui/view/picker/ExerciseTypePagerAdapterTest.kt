package com.softwareoverflow.hiit_trainer.ui.view.picker

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ExerciseTypePagerAdapterTest {

    lateinit var adapter: ExerciseTypePagerAdapter
    private val adapterList = arrayListOf(1, 2, 3, 4, 5, 6, 7)

    @Before
    fun init() {
        adapter = ExerciseTypePagerAdapter(
            ExerciseTypePagerAdapter.Companion.ExerciseTypeAdapter.ICON,
            adapterList
        )
    }

    @Test
    fun moveItemToCenter_unchanged() {
        adapter.moveItemToCenter(4)
        checkAdapterValues(adapterList)
    }

    @Test
    fun moveItemToCenter_fromPreMiddle(){
        adapter.moveItemToCenter(3)
        checkAdapterValues(arrayListOf(7, 1, 2, 3, 4, 5, 6))

        adapter.moveItemToCenter(1)
        checkAdapterValues(arrayListOf(5, 6, 7, 1, 2, 3, 4))
    }

    @Test
    fun moveItemToCenter_fromPostMiddle(){
        adapter.moveItemToCenter(6)
        checkAdapterValues(arrayListOf(3, 4, 5, 6, 7, 1, 2))

        adapter.moveItemToCenter(7)
        checkAdapterValues(arrayListOf(4, 5, 6, 7, 1, 2, 3))
    }

    // TODO work out how to fix these tests. loopForward / loopBackward both call notifyDatasetChanged which cannot be mocked
    /*@Test
    fun loopForward() {
        adapter.loopForward()
        checkAdapterValues(arrayListOf(2, 3, 4, 5, 6, 7, 1))

        adapter.loopForward()
        adapter.loopForward()
        adapter.loopForward()
        checkAdapterValues(arrayListOf(5, 6, 7, 1, 2, 3, 4))
    }

    @Test
    fun loopBackward() {
        adapter.loopBackward()
        checkAdapterValues(arrayListOf(7, 1, 2, 3, 4, 5, 6))

        adapter.loopBackward()
        adapter.loopBackward()
        checkAdapterValues(arrayListOf(5, 6, 7, 1, 2, 3, 4))
    }*/

    private fun checkAdapterValues(expectedValues: MutableList<Int>){
        assertEquals(expectedValues.size, adapter.itemCount)

        for (i in 0 until expectedValues.size){
            assertEquals(expectedValues[i], adapter.getItemId(i).toInt())
        }
    }
}