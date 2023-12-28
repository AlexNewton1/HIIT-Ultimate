package com.softwareoverflow.hiit_trainer.ui.workout

import android.content.Context
import android.text.format.DateUtils
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO
import com.softwareoverflow.hiit_trainer.ui.getDuration
import com.softwareoverflow.hiit_trainer.ui.getFullWorkoutSets
import com.softwareoverflow.hiit_trainer.ui.getWorkoutCompleteExerciseType
import com.softwareoverflow.hiit_trainer.ui.getWorkoutPrepSet
import com.softwareoverflow.hiit_trainer.ui.history.write.IHistorySaver
import com.softwareoverflow.hiit_trainer.ui.utils.SharedPreferencesManager
import com.softwareoverflow.hiit_trainer.ui.workout.media.WorkoutMediaManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val mediaManager: WorkoutMediaManager,
    private val historySaver: IHistorySaver
) : IWorkoutObserver, ViewModel() {


    private val workoutCompleteExerciseType =
        getWorkoutCompleteExerciseType(context)

    private val _workout = MutableStateFlow(WorkoutDTO())

    val workout: StateFlow<WorkoutDTO> get() = _workout

    private lateinit var timer: WorkoutTimer

    private val _fullWorkoutSets by lazy {
        _workout.value.getFullWorkoutSets(context)
    }

    // Start with a default UI state. Not a nice solution but it will do for now.
    private lateinit var _uiState : MutableStateFlow<UiState>
    val uiState: StateFlow<UiState> get() = _uiState

    private val _isFinished = MutableStateFlow(false)
    val isWorkoutFinished: StateFlow<Boolean> get() = _isFinished


    private var isInitialized = false

    init {

    }

    fun initialize(context: Context, workoutDto: WorkoutDTO) {
        if (isInitialized) return

        _workout.value = workoutDto


        var duration = workoutDto.getDuration()
        val allSets = workoutDto.getFullWorkoutSets(context.applicationContext)

        val prepSet = getWorkoutPrepSet(context.applicationContext)
        prepSet?.let {
            duration += it.workTime
        }
        val hasPrepSet = prepSet != null

        _workout.value = _workout.value.copy(
            workoutSets = allSets.toMutableList() // Deep copy the list so we don't end up saving the modified version
        )

        _uiState = MutableStateFlow(
            UiState(
                currentWorkoutSet = allSets.first(),
                upNextExerciseType = if (prepSet != null) allSets[1].exerciseTypeDTO!! else null,
                currentSection = if (hasPrepSet) WorkoutSection.PREPARE else WorkoutSection.WORK,
                sectionTimeRemainingValue = allSets.first().workTime,
                workoutTimeRemainingValue = _workout.value.getDuration(),
                currentRepValue = 1,

                isPaused = false,
                isSoundOn = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
                    .getBoolean(SharedPreferencesManager.playWorkoutSounds, true)
            )
        )

        timer = WorkoutTimer(
            context.applicationContext, duration, allSets, mediaManager, historySaver, this
        )
        timer.start()


        isInitialized = true
    }


    private fun cancel() {
        timer.cancel()
    }

    override fun onCleared() {
        cancel()
        super.onCleared()
    }

    data class UiState(
        val currentWorkoutSet: WorkoutSetDTO,
        val upNextExerciseType: ExerciseTypeDTO?,
        val currentSection: WorkoutSection = WorkoutSection.PREPARE,
        val sectionTimeRemainingValue: Int = 0,
        val workoutTimeRemainingValue: Int = 0,
        val currentRepValue: Int = 0,

        val isPaused: Boolean = false,
        val isSoundOn: Boolean = true
    ) {
        val currentExerciseType: ExerciseTypeDTO = currentWorkoutSet.exerciseTypeDTO!!

        val sectionTimeRemaining: String = DateUtils.formatElapsedTime(
            sectionTimeRemainingValue.toLong()
        )

        val workoutTimeRemaining: String = DateUtils.formatElapsedTime(
            workoutTimeRemainingValue.toLong()
        )

        val currentRep: String = "${currentRepValue}/${currentWorkoutSet.numReps}"
    }

    // region user controlled buttons
    fun toggleSound() {
        _uiState.value = _uiState.value.copy(
            isSoundOn = !_uiState.value.isSoundOn
        )
        timer.toggleSound(_uiState.value.isSoundOn)
    }

    fun togglePause() {
        _uiState.value = _uiState.value.copy(
            isPaused = !_uiState.value.isPaused
        )
        timer.togglePause(_uiState.value.isPaused)
    }

    fun skipSection() {
        timer.skip()
    }
    //endregion

    //region IWorkoutObserver Overrides
    override fun onTimerTick(workoutRemaining: Int, workoutSectionRemaining: Int) {
        // Show the upcoming exercise type

        val currentSet = _uiState.value.currentWorkoutSet
        val currentSection = _uiState.value.currentSection


        val currentIndex = _fullWorkoutSets.indexOf(currentSet)
        var nextExerciseType =
            if (currentIndex < _fullWorkoutSets.size - 1) _fullWorkoutSets[currentIndex + 1].exerciseTypeDTO else null
        var showNextExerciseType = false

        if ((currentSection == WorkoutSection.RECOVER || currentSection == WorkoutSection.PREPARE)) {
            if (_uiState.value.sectionTimeRemainingValue <= 10) {
                showNextExerciseType = true
            }
        } else if ((currentIndex == _fullWorkoutSets.size - 1) && uiState.value.currentRepValue == currentSet.numReps) {
            showNextExerciseType = true
            nextExerciseType = workoutCompleteExerciseType
        }

        _uiState.value = _uiState.value.copy(
            sectionTimeRemainingValue = workoutSectionRemaining,
            workoutTimeRemainingValue = workoutRemaining,
            upNextExerciseType = if (showNextExerciseType) nextExerciseType else null
        )
    }

    override fun onWorkoutSectionChange(
        section: WorkoutSection, currentSet: WorkoutSetDTO, currentRep: Int
    ) {

        var sectionUiState = _uiState.value.currentSection
        var workoutSetUiState = _uiState.value.currentWorkoutSet
        var upNextExerciseTypeUiState = _uiState.value.upNextExerciseType
        var repUiState = _uiState.value.currentRepValue

        if (sectionUiState != section) sectionUiState = section

        if (workoutSetUiState != currentSet) {
            workoutSetUiState = currentSet

            // Reset the up next icon as the set has just changed
            upNextExerciseTypeUiState = null
        }

        if (repUiState != currentRep) repUiState = currentRep

        // Show the up next icon for prepare / recover
        if ((section == WorkoutSection.PREPARE || section == WorkoutSection.RECOVER) && upNextExerciseTypeUiState == null) {
            val nextWorkoutSetIndex = _fullWorkoutSets.indexOf(workoutSetUiState) + 1

            if (nextWorkoutSetIndex != _fullWorkoutSets.size) {
                upNextExerciseTypeUiState = _fullWorkoutSets[nextWorkoutSetIndex].exerciseTypeDTO
            }
        }

        _uiState.value = _uiState.value.copy(
            currentWorkoutSet = workoutSetUiState,
            currentSection = sectionUiState,
            upNextExerciseType = upNextExerciseTypeUiState,
            currentRepValue = repUiState,

            )
    }

    override fun onFinish() {
        _isFinished.value = true
        cancel()
    }

//endregion
}
