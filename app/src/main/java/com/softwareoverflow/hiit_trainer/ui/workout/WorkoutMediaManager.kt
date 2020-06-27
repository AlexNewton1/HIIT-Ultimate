package com.softwareoverflow.hiit_trainer.ui.workout

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.preference.PreferenceManager
import com.softwareoverflow.hiit_trainer.R
import timber.log.Timber

class WorkoutMediaManager(context: Context) {

    private var _soundPool: SoundPool

    private val playVocal15: Boolean
    private val playVocal10: Boolean
    private val playVocal5: Boolean

    var playSound = true
    private set

    private var isReady = false

    private val soundVocal15: Int
    private val soundVocal10: Int
    private val soundVocal5: Int
    private val sound321: Int
    private val soundWorkStart: Int
    private val soundWorkoutComplete: Int
    private val soundRestStart: Int
    private val soundRecoverStart: Int

    enum class WorkoutSound {
        SOUND_VOCAL_15, SOUND_VOCAL_10, SOUND_VOCAL_5, SOUND_321, SOUND_WORK_START, SOUND_REST_START, SOUND_RECOVER_START, SOUND_WORKOUT_COMPLETE
    }

    init {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        val vocalSounds = prefs.getStringSet(
            context.resources.getString(R.string.key_final_seconds_vocal),
            setOf("10", "5", "15")
        )!!

        playVocal15 = vocalSounds.contains("15")
        playVocal10 = vocalSounds.contains("10")
        playVocal5 = vocalSounds.contains("5")

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
                    if (status == 0)
                        isReady = true
                    else
                        Timber.w("Problem setting up SoundPool. Status code $status")
                }
            }

        soundVocal15 = _soundPool.load(context, R.raw.vocal_15, 1)
        soundVocal10 = _soundPool.load(context, R.raw.vocal_10, 1)
        soundVocal5 = _soundPool.load(context, R.raw.vocal_5, 1)
        soundWorkoutComplete = _soundPool.load(context, R.raw.fanfare_workout_complete, 1)
        soundWorkStart = _soundPool.load(context, R.raw.ding_work, 1)
        sound321 = _soundPool.load(context, R.raw.bong_321_work, 1)
        soundRestStart = _soundPool.load(context, R.raw.ding_rest, 1)
        soundRecoverStart = _soundPool.load(context, R.raw.ding_rest, 1)
    }

    fun toggleSound(soundOn: Boolean) {
        playSound = soundOn
    }

    /**
     * Helper function to allow mapping of [WorkoutSection] to [WorkoutSound]
     */
    fun playSound(workoutSection: WorkoutSection) {
        when (workoutSection) {
            WorkoutSection.WORK -> playSound(WorkoutSound.SOUND_WORK_START)
            WorkoutSection.REST -> playSound(WorkoutSound.SOUND_REST_START)
            WorkoutSection.RECOVER -> playSound(WorkoutSound.SOUND_RECOVER_START)
            WorkoutSection.PREPARE -> { /* Do Nothing */
            }
        }
    }

    fun playSound(sound: WorkoutSound) {
        if (!isReady || !playSound)
            return

        when (sound) {
            WorkoutSound.SOUND_REST_START ->
                _soundPool.play(soundRestStart, 1f, 1f, 9, 0, 1f)
            WorkoutSound.SOUND_RECOVER_START ->
                _soundPool.play(soundRecoverStart, 1f, 1f, 9, 0, 1f)
            WorkoutSound.SOUND_321 ->
                _soundPool.play(sound321, 0.7f, 0.7f, 9, 1, 2f)
            WorkoutSound.SOUND_WORK_START ->
                _soundPool.play(soundWorkStart, 1f, 1f, 10, 0, 1f)
            WorkoutSound.SOUND_WORKOUT_COMPLETE ->
                _soundPool.play(soundWorkoutComplete, 1f, 1f, 10, 0, 1f)
            WorkoutSound.SOUND_VOCAL_15 ->
                if(playVocal15) _soundPool.play(soundVocal15, 1f, 1f, 10, 0, 1f)
            WorkoutSound.SOUND_VOCAL_10 ->
                if(playVocal10) _soundPool.play(soundVocal10, 1f, 1f, 10, 0, 1f)
            WorkoutSound.SOUND_VOCAL_5 ->
                if(playVocal5) _soundPool.play(soundVocal5, 1f, 1f, 10, 0, 1f)
        }
    }

    fun onDestroy() {
         _soundPool.release()
    }
}