package com.softwareoverflow.hiit_trainer.data.mapper

import com.softwareoverflow.hiit_trainer.data.Workout
import com.softwareoverflow.hiit_trainer.data.WorkoutSet
import com.softwareoverflow.hiit_trainer.data.entity.ExerciseTypeEntity
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO

fun Workout.toDTO() : WorkoutDTO {

    return WorkoutDTO(
        workout.id,
        workout.name,
        workoutSets.toDTO()
    )
}

fun List<WorkoutSet>.toDTO() : MutableList<WorkoutSetDTO> {
    val dtoList = ArrayList<WorkoutSetDTO>()
    forEach { dtoList.add(it.toDTO()) }
    return dtoList
}

fun WorkoutSet.toDTO(): WorkoutSetDTO {

    return WorkoutSetDTO(
        workoutSet.id,
        exerciseType.toDTO(),
        workoutSet.workTime,
        workoutSet.restTime,
        workoutSet.numReps,
        workoutSet.recoverTime
    )

}

fun ExerciseTypeEntity.toDTO(): ExerciseTypeDTO {

    return ExerciseTypeDTO(
        id,
        name,
        icon,
        color
    )
}

fun ExerciseTypeDTO.toEntity() : ExerciseTypeEntity {
    return ExerciseTypeEntity(
        id,
        name,
        iconName,
        color
    )
}