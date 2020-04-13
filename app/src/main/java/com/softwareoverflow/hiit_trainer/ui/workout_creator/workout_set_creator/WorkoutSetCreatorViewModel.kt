package com.softwareoverflow.hiit_trainer.ui.workout_creator.workout_set_creator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softwareoverflow.hiit_trainer.repository.IWorkoutRepository
import com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO

class WorkoutSetCreatorViewModel(
    workoutSetDTO: WorkoutSetDTO? = null,
    repo: IWorkoutRepository
) :
    ViewModel() {

    val allExerciseTypes = repo.getAllExerciseTypes()

    var selectedExerciseTypeId = MutableLiveData<Long?>(null)


    // TODO - change all of these values to be Transformations.map (or switchMap?) to avoid having to set them all once at init
    private var _exerciseType: MutableLiveData<ExerciseTypeDTO?> =
        MutableLiveData(workoutSetDTO?.exerciseTypeDTO)
    val exerciseType: LiveData<ExerciseTypeDTO?>
        get() = _exerciseType

    private var _workTime: MutableLiveData<Int> = MutableLiveData(15)
    val workTime: LiveData<Int>
        get() = _workTime

    private var _restTime: MutableLiveData<Int> = MutableLiveData(5)
    val restTime: LiveData<Int>
        get() = _restTime

    private var _numReps: MutableLiveData<Int> = MutableLiveData(3)
    val numReps: LiveData<Int>
        get() = _numReps

    private var _recoverTime: MutableLiveData<Int> = MutableLiveData(120)
    val recoverTime: LiveData<Int>
        get() = _recoverTime

    init {
        workoutSetDTO?.let {
            _workTime.value = it.workTime
            _restTime.value = it.restTime
            _numReps.value = it.numReps
            _recoverTime.value = it.recoverTime
            _exerciseType.value = it.exerciseTypeDTO
        }

       /* viewModelScope.launch {
            Timber.d("2waybind current value ${selectedExerciseTypeId.value}")
            delay(5000)
            Timber.d("2waybind later value ${selectedExerciseTypeId.value}")
            delay(5000)
            Timber.d("2waybind latest value ${selectedExerciseTypeId.value}")

            delay (1000)
            Timber.d("2waybind updating the livedata value")
            selectedExerciseTypeId.setValue(1L)
        }*/
    }

    /*fun setSelectedExerciseTypeId(exerciseTypeId: Long) {
        Timber.d("2waybind Inside setter")
        selectedExerciseTypeId = exerciseTypeId
    }*/
}