package com.softwareoverflow.hiit_trainer.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Workout")
class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    var name: String = ""
)