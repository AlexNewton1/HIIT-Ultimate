package com.softwareoverflow.hiit_trainer.ui.history.write

import com.softwareoverflow.hiit_trainer.repository.dto.history.WorkoutHistoryDTO
import com.softwareoverflow.hiit_trainer.ui.workout.WorkoutSection
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.time.LocalDate


@RunWith(MockitoJUnitRunner::class)
class HistorySaverLocalTest {

    @Test
    fun historySaver_appendsExisting() {
        val mockWriter = mock<IHistoryWriter> {}

        val classUnderTest = HistorySaverLocal(mockWriter)

        classUnderTest.addHistory(3, WorkoutSection.REST, "Exercise Name")
        classUnderTest.addHistory(2, WorkoutSection.REST, "Exercise Name")

        classUnderTest.write()

        verify(mockWriter).writeHistory(argThat {
            this == WorkoutHistoryDTO(6, "Exercise Name", WorkoutSection.REST, LocalDate.now())
        })
    }

    @Test
    fun historySaver_writes_changeSection() {
        val mockWriter = mock<IHistoryWriter> {}

        val classUnderTest = HistorySaverLocal(mockWriter)

        classUnderTest.addHistory(3, WorkoutSection.REST, "Exercise Name")
        classUnderTest.addHistory(2, WorkoutSection.REST, "Exercise Name")

        classUnderTest.addHistory(2, WorkoutSection.WORK, "Exercise Name")

        verify(mockWriter, times(1)).writeHistory(argThat {
            this == WorkoutHistoryDTO(6, "Exercise Name", WorkoutSection.REST, LocalDate.now())
        })
    }


    @Test
    fun historySaver_writes_changeName() {
        val mockWriter = mock<IHistoryWriter> {}

        val classUnderTest = HistorySaverLocal(mockWriter)

        classUnderTest.addHistory(3, WorkoutSection.REST, "Exercise Name")
        classUnderTest.addHistory(2, WorkoutSection.REST, "Exercise Name")

        classUnderTest.addHistory(2, WorkoutSection.WORK, "New Exercise")

        verify(mockWriter, times(1)).writeHistory(any())
        verify(mockWriter).writeHistory(argThat {
            this == WorkoutHistoryDTO(6, "Exercise Name", WorkoutSection.REST, LocalDate.now())
        })
    }
}