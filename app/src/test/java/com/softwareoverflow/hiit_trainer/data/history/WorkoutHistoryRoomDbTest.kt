package com.softwareoverflow.hiit_trainer.data.history

import com.softwareoverflow.hiit_trainer.repository.dto.history.WorkoutHistoryDTO
import com.softwareoverflow.hiit_trainer.ui.workout.WorkoutSection
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.time.LocalDate

@RunWith(MockitoJUnitRunner::class)
class WorkoutHistoryRoomDbTest {

    @Test
    fun createOrUpdate_New() {
        val mockDao = mock<WorkoutHistoryDao> {
            onBlocking {
                getExistingEntry(
                    any(), any(), any()
                )
            } doReturn null
        }

        val classUnderTest = WorkoutHistoryRoomDb(mockDao)

        runTest {
            classUnderTest.createOrUpdate(
                WorkoutHistoryDTO(
                    5, "Testing", WorkoutSection.WORK, LocalDate.of(2023, 7, 25)
                )
            )

            val expectedHistory = WorkoutHistoryEntity(
                5, "Testing", WorkoutSection.WORK, LocalDate.of(2023, 7, 25)
            )
            verify(mockDao).createOrUpdate(expectedHistory)
        }
    }

    @Test
    fun createOrUpdate_RenamesRest() {
        val mockDao = mock<WorkoutHistoryDao> {
            onBlocking {
                getExistingEntry(
                    any(), any(), any()
                )
            } doReturn null
        }

        val classUnderTest = WorkoutHistoryRoomDb(mockDao)

        runTest {
            classUnderTest.createOrUpdate(
                WorkoutHistoryDTO(
                    5, "Testing", WorkoutSection.REST, LocalDate.of(2023, 7, 25)
                )
            )

            val expectedHistory = WorkoutHistoryEntity(
                5, WorkoutSection.REST.name, WorkoutSection.REST, LocalDate.of(2023, 7, 25)
            )
            verify(mockDao).createOrUpdate(expectedHistory)
        }
    }

    @Test
    fun createOrUpdate_RenamesRecover() {
        val mockDao = mock<WorkoutHistoryDao> {
            onBlocking {
                getExistingEntry(
                    any(), any(), any()
                )
            } doReturn null
        }

        val classUnderTest = WorkoutHistoryRoomDb(mockDao)

        runTest {
            classUnderTest.createOrUpdate(
                WorkoutHistoryDTO(
                    5, "Testing", WorkoutSection.RECOVER, LocalDate.of(2023, 7, 25)
                )
            )

            val expectedHistory = WorkoutHistoryEntity(
                5, WorkoutSection.RECOVER.name, WorkoutSection.RECOVER, LocalDate.of(2023, 7, 25)
            )
            verify(mockDao).createOrUpdate(expectedHistory)
        }
    }

    @Test
    fun createOrUpdate_Update() {
        val mockDao = mock<WorkoutHistoryDao> {
            onBlocking { getExistingEntry(any(), any(), any()) } doReturn WorkoutHistoryEntity(
                3, "Burpees", WorkoutSection.WORK, LocalDate.of(2023, 7, 25)
            )
        }

        val classUnderTest = WorkoutHistoryRoomDb(mockDao)

        runTest {
            classUnderTest.createOrUpdate(
                WorkoutHistoryDTO(
                    1, "Burpees", WorkoutSection.WORK, LocalDate.of(2023, 7, 25)
                )
            )

            val expectedHistory =
                WorkoutHistoryEntity(4, "Burpees", WorkoutSection.WORK, LocalDate.of(2023, 7, 25))
            verify(mockDao).createOrUpdate(expectedHistory)
        }
    }

    @Test
    fun getAllHistory() = runTest {
        val entityList = listOf(
            WorkoutHistoryEntity(
                3, "REST", WorkoutSection.REST, LocalDate.of(2023, 7, 25)
            ),
            WorkoutHistoryEntity(
                1, "RECOVER", WorkoutSection.RECOVER, LocalDate.of(2023, 6, 18)
            ),
            WorkoutHistoryEntity(
                100, "Work Time", WorkoutSection.WORK, LocalDate.of(2022, 9, 1)
            ),
            WorkoutHistoryEntity(
                17202, "More Working Time", WorkoutSection.WORK, LocalDate.of(2022, 8, 11)
            ),
            WorkoutHistoryEntity(
                10460, "REST", WorkoutSection.REST, LocalDate.of(2022, 8, 3)
            ),
        )

        val mockDao = mock<WorkoutHistoryDao> {
            onBlocking { getAllHistory() } doReturn entityList
        }

        val classUnderTest = WorkoutHistoryRoomDb(mockDao)


        val expected = entityList.toDto()

        val result = classUnderTest.getAllHistory()

        assertEquals(expected, result)
    }

    @Test
    fun getHistoryBetweenDates() = runTest {
        // All we can test with this is that it calls toDto correctly
        val entityList = listOf(
            WorkoutHistoryEntity(
                3, "ABCD", WorkoutSection.WORK, LocalDate.of(2023, 7, 25)
            ),
            WorkoutHistoryEntity(
                1, "REST", WorkoutSection.REST, LocalDate.of(2023, 6, 18)
            ),
            WorkoutHistoryEntity(
                100, "RECOVER", WorkoutSection.RECOVER, LocalDate.of(2022, 9, 1)
            ),
            WorkoutHistoryEntity(
                17202, "Some working name", WorkoutSection.WORK, LocalDate.of(2022, 8, 11)
            ),
            WorkoutHistoryEntity(
                10460, "Yet more work", WorkoutSection.WORK, LocalDate.of(2022, 8, 3)
            ),
        )

        val mockDao = mock<WorkoutHistoryDao> {
            onBlocking { getHistoryBetweenDates(any(), any()) } doReturn entityList
        }

        val classUnderTest = WorkoutHistoryRoomDb(mockDao)


        val expected = entityList.toDto()

        val result = classUnderTest.getHistoryBetweenDates(
            LocalDate.of(2023, 12, 1), LocalDate.of(2023, 12, 1)
        )

        assertEquals(expected, result)
    }
}