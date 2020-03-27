package com.softwareoverflow.hiit_trainer

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO
import com.softwareoverflow.hiit_trainer.ui.getFormattedDuration
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WorkoutDtoTest {

    @Test
    fun getWorkoutDuration() {
        // Arrange
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val exerciseType = ExerciseTypeDTO(null, "Test Exercise Type", "test_icon", "#666666")

        val set1 = WorkoutSetDTO(null, exerciseType, 5,5, 6, 120) // 02:55
        val set2 = WorkoutSetDTO(null, exerciseType, 7, 3, 2, 0) // 00:17
        val set3 = WorkoutSetDTO(null, exerciseType, 0, 0, 0, 30) // 00:30
        val set4 = WorkoutSetDTO(null, exerciseType, 30, 30, 1, 1000000) // 00:30

        val dto = WorkoutDTO()
        dto.workoutSets.addAll(0, arrayListOf(set1, set2, set3, set4))

        // Act
        val durationString = dto.getFormattedDuration(context.resources)

        // Assert
        assertEquals("04:12", durationString)
    }

}