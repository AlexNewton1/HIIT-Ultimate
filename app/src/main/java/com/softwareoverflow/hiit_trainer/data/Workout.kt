package com.softwareoverflow.hiit_trainer.data

import androidx.room.Embedded
import androidx.room.Relation
import com.softwareoverflow.hiit_trainer.data.entity.WorkoutEntity
import com.softwareoverflow.hiit_trainer.data.entity.WorkoutSetEntity

class Workout(
    @Embedded var workout: WorkoutEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "workoutId",
        entity = WorkoutSetEntity::class
    ) var workoutSets: List<WorkoutSet>
)