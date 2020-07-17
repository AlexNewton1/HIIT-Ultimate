package com.softwareoverflow.hiit_trainer.ui.workout

import android.app.Application
import android.text.format.DateUtils
import androidx.lifecycle.*
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO
import com.softwareoverflow.hiit_trainer.ui.getDuration
import com.softwareoverflow.hiit_trainer.ui.getFullWorkoutSets
import com.softwareoverflow.hiit_trainer.ui.getWorkoutCompleteExerciseType
import kotlinx.coroutines.launch

class WorkoutViewModel(application: Application, private val workoutDto: WorkoutDTO) :
    AndroidViewModel(application) {

    private val _workout = MutableLiveData<WorkoutDTO>()
    //private val _mutableWorkout = MediatorLiveData<WorkoutDTO>()
    val workout: LiveData<WorkoutDTO>
        get() = _workout

    private val _currentWorkoutSet = MutableLiveData<WorkoutSetDTO?>()
    val currentWorkoutSet: LiveData<WorkoutSetDTO?>
        get() = _currentWorkoutSet

    private val _currentRep = MutableLiveData(1)
    var currentRepFormatted =
        Transformations.map(_currentRep) { "$it/${_currentWorkoutSet.value?.numReps}" }

    private val _sectionTimeRemaining = MutableLiveData(5)
    val sectionTimeRemaining =
        Transformations.map(_sectionTimeRemaining) { DateUtils.formatElapsedTime(it.toLong()) }

    private val _workoutTimeRemaining = MutableLiveData(0)
    val workoutTimeRemainingFormatted = Transformations.map(_workoutTimeRemaining) {
        DateUtils.formatElapsedTime(it.toLong())
    }

    private val _currentSection: MutableLiveData<WorkoutSection> =
        MutableLiveData(WorkoutSection.WORK)
    val currentSection = Transformations.map(_currentSection) { it.toString() }

    private val _upNextExerciseType = MutableLiveData<ExerciseTypeDTO?>(null)
    val upNextExerciseType: LiveData<ExerciseTypeDTO?>
        get() = _upNextExerciseType

    private val _showUpNextLabel = MutableLiveData(false)
    val showUpNextLabel: LiveData<Boolean>
        get() = _showUpNextLabel

    private val _animateUpNextExerciseType = MutableLiveData(false)
    val animateUpNextExerciseType: LiveData<Boolean>
        get() = _animateUpNextExerciseType

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
            // Copy the workout and deep copy the workout set list so as to not change the original
            _workout.value = workoutDto.copy(
                workoutSets = workoutDto.workoutSets.map { it.copy() }.toMutableList()
            ).apply {
                _currentWorkoutSet.value = this.workoutSets[0]
                _currentRep.value = 1
                _sectionTimeRemaining.value = _currentWorkoutSet.value!!.workTime
                _workoutTimeRemaining.value = this.getDuration()
            }
        }
    }

    fun updateValues(section: WorkoutSection, set: WorkoutSetDTO, currentRep: Int) {
        if (_currentSection.value != section)
            _currentSection.value = section

        if (_currentWorkoutSet.value != set) {
            _currentWorkoutSet.value = set

            // Reset the up next icon as the set has just changed
            _upNextExerciseType.value = null
            _showUpNextLabel.value = false
            _animateUpNextExerciseType.value = false
        }

        if (_currentRep.value != currentRep)
            _currentRep.value = currentRep

        // Show the up next icon for prepare / recover
        if ((section == WorkoutSection.PREPARE || section == WorkoutSection.RECOVER) &&
            _upNextExerciseType.value == null
        ) {

            val workoutSets = _workout.value?.workoutSets!!
            val nextWorkoutSetIndex = workoutSets.indexOf(currentWorkoutSet.value) + 1

            if (nextWorkoutSetIndex != workoutSets.size) {
                _upNextExerciseType.value = workoutSets[nextWorkoutSetIndex].exerciseTypeDTO
                _showUpNextLabel.value = true
            }
        }
    }

    /** Decrements the main on-screen timer and the remaining time**/
    fun updateTimers(workoutTimeRemaining: Int, sectionTimeRemaining: Int) {
        _workoutTimeRemaining.value = workoutTimeRemaining
        _sectionTimeRemaining.value = sectionTimeRemaining

        // Show the upcoming exercise type
        val workoutSets = _workout.value?.getFullWorkoutSets(getApplication<Application>().applicationContext)!!
        val currentWorkoutSetIndex = workoutSets.indexOf(currentWorkoutSet.value) + 1
        if ((_currentSection.value == WorkoutSection.RECOVER || _currentSection.value == WorkoutSection.PREPARE)) {
            if (sectionTimeRemaining <= 3 && _animateUpNextExerciseType.value == false) {
                _animateUpNextExerciseType.value = true
                _showUpNextLabel.value = false
            }
        } else if (currentWorkoutSetIndex == workoutSets.size && _currentRep.value == _currentWorkoutSet.value!!.numReps) {
            _showUpNextLabel.value = true
            _upNextExerciseType.value = getWorkoutCompleteExerciseType(getApplication())
        }
    }

    fun getOriginalWorkout(): WorkoutDTO = workoutDto

    // region user controlled buttons
    fun toggleSound() {
        _soundOn.apply {
            value = !value!! // Looks a bit funky but simply switches the boolean value
        }
    }

    fun togglePause() {
        _isPaused.apply {
            value = !value!! // Looks a bit funky but simply switches the boolean value
        }
    }

    fun skipSection() {
        _skipSection.value = true
    }

    fun onSectionSkipped() {
        _skipSection.value = false
    }
    //endregion
}
