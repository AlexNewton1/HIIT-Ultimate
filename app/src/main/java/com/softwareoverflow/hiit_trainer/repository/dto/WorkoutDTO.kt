package com.softwareoverflow.hiit_trainer.repository.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WorkoutDTO (
    var id: Long? = null,
    var name: String = "",
    var workoutSets: MutableList<WorkoutSetDTO> = ArrayList(),
    var numReps: Int = 1,
    var recoveryTime: Int = 120
) : Parcelable