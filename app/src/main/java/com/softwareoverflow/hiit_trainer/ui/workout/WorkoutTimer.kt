package com.softwareoverflow.hiit_trainer.ui.workout

import android.os.CountDownTimer
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.ui.getDuration

// TODO retrofit a preparation timer in....
class WorkoutTimer(workout: WorkoutDTO, private val observer: IWorkoutObserver) {

    private lateinit var timer: CountDownTimer
    private var millisecondsRemaining: Long = workout.getDuration() * 1000L
    private var isPaused: Boolean = false

    private var workoutSets = workout.workoutSets.iterator()
    private var currentSection = WorkoutSection.WORK
    private var currentSet = workoutSets.next()
    private var currentSetIndex = 0
    private var currentRep = 1
    private var millisRemainingInSection = getCurrentSetWorkTime()

    init {
        createTimer(millisecondsRemaining)
        timer.start()

        observer.onWorkoutSectionChange(currentSection, currentSet, currentRep)
    }

    /** Skips the current section of the workout **/
    fun skip() {
        millisecondsRemaining -= millisRemainingInSection
        millisecondsRemaining =
            ((millisecondsRemaining + 999) / 1000) * 1000 // Round up to the nearest second (in millis) to prevent the frequent polling of the timer getting out of sync

        if (workoutSets.hasNext())
            startNextWorkoutSection()

        // Cancel and recreate the timer
        timer.cancel()

        if (!isPaused) {
            createTimer(millisecondsRemaining)
            timer.start()
        }
    }

    /** Allows the pausing / resuming of the timer**/
    fun togglePause(isPaused: Boolean) {
        this.isPaused = isPaused

        if (isPaused) {
            timer.cancel()
        } else {
            createTimer(millisecondsRemaining)
            timer.start()
        }
    }

    private fun createTimer(millis: Long) {
        val tickInterval = 100L

        timer = object : CountDownTimer(millis, tickInterval) {
            override fun onFinish() {
                observer.onFinish()
            }

            override fun onTick(millisUntilFinished: Long) {
                if (millisecondsRemaining % 1000 == 0L) {
                    observer.onTimerTick(
                        (millisecondsRemaining / 1000).toInt(),
                        (millisRemainingInSection / 1000).toInt()
                    )
                }

                millisRemainingInSection -= tickInterval
                millisecondsRemaining -= tickInterval

                if (millisRemainingInSection <= 0)
                    startNextWorkoutSection()
            }
        }
    }

    private fun startNextWorkoutSection() {
        if (currentRep == currentSet.numReps) {
            if (currentSection == WorkoutSection.RECOVER) {
                // Start the new workout set
                currentRep = 1
                currentSection = WorkoutSection.WORK

                if(workoutSets.hasNext())
                    currentSet = workoutSets.next()

                millisRemainingInSection = getCurrentSetWorkTime()
                currentSetIndex++

            } else {
                // Start the recovery period
                currentSection = WorkoutSection.RECOVER
                millisRemainingInSection = getCurrentSetRecoverTime()
            }
        } else {
            if (currentSection == WorkoutSection.WORK) {
                // Start the rest period
                currentSection = WorkoutSection.REST
                millisRemainingInSection = getCurrentSetRestTime()

            } else if (currentSection == WorkoutSection.REST) {
                // Start the next rep
                currentRep++
                currentSection = WorkoutSection.WORK
                millisRemainingInSection = getCurrentSetWorkTime()
            }
        }

        observer.onWorkoutSectionChange(currentSection, currentSet, currentRep)
    }

    fun cancel(){
        timer.cancel()
    }

    private fun getCurrentSetWorkTime() = currentSet.workTime!! * 1000L
    private fun getCurrentSetRestTime() = currentSet.restTime!! * 1000L
    private fun getCurrentSetRecoverTime() = currentSet.recoverTime!! * 1000L
}

enum class WorkoutSection {
    WORK, REST, RECOVER
}