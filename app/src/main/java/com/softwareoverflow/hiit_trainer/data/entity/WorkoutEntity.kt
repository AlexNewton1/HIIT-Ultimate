package com.softwareoverflow.hiit_trainer.data.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "Workout")
class Workout {

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null

    var name: String = ""

    @Ignore
    @Relation(parentColumn = "id", entityColumn = "workoutId")
    var sets: List<WorkoutSet> = emptyList()
}