package com.softwareoverflow.hiit_trainer.ui.workout_creator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softwareoverflow.hiit_trainer.repository.IWorkoutRepository
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO

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

    private var _workoutSet: MutableLiveData<WorkoutSetDTO> = MutableLiveData()
    var workoutSet: WorkoutSetDTO = WorkoutSetDTO()
        get() = _workoutSet.value ?: WorkoutSetDTO()
        private set(value) {
            val workoutSetIndex =
                _workout.value!!.workoutSets.indexOfFirst { it.id == value.id } ?: -1
            // TODO work out how the adapter can notify item changed in this case. Or just livedata the list of workout sets which should handle it auto-magically
            // If the workoutSet id already exists in the list, update this entry only
            if (workoutSetIndex >= 0) {
                val newWorkoutSets = _workout.value!!.workoutSets.toCollection(mutableListOf())
                newWorkoutSets.removeAt(workoutSetIndex)
                newWorkoutSets.add(workoutSetIndex, value)
            } else { // If the id is not already in the list this must be a new WorkoutSet
                _workout.value!!.workoutSets.add(value)
            }

            //reset the field to default to be created as a new WorkoutSet
            field = WorkoutSetDTO()
        }

    init {
        if (id == null)
            _workout.value = WorkoutDTO()
        else
            _workout.value = repository.getWorkoutById(id).value
    }

    // TODO - 2 way bind the workout name edit text

    /**
     * If the workoutSet has an id already present in the list, that entry will be updated.
     * If the workoutSet has an id not already in the list, the item will be appended to the list
     */
    fun addOrUpdateWorkoutSet(workoutSet: WorkoutSetDTO) {
        this.workoutSet = workoutSet
    }
}
