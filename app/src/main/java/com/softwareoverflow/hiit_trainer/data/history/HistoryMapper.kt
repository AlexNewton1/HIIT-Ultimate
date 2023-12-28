package com.softwareoverflow.hiit_trainer.data.history

import com.softwareoverflow.hiit_trainer.repository.dto.history.WorkoutHistoryDTO

fun List<WorkoutHistoryDTO>.toEntity() : List<WorkoutHistoryEntity> {
    return this.map { it.toEntity() }
}

fun WorkoutHistoryDTO.toEntity(): WorkoutHistoryEntity {
    return WorkoutHistoryEntity(
        this.time, this.name, this.type, this.date
    )
}

fun List<WorkoutHistoryEntity>.toDto() : List<WorkoutHistoryDTO> {
    return this.map { it.toDto() }
}

fun WorkoutHistoryEntity.toDto(): WorkoutHistoryDTO {
    return WorkoutHistoryDTO(this.time, this.name, this.type, this.date)
}