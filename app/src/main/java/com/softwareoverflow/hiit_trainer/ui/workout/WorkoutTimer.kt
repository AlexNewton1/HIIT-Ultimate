package com.softwareoverflow.hiit_trainer.ui.workout

import android.content.Context
import android.os.CountDownTimer
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO
import timber.log.Timber

class WorkoutTimer(
    context: Context,
    workoutDurationSeconds: Int,
    _workoutSets: List<WorkoutSetDTO>,
    private val observer: IWorkoutObserver
) {

    private lateinit var timer: CountDownTimer
    private var millisecondsRemaining: Long = workoutDurationSeconds * 1000L

    private val _soundManager: WorkoutMediaManager = WorkoutMediaManager(context)

    private var isRunning = false
    private var isPaused: Boolean = false

    private var workoutSets = _workoutSets.iterator()
    private var currentSection: WorkoutSection
    private var currentSet = workoutSets.next()
    private var currentSetIndex = 0
    private var currentRep = 1
    private var millisRemainingInSection = getCurrentSetWorkTime()

    init {
        createTimer(millisecondsRemaining)

        val hasPrepSet =
            currentSet.exerciseTypeDTO!!.name == context.getString(R.string.get_ready) &&
                    currentSet.exerciseTypeDTO!!.id == null
        currentSection =
            if (hasPrepSet) WorkoutSection.PREPARE
            else WorkoutSection.WORK

        observer.onWorkoutSectionChange(currentSection, currentSet, currentRep)
    }

    /** Skips the current section of the workout **/
    fun skip() {
        millisecondsRemaining -= millisRemainingInSection
        millisecondsRemaining =
            ((millisecondsRemaining + 999) / 1000) * 1000 // Round up to the nearest second (in millis) to prevent the frequent polling of the timer getting out of sync

        Timber.d("Timer: WorkoutTimer: OnSkip: ${millisecondsRemaining / 1000}")

        if (millisecondsRemaining <= 0) {
            _soundManager.playSound(WorkoutMediaManager.WorkoutSound.SOUND_WORKOUT_COMPLETE)
            observer.onFinish()
            return
        }

        val currentSound = _soundManager.playSound
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
            millisecondsRemaining += 1000 // The display lags behind by 1s
            millisRemainingInSection += 1000 // The display lags behind by 1s
            createTimer(millisecondsRemaining)

            isRunning = true
            timer.start()
        }
    }

    /** Allows muting / un-muting **/
    fun toggleSound(playSound: Boolean) {
        _soundManager.toggleSound(playSound)
    }

    private fun createTimer(millis: Long) {
        val tickInterval = 1000L

        timer = object : CountDownTimer(millis, tickInterval) {
            override fun onFinish() {
                _soundManager.playSound(WorkoutMediaManager.WorkoutSound.SOUND_WORKOUT_COMPLETE)
                timer.cancel()
                observer.onFinish()
            }

            override fun onTick(millisUntilFinished: Long) {
                Timber.d("Timer: Timer: ${millisecondsRemaining / 1000}")

                observer.onTimerTick(
                    (millisecondsRemaining / 1000).toInt(),
                    (millisRemainingInSection / 1000).toInt()
                )

                if (currentSection == WorkoutSection.WORK)
                    when (millisRemainingInSection / 1000) {
                        15L -> _soundManager.playSound(WorkoutMediaManager.WorkoutSound.SOUND_VOCAL_15)
                        10L -> _soundManager.playSound(WorkoutMediaManager.WorkoutSound.SOUND_VOCAL_10)
                        5L -> _soundManager.playSound(WorkoutMediaManager.WorkoutSound.SOUND_VOCAL_5)
                    }

                if (millisRemainingInSection in 1..3000L)
                    _soundManager.playSound(WorkoutMediaManager.WorkoutSound.SOUND_321)

                if (millisRemainingInSection <= 0 && millisUntilFinished > tickInterval)
                    startNextWorkoutSection()

                millisRemainingInSection -= tickInterval
                millisecondsRemaining -= tickInterval
            }
        }
    }

    private fun startNextWorkoutSection() {
        if (currentSection == WorkoutSection.PREPARE) {
            // Begin the workout
            currentSection = WorkoutSection.WORK

            if (workoutSets.hasNext())
                currentSet = workoutSets.next()
            millisRemainingInSection = getCurrentSetWorkTime()
        } else if (currentRep == currentSet.numReps) {
            if (currentSection == WorkoutSection.RECOVER) {
                // Start the new workout set
                currentRep = 1
                currentSection = WorkoutSection.WORK

                if (workoutSets.hasNext())
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
        observer.onTimerTick(
            millisecondsRemaining.toInt() / 1000,
            millisRemainingInSection.toInt() / 1000
        )
    }

    fun start() {
        isRunning = true
        timer.start()
    }

    fun cancel() {
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