package com.softwareoverflow.hiit_trainer.data

import androidx.room.Embedded
import androidx.room.Relation
import com.softwareoverflow.hiit_trainer.data.entity.ExerciseTypeEntity
import com.softwareoverflow.hiit_trainer.data.entity.WorkoutSetEntity

data class WorkoutSet(
    @Embedded var workoutSet: WorkoutSetEntity,

    @Relation(
        parentColumn = "exerciseTypeId",
        entityColumn = "id"
    ) var exerciseType: ExerciseTypeEntity
)