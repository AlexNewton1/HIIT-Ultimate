package com.softwareoverflow.hiit_trainer.repository.dto

data class WorkoutDTO (
    var id: Long? = null,
    var name: String = "",
    var workoutSets: MutableList<WorkoutSetDTO> = ArrayList()
)