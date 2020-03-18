package com.softwareoverflow.hiit_trainer.repository.dto

class WorkoutSetDTO(
    val id: Long?,
    var exerciseTypeDTO: ExerciseTypeDTO?,
    var workTime: Int,
    var restTime: Int,
    var numReps: Int,
    var recoverTime: Int
)