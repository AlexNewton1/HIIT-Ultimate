package com.softwareoverflow.hiit_trainer.repository.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExerciseTypeDTO(
    var id: Long? = null,
    var name: String? = null,
    var iconName: String? = null,
    var colorHex: String? = null
) : Parcelable