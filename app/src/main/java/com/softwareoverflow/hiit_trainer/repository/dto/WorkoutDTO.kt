package com.softwareoverflow.hiit_trainer.repository.dto

class WorkoutDTO (
    val id: Long? = null,
    var name: String = "",
    var workoutSets: MutableList<WorkoutSetDTO> = ArrayList()
)