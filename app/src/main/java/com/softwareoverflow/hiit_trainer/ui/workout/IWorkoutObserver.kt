package com.softwareoverflow.hiit_trainer.ui.workout

interface IWorkoutObserver {

    /** Called whenever the timer ticks a full second **/
    fun onTimerTick(workoutSectionMillisRemaining: Long, totalMillisRemaining: Long)

    /** Called when the [WorkoutSection] changed. Provides the new section, the new rep and the new set numbers **/
    fun onWorkoutSectionChange(section: WorkoutSection, currentRep: Int, currentSet: Int)

    /** Called when the workout timer completes **/
    fun onFinish()
}