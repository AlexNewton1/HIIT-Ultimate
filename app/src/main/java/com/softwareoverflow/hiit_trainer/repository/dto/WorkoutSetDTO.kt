package com.softwareoverflow.hiit_trainer.repository.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkoutSetDTO(
    var exerciseTypeDTO: ExerciseTypeDTO? = null,
    var workTime: Int = 25,
    var restTime: Int = 5,
    var numReps: Int = 6,
    var recoverTime: Int = 120,
    var orderInWorkout: Int? = null
) : Parcelable