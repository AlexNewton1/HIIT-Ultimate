package com.softwareoverflow.hiit_trainer.ui.workout

import android.text.format.DateUtils
import androidx.lifecycle.*
import com.softwareoverflow.hiit_trainer.repository.IWorkoutRepository
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO
import com.softwareoverflow.hiit_trainer.ui.getDuration
import com.softwareoverflow.hiit_trainer.ui.view.LoadingSpinner
import kotlinx.coroutines.launch
import timber.log.Timber

class WorkoutViewModel(workoutId: Long, repo: IWorkoutRepository) : ViewModel() {

    private val _workout = MutableLiveData<WorkoutDTO>()
    val workout: LiveData<WorkoutDTO>
        get() = _workout

    private val _currentWorkoutSet = MutableLiveData<WorkoutSetDTO?>()
    val currentWorkoutSet: LiveData<WorkoutSetDTO?>
        get() = _currentWorkoutSet

    private val _currentRep = MutableLiveData(1)
    var currentRepFormatted =
        Transformations.map(_currentRep) { "$it/${_currentWorkoutSet.value?.numReps}" }

    private val _sectionTimeRemaining = MutableLiveData(5)
    val sectionTimeRemaining = Transformations.map(_sectionTimeRemaining) { DateUtils.formatElapsedTime(it.toLong()) }

    private val _workoutTimeRemaining = MutableLiveData(0)
    val workoutTimeRemainingFormatted = Transformations.map(_workoutTimeRemaining) {
        DateUtils.formatElapsedTime(it.toLong())
    }

    private val _currentSection: MutableLiveData<WorkoutSection> = MutableLiveData(WorkoutSection.WORK)
    val currentSection = Transformations.map(_currentSection){ it.toString() }
    val currentSectionIcon = Transformations.map(_currentSection){
        when (it) {
            WorkoutSection.WORK -> "icon_fire"
            WorkoutSection.REST -> "icon_rest"
            WorkoutSection.RECOVER -> "icon_recover"
            else -> {
                Timber.w("Unable to find icon for WorkoutSection $it")
                return@map ""
            }
        }
    }

    // region user controls
    private val _soundOn = MutableLiveData(true)
    val soundOn: LiveData<Boolean>
        get() = _soundOn

    private val _isPaused = MutableLiveData(false)
    val isPaused: LiveData<Boolean>
        get() = _isPaused

    private val _skipSection = MutableLiveData(false)
    val skipSection: LiveData<Boolean>
        get() = _skipSection

    //endregion

    init {
        viewModelScope.launch {
            LoadingSpinner.showLoadingIcon()
            _workout.value = repo.getWorkoutById(workoutId).apply {
                _currentWorkoutSet.value = this.workoutSets[0]
                _currentRep.value = 1
                _sectionTimeRemaining.value = _currentWorkoutSet.value!!.workTime
                _workoutTimeRemaining.value = this.getDuration()
            }

            LoadingSpinner.hideLoadingIcon()

        }
    }

    fun updateValues(section: WorkoutSection, set: WorkoutSetDTO, currentRep: Int) {
        if(_currentSection.value != section)
            _currentSection.value = section

        if (_currentWorkoutSet.value != set)
            _currentWorkoutSet.value = set

        if (_currentRep.value != currentRep)
            _currentRep.value = currentRep

        // Show the "up next" exercise type information
        if(_currentSection.value == WorkoutSection.RECOVER){
            // TODO
        }
    }

    /** Decrements the main on-screen timer and the remaining time**/
    fun updateTimers(workoutTimeRemaining: Int, sectionTimeRemaining: Int) {
        _workoutTimeRemaining.value = workoutTimeRemaining
        _sectionTimeRemaining.value = sectionTimeRemaining
    }

    fun toggleSound() {
        _soundOn.apply {
            value = !value!!
        } // Looks a bit funky but simply switches the boolean value
    }

    fun togglePause() {
        _isPaused.apply {
            value = !value!!
        } // Looks a bit funky but simply switches the boolean value
    }

    fun skipSection() {
        // TODO - work out defect in skipping sections leaving the remaining workout time out of sync
        _skipSection.value = true
    }

    fun onSectionSkipped() {
        _skipSection.value = false
    }
}
