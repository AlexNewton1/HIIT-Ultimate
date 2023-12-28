package com.softwareoverflow.hiit_trainer.data.history

import com.softwareoverflow.hiit_trainer.repository.dto.history.WorkoutHistoryDTO
import com.softwareoverflow.hiit_trainer.ui.workout.WorkoutSection
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

class WorkoutHistoryRoomDb @Inject constructor(private val historyDao: WorkoutHistoryDao) :
    IWorkoutHistoryRepository {

    override suspend fun createOrUpdate(workoutHistory: WorkoutHistoryDTO) {
        if (workoutHistory.type == WorkoutSection.PREPARE) return

        Timber.d("Before name change: ${workoutHistory.name}")
        // We need to update the name before we search for the matching entry in the database
        workoutHistory.name =
            when (workoutHistory.type) {
                WorkoutSection.RECOVER -> WorkoutSection.RECOVER.name
                WorkoutSection.REST -> WorkoutSection.REST.name
                else -> workoutHistory.name
            }
        Timber.d("Post name change: ${workoutHistory.name}")

        val dateString = HistoryConverters.historyDateFormat.format(workoutHistory.date)

        val existingData =
            historyDao.getExistingEntry(workoutHistory.name, workoutHistory.type.name, dateString)

        if (existingData != null) {
            // If we have a match we need to update the time
            workoutHistory.time += existingData.time

            Timber.d("Found existing entry: ${existingData.time}s for section ${existingData.type} named ${existingData.name}\nValue updated to ${workoutHistory.time}")
        }

        Timber.d("Writing to database: ${workoutHistory.time}s for section ${workoutHistory.type} named ${workoutHistory.name}")
        historyDao.createOrUpdate(workoutHistory.toEntity())
        Timber.d("\n\n")
    }

    override suspend fun getAllHistory(): List<WorkoutHistoryDTO> {
        return historyDao.getAllHistory().map { it.toDto() }
    }

    override suspend fun getHistoryBetweenDates(
        from: LocalDate, to: LocalDate
    ): List<WorkoutHistoryDTO> {
        val fromString = HistoryConverters.historyDateFormat.format(from)
        val toString = HistoryConverters.historyDateFormat.format(to)
        return historyDao.getHistoryBetweenDates(fromString, toString).map { item -> item.toDto() }
    }
}