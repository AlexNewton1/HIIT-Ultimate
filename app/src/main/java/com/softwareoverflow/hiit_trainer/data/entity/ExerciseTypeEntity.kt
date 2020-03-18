package com.softwareoverflow.hiit_trainer.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "ExerciseType")
class ExerciseType {

    @PrimaryKey(autoGenerate = true) val id: Int? = null

    var name: String = ""
    var icon: String = ""
}