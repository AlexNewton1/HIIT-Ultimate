package com.softwareoverflow.hiit_trainer.ui.workout

import android.content.Context
import android.os.CountDownTimer
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.ui.getDuration

class WorkoutTimer(context: Context, workout: WorkoutDTO, private val observer: IWorkoutObserver) {

    private lateinit var timer: CountDownTimer
    private var millisecondsRemaining: Long = workout.getDuration() * 1000L

    private val _soundManager: WorkoutMediaManager = WorkoutMediaManager(context)

    private var isRunning = false
    private var isPaused: Boolean = false

    private var workoutSets = workout.workoutSets.iterator()
    private var currentSection = WorkoutSection.PREPARE
    private var currentSet = workoutSets.next()
    private var currentSetIndex = 0
    private var currentRep = 1
    private var millisRemainingInSection = getCurrentSetWorkTime()

    init {
        createTimer(millisecondsRemaining)

        isRunning = true
        timer.start()

        observer.onWorkoutSectionChange(currentSection, currentSet, currentRep)
    }

    /** Skips the current section of the workout **/
    fun skip() {
        millisecondsRemaining -= millisRemainingInSection
        millisecondsRemaining =
            ((millisecondsRemaining + 999) / 1000) * 1000 // Round up to the nearest second (in millis) to prevent the frequent polling of the timer getting out of sync

        if(millisecondsRemaining <= 0 ){
            _soundManager.playSound(WorkoutMediaManager.WorkoutSound.SOUND_WORKOUT_COMPLETE)
            observer.onFinish()
            return
        }

        val currentSound = _soundManager.isMuted()
        _soundManager.toggleSound(false) // Mute all noises when skipping through
        startNextWorkoutSection()
        _soundManager.toggleSound(currentSound)

        // Update any observers with the new values
        observer.onTimerTick(
            (millisecondsRemaining / 1000).toInt(),
            (millisRemainingInSection / 1000).toInt()
        )

        // Cancel and recreate the timer
        isRunning = false
        timer.cancel()

        if (!isPaused) {
            createTimer(millisecondsRemaining)
            isRunning = true
            timer.start()
        }
    }

    /** Allows the pausing / resuming of the timer**/
    fun togglePause(isPaused: Boolean) {
        this.isPaused = isPaused

        if (isPaused) {
            isRunning = false
            timer.cancel()
        } else if (!isRunning) {
            createTimer(millisecondsRemaining)

            isRunning = true
            timer.start()
        }
    }

    /** Allows muting / un-muting **/
    fun toggleSound(playSound: Boolean){
        _soundManager.toggleSound(playSound)
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

                    if (millisRemainingInSection <= 3000L && currentSection != WorkoutSection.WORK ) {
                        _soundManager.playSound(WorkoutMediaManager.WorkoutSound.SOUND_321)
                    }
                }

                millisRemainingInSection -= tickInterval
                millisecondsRemaining -= tickInterval

                if (millisRemainingInSection <= 0 && millisUntilFinished > tickInterval)
                    startNextWorkoutSection()
            }
        }
    }

    private fun startNextWorkoutSection() {
        if(currentSection == WorkoutSection.PREPARE){
            // Begin the workout
            currentSection = WorkoutSection.WORK

            if(workoutSets.hasNext())
                currentSet = workoutSets.next()
            millisRemainingInSection = getCurrentSetWorkTime()
        }
        else if (currentRep == currentSet.numReps) {
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

        _soundManager.playSound(currentSection)
        observer.onWorkoutSectionChange(currentSection, currentSet, currentRep)
    }

    fun cancel(){
        _soundManager.onDestroy()
        timer.cancel()
    }

    private fun getCurrentSetWorkTime() = currentSet.workTime!! * 1000L
    private fun getCurrentSetRestTime() = currentSet.restTime!! * 1000L
    private fun getCurrentSetRecoverTime() = currentSet.recoverTime!! * 1000L
}

enum class WorkoutSection {
    PREPARE, WORK, REST, RECOVER
}