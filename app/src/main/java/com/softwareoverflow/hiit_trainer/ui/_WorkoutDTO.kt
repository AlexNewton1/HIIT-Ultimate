package com.softwareoverflow.hiit_trainer.ui

import android.content.res.Resources
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO


fun WorkoutDTO.getFormattedDuration(res: Resources) : String {
    var totalTime = 0

    for(i in 0 until  workoutSets.size){
        val workoutSet = workoutSets[i]

        totalTime += (workoutSet.workTime!! + workoutSet.restTime!!) * workoutSet.numReps!!
        totalTime -= workoutSet.restTime!! // There is no rest period at the end of a workout set

        if(i != workoutSets.size - 1)
            totalTime += workoutSet.recoverTime!!
    }

    return res.getString(R.string.min_sec_time_format, totalTime / 60, totalTime % 60)
}