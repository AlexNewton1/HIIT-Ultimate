package com.softwareoverflow.hiit_trainer.repository.dto

data class WorkoutSetDTO(
    val id: Long? = null,
    var exerciseTypeDTO: ExerciseTypeDTO? = null,
    var workTime: Int? = 25,
    var restTime: Int? = 5,
    var numReps: Int? = 6,
    var recoverTime: Int? = 120
)