package com.softwareoverflow.hiit_trainer.ui.workout_creator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softwareoverflow.hiit_trainer.repository.IWorkoutRepository
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO
import timber.log.Timber

/**
 * ViewModel for creating / editing workouts.
 *
 * @param repository the repository to use for loading the workout
 * @param id the Id of the workout to load, or null if creating new
 */
class WorkoutCreatorViewModel(repository: IWorkoutRepository, id: Long?) : ViewModel() {

    private var _workout: MutableLiveData<WorkoutDTO> = MutableLiveData()
    val workout: LiveData<WorkoutDTO>
        get() = _workout

    private var workoutSetIndex: Int? = null

    init {
        if(id == null) {
            _workout.value = WorkoutDTO()
        }
        else {
            _workout.value = repository.getWorkoutById(id).value
        }
    }

    fun setWorkoutName(name: String){
        _workout.value?.name = name
    }

    /**
     * Updates the workout set at the workoutSetIndex. Inserts into the list if this number is
     * larger than the current size of the list
     */
    fun updateWorkoutSet(workoutSet: WorkoutSetDTO){
        if(workoutSetIndex == null){
            Timber.e("Attempting to update workout set with a null index variable.")
        }

        workoutSetIndex?.let{
            _workout.value?.workoutSets?.removeAt(it)
            _workout.value?.workoutSets?.add(it, workoutSet)
        }
    }

    /**
     * Used to indicate that the workout set at the given index is currently being altered
     */
    fun setWorkoutSetIndex(index: Int?){
        workoutSetIndex = index
    }

    /**
     * Ease of use function to set the workout set index to indicate that the workout set being
     * altered in a new workout set
     */
    fun setWorkoutSetIndexToNext(){
        workoutSetIndex = _workout.value?.workoutSets?.size
    }

    fun getWorkoutSetToEdit() : WorkoutSetDTO? {
        workoutSetIndex?.let {
            return _workout.value?.workoutSets?.get(it)
        }

        return null
    }
}
