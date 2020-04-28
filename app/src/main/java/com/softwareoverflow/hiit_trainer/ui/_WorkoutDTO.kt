package com.softwareoverflow.hiit_trainer.ui

import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO

/**
 * Gets the duration of the workout, in seconds
 */
fun WorkoutDTO.getDuration() : Int {
    var totalTime = 0

    for(i in 0 until  workoutSets.size){
        val workoutSet = workoutSets[i]

        totalTime += (workoutSet.workTime!! + workoutSet.restTime!!) * workoutSet.numReps!!
        totalTime -= workoutSet.restTime!! // There is no rest period at the end of a workout set

        if(i != workoutSets.size - 1)
            totalTime += workoutSet.recoverTime!!
    }

    return totalTime
}