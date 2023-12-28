package com.softwareoverflow.hiit_trainer.ui.history.write

import com.softwareoverflow.hiit_trainer.ui.workout.WorkoutSection

interface IHistorySaver {

    fun addHistory(seconds: Int, section: WorkoutSection, name: String)

    fun write()
}