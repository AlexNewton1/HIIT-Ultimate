package com.softwareoverflow.hiit_trainer.ui.workout

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.softwareoverflow.hiit_trainer.R
import timber.log.Timber

class WorkoutMediaManager(context: Context) {

    private var _soundPool: SoundPool

    private var playSound = true
    private var isReady = false

    private val sound321: Int // P2
    private val soundWorkStart: Int // P3
    private val soundWorkoutComplete: Int // P4
    private val soundRestStart: Int // P1
    private val soundRecoverStart: Int // P1

    enum class  WorkoutSound {
        SOUND_321, SOUND_WORK_START, SOUND_REST_START, SOUND_RECOVER_START, SOUND_WORKOUT_COMPLETE
    }

    init {
        val audioAttrs = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()

        _soundPool = SoundPool.Builder()
            .setMaxStreams(2)
            .setAudioAttributes(audioAttrs)
            .build()
            .apply {
                setOnLoadCompleteListener { _, _, status ->
                    if(status == 0)
                        isReady = true
                    else
                        Timber.w("Problem setting up SoundPool. Status code $status")
                }
            }

        soundWorkoutComplete = _soundPool.load(context, R.raw.fanfare_workout_complete, 4)
        soundWorkStart = _soundPool.load(context, R.raw.ding_work, 3)
        sound321 = _soundPool.load(context, R.raw.bong_321_work, 2)
        soundRestStart = _soundPool.load(context, R.raw.ding_rest, 1)
        soundRecoverStart = _soundPool.load(context, R.raw.ding_rest, 1)
    }

    fun toggleSound(soundOn: Boolean){
        playSound = soundOn
    }

    /**
     * Helper function to allow mapping of [WorkoutSection] to [WorkoutSound]
     */
    fun playSound(workoutSection: WorkoutSection){
        when(workoutSection){
            WorkoutSection.WORK -> playSound(WorkoutSound.SOUND_WORK_START)
            WorkoutSection.REST -> playSound(WorkoutSound.SOUND_REST_START)
            WorkoutSection.RECOVER -> playSound(WorkoutSound.SOUND_RECOVER_START)
            WorkoutSection.PREPARE -> { /* Do Nothing */ }
        }
    }

    fun playSound(sound: WorkoutSound){
        if(!isReady || !playSound)
            return

        when (sound) {
            WorkoutSound.SOUND_REST_START ->
                _soundPool.play(soundRestStart, 1f, 1f, 1, 0, 1f)
            WorkoutSound.SOUND_RECOVER_START ->
                _soundPool.play(soundRecoverStart, 1f, 1f, 1, 0, 1f)
            WorkoutSound.SOUND_321 ->
                _soundPool.play(sound321, 0.7f, 0.7f, 2, 1, 2f)
            WorkoutSound.SOUND_WORK_START ->
                _soundPool.play(soundWorkStart, 1f, 1f, 3, 0, 1f)
            WorkoutSound.SOUND_WORKOUT_COMPLETE ->
                _soundPool.play(soundWorkoutComplete, 1f, 1f, 4, 0, 1f)
        }
    }

    fun isMuted() = !playSound

    fun onDestroy() {
        _soundPool.release()
    }
}