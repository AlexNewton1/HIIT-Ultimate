package com.softwareoverflow.hiit_trainer.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "WorkoutSet",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseTypeEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseTypeId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE // TODO see todo below and work out FK constraints....
        )
    ]
)
class WorkoutSetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    var workoutId: Long? = null,
    var exerciseTypeId: Long? = null, // TODO work out if it's worth linking this to Exercise Type table, or just simply repeat the values? Seems dirty but makes handling exercise types completely separate
    var workTime: Int,
    var restTime: Int,
    var numReps: Int,
    var recoverTime: Int
)