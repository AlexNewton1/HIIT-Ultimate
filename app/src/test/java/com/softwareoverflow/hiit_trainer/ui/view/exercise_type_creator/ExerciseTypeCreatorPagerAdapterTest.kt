package com.softwareoverflow.hiit_trainer.ui.view.exercise_type_creator

import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.exercise_type.ExerciseTypeCreatorPagerAdapter
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ExerciseTypeCreatorPagerAdapterTest {

    lateinit var adapterCreator: ExerciseTypeCreatorPagerAdapter
    private val adapterList = arrayListOf(1, 2, 3, 4, 5, 6, 7)

    @Before
    fun init() {
        adapterCreator =
            ExerciseTypeCreatorPagerAdapter(
                ExerciseTypeCreatorPagerAdapter.Companion.ExerciseTypeAdapter.ICON,
                adapterList
            )
    }

    @Test
    fun moveItemToCenter_unchanged() {
        adapterCreator.moveItemToCenter(4)
        checkAdapterValues(adapterList)
    }

    @Test
    fun moveItemToCenter_fromPreMiddle(){
        adapterCreator.moveItemToCenter(3)
        checkAdapterValues(arrayListOf(7, 1, 2, 3, 4, 5, 6))

        adapterCreator.moveItemToCenter(1)
        checkAdapterValues(arrayListOf(5, 6, 7, 1, 2, 3, 4))
    }

    @Test
    fun moveItemToCenter_fromPostMiddle(){
        adapterCreator.moveItemToCenter(6)
        checkAdapterValues(arrayListOf(3, 4, 5, 6, 7, 1, 2))

        adapterCreator.moveItemToCenter(7)
        checkAdapterValues(arrayListOf(4, 5, 6, 7, 1, 2, 3))
    }

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
        assertEquals(expectedValues.size, adapterCreator.itemCount)

        for (i in 0 until expectedValues.size){
            assertEquals(expectedValues[i], adapterCreator.getItemId(i).toInt())
        }
    }
}