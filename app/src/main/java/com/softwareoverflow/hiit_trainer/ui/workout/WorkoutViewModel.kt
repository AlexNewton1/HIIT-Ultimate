package com.softwareoverflow.hiit_trainer.ui.workout

import androidx.lifecycle.*
import com.softwareoverflow.hiit_trainer.repository.IWorkoutRepository
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO
import com.softwareoverflow.hiit_trainer.ui.getDuration
import com.softwareoverflow.hiit_trainer.ui.view.LoadingSpinner
import kotlinx.coroutines.launch

class WorkoutViewModel(workoutId: Long, repo: IWorkoutRepository) : ViewModel() {

    private val _workout = MutableLiveData<WorkoutDTO>()

    private val _currentWorkoutSet = MutableLiveData<WorkoutSetDTO?>()
    val currentWorkoutSet: LiveData<WorkoutSetDTO?>
        get() = _currentWorkoutSet

    private val _currentRep = MutableLiveData(1)
    var currentRepFormatted =
        Transformations.map(_currentRep) { "$it/${_currentWorkoutSet.value?.numReps}" }

    // TODO - probably have this 5 value not hard coded - perhaps a user setting
    private val _repTimeRemaining = MutableLiveData(5)
    val repTimeRemaining: LiveData<Int>
        get() = _repTimeRemaining

    private val _upNextExerciseType = MutableLiveData<ExerciseTypeDTO?>()
    val upNextExerciseType: LiveData<ExerciseTypeDTO?>
        get() = _upNextExerciseType

    private val _label = MutableLiveData<String?>()
    val label: LiveData<String?>
        get() = _label

    private val _workoutTimeRemaining = MutableLiveData(0)
    val workoutTimeRemaining: LiveData<Int>
        get() = _workoutTimeRemaining

    init {
        viewModelScope.launch {
            LoadingSpinner.showLoadingIcon()
            val savedWorkout = repo.getWorkoutById(workoutId)
            _workout.value = savedWorkout

            _workoutTimeRemaining.value = savedWorkout.getDuration()

            LoadingSpinner.hideLoadingIcon()

        }
    }

    fun onRepCompleted(){

    }

    /** Decrements the main on-screen timer and the remaining time**/
    fun decrementTimers(){
        _repTimeRemaining.value = _repTimeRemaining.value!! - 1
        _workoutTimeRemaining.value = _workoutTimeRemaining.value!! - 1
    }
}
