package com.softwareoverflow.hiit_trainer.ui

import android.content.Context
import androidx.preference.PreferenceManager
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO
import com.softwareoverflow.hiit_trainer.ui.utils.SharedPreferencesManager

/**
 * Gets the duration of the workout, in seconds
 */
fun WorkoutDTO.getDuration(): Int {
    var totalTime = 0

    for (i in 0 until workoutSets.size) {
        val workoutSet = workoutSets[i]

        totalTime += (workoutSet.workTime + workoutSet.restTime) * workoutSet.numReps
        totalTime -= workoutSet.restTime // There is no rest period at the end of a workout set

        if (i != workoutSets.size - 1)
            totalTime += workoutSet.recoverTime
    }

    totalTime += recoveryTime
    totalTime *= numReps
    totalTime -= recoveryTime // Don't need to recover after the last cycle

    return totalTime
}

fun WorkoutDTO.getFormattedDuration(): String {
    val duration = getDuration()
    return String.format("%02d:%02d", duration / 60, duration % 60)
}

fun WorkoutDTO.getFullWorkoutSets(context: Context) : List<WorkoutSetDTO> {
    val updatedWorkoutSets = ArrayList(workoutSets)

    for(i in 1 until numReps) {
        updatedWorkoutSets.last().recoverTime = this.recoveryTime // Set the final recovery (otherwise unused) to be the workouts recovery time
        updatedWorkoutSets.addAll(this.workoutSets)
    }

    updatedWorkoutSets.forEachIndexed { i, dto ->
        dto.orderInWorkout = i
    }

    val prepSet = getWorkoutPrepSet(context)
    if(prepSet != null){
        updatedWorkoutSets.add(0, prepSet)
    }

    return updatedWorkoutSets
}

fun getWorkoutPrepSet(context: Context): WorkoutSetDTO? {
    val sp = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
    val isEnabled = sp.getBoolean(SharedPreferencesManager.prepSetEnabled, true)

    if(isEnabled){
        return WorkoutSetDTO(
            ExerciseTypeDTO(
                null,
                context.getString(R.string.get_ready),
                "icon_heart_pulse",
                "#FF000000"
            ),
            sp.getString(SharedPreferencesManager.prepSetTime, "5")!!.toInt(),
            0,
            1,
            0
        )
    }
    else {
        return null
    }
}

fun getWorkoutCompleteExerciseType(context: Context): ExerciseTypeDTO = ExerciseTypeDTO(
    null,
    context.getString(R.string.workout_complete),
    "icon_trophy",
    "#FF000000"
)