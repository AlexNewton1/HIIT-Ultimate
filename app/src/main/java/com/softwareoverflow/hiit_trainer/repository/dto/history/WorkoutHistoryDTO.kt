package com.softwareoverflow.hiit_trainer.repository.dto.history

import android.os.Parcelable
import com.softwareoverflow.hiit_trainer.ui.workout.WorkoutSection
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class WorkoutHistoryDTO(
    var time: Int, var name: String, val type: WorkoutSection, val date: LocalDate
) : Parcelable