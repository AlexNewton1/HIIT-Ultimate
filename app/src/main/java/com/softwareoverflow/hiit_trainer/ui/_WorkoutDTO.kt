package com.softwareoverflow.hiit_trainer.ui

import android.content.res.Resources
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO


fun WorkoutDTO.getFormattedDuration(res: Resources) : String {
    var totalTime = 0

    workoutSets.forEach {
        totalTime += (it.workTime + it.restTime) * it.numReps
        totalTime -= it.restTime // There is no rest period at the end of a workout set
    }

    return res.getString(R.string.min_sec_time_format, totalTime / 60, totalTime % 60)
}