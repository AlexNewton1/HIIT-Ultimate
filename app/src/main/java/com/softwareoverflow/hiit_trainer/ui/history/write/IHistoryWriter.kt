package com.softwareoverflow.hiit_trainer.ui.history.write

import com.softwareoverflow.hiit_trainer.repository.dto.history.WorkoutHistoryDTO

interface IHistoryWriter {

    fun writeHistory(obj: WorkoutHistoryDTO)

}