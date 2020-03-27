package com.softwareoverflow.hiit_trainer.repository.dto

data class WorkoutDTO (
    val id: Long? = null,
    var name: String = "",
    var workoutSets: MutableList<WorkoutSetDTO> = ArrayList()
)