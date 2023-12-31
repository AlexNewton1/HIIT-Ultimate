package com.softwareoverflow.hiit_trainer.data.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.softwareoverflow.hiit_trainer.ui.workout.WorkoutSection
import java.time.LocalDate

@Entity(tableName = "workout_history", primaryKeys = ["_name", "_type", "_date_string"])
data class WorkoutHistoryEntity(

    @ColumnInfo(name = "_time")
    var time: Int = 0,

    @ColumnInfo(name = "_name")
    var name: String = "",

    @ColumnInfo(name = "_type")
    var type: WorkoutSection = WorkoutSection.WORK,

    @ColumnInfo(name = "_date_string")
    var date: LocalDate = LocalDate.now() // This will be converted by the converter to a string in the form "yyyyMMdd"
    )