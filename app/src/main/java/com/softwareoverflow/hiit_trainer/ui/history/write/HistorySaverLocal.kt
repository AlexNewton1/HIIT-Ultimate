package com.softwareoverflow.hiit_trainer.ui.history.write

import com.softwareoverflow.hiit_trainer.repository.dto.history.WorkoutHistoryDTO
import com.softwareoverflow.hiit_trainer.ui.workout.WorkoutSection
import java.time.LocalDate
import javax.inject.Inject

class HistorySaverLocal @Inject constructor (private val historyWriter: IHistoryWriter) : IHistorySaver {

    var history : WorkoutHistoryDTO? = null

    override fun addHistory(seconds: Int, section: WorkoutSection, name: String) {
        val dateNow = LocalDate.now()

        if(history == null) {
            createDTO(section, name, dateNow)
        }

        history?.apply {
            if( this.date != dateNow || section != this.type || name != this.name) {
                write()

                createDTO(section, name, dateNow)
            }

            when (section) {
                WorkoutSection.WORK -> this.time += seconds
                WorkoutSection.REST -> this.time += seconds
                WorkoutSection.RECOVER -> this.time += seconds
                else -> { /* Do Nothing */ }
            }
        }
    }

    private fun createDTO(section: WorkoutSection, name: String, date: LocalDate){
        history = WorkoutHistoryDTO(1, name, section, date)
    }

    override fun write() {
        history?.let {
            historyWriter.writeHistory(it.copy())

            history = null
        }
    }
}