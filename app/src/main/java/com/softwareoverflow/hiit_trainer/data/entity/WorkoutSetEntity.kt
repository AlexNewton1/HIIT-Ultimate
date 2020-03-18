package com.softwareoverflow.hiit_trainer.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "WorkoutSet",
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseType::class,
            parentColumns = ["id"],
            childColumns = ["exerciseTypeId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
class WorkoutSet {

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null

    var workoutId: Int? = null

    var exerciseTypeId: Int? = null

    var workTime: Int = 0
    var restTime: Int = 0
    var numReps: Int = 0
}