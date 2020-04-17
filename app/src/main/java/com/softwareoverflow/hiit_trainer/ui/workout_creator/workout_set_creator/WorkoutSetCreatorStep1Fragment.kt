package com.softwareoverflow.hiit_trainer.ui.workout_creator.workout_set_creator


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.snackbar.Snackbar
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.databinding.FragmentWorkoutSetCreatorStep1Binding
import com.softwareoverflow.hiit_trainer.ui.hideKeyboard
import com.softwareoverflow.hiit_trainer.ui.view.exercise_type_picker.IListAdapterEventListener
import com.softwareoverflow.hiit_trainer.ui.workout_creator.WorkoutCreatorViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_workout_set_creator_step_1.*

class WorkoutSetCreatorStep1Fragment : Fragment() {

    // TODO Not sure this is actually needed? Might be best to use nav arguments to pass data back and forth between these fragments...
    private val workoutViewModel: WorkoutCreatorViewModel by navGraphViewModels(R.id.nav_workout_creator)

    private val workoutSetViewModel: WorkoutSetCreatorViewModel by navGraphViewModels(R.id.nav_workout_set_creator)
    {
        // TODO pass in the correct ID - this should probably come through the args?
        WorkoutSetCreatorViewModelFactory(workoutViewModel.workoutSet, context!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentWorkoutSetCreatorStep1Binding>(
            inflater, R.layout.fragment_workout_set_creator_step_1, container, false
        )
        binding.lifecycleOwner = this

        workoutSetViewModel.allExerciseTypes.observe(viewLifecycleOwner, Observer {
            it?.let{
                exerciseTypePickerList.submitList(it)
            }
        })

        binding.exerciseTypePickerList.setAdapterListener(object: IListAdapterEventListener {
            override fun onItemSelected(selected: Long?) {
                workoutSetViewModel.selectedExerciseTypeId.postValue(selected)
            }

            override fun triggerItemDeletion(id: Long) {
                workoutSetViewModel.deleteExerciseTypeById(id)
            }

            override fun triggerItemEdit(id: Long) {
                workoutSetViewModel.selectedExerciseTypeId.value = id
                createOrEditExerciseType()
            }
        })

        binding.createNewExerciseTypeFAB.setOnClickListener {
            workoutSetViewModel.selectedExerciseTypeId.value = null
            createOrEditExerciseType()
        }

        return binding.root
    }

    fun createOrEditExerciseType(){
        findNavController().navigate(R.id.action_exerciseTypePickerFragment_to_exerciseTypeCreator)
    }

    override fun onStart() {
        super.onStart()
        activity!!.mainActivityFAB.setImageResource(R.drawable.icon_arrow_right)
        activity!!.mainActivityFAB.show()
        activity?.mainActivityFAB?.setOnClickListener {

            workoutSetViewModel.selectedExerciseTypeId.value?.let {
                workoutSetViewModel.setChosenExerciseTypeId(it)
                findNavController().navigate(R.id.action_exerciseTypePickerFragment_to_workoutSetCreator)
            }

            Snackbar.make(view!!, R.string.select_exercise_type, Snackbar.LENGTH_SHORT).show()


            // TODO handle animation of button
        }

        hideKeyboard(activity!!)
    }
}
