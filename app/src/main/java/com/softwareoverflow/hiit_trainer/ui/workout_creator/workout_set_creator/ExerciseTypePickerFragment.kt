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
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.databinding.FragmentExerciseTypePickerBinding
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO
import com.softwareoverflow.hiit_trainer.ui.hideKeyboard
import com.softwareoverflow.hiit_trainer.ui.view.exercise_type_picker.ISelectableListener
import com.softwareoverflow.hiit_trainer.ui.workout_creator.WorkoutCreatorViewModel
import com.softwareoverflow.hiit_trainer.ui.workout_creator.WorkoutCreatorViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_exercise_type_picker.*
import timber.log.Timber

class ExerciseTypePickerFragment : Fragment() {

    // TODO Not sure this is actually needed? Might be best to use nav arguments to pass data back and forth between fragments...
    private val workoutViewModel: WorkoutCreatorViewModel by navGraphViewModels(R.id.nav_workout_creator) {
        // TODO - update this to actually get and send the correct ID
        WorkoutCreatorViewModelFactory(
            activity!!,
            null
        )
    }

    // TODO - N.B if this isn't used, then it isn't created and the app will crash when trying to create a new exercise type
    private val workoutSetViewModel: WorkoutSetCreatorViewModel by navGraphViewModels(R.id.nav_workout_set_creator)
    {
        // TODO this should probably come through the args?
        var workoutSet = workoutViewModel.getWorkoutSetToEdit()
        if (workoutSet == null) {
            workoutSet = WorkoutSetDTO(null, null, 15, 5, 3, 120)
        }

        WorkoutSetCreatorViewModelFactory(workoutSet, context!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentExerciseTypePickerBinding>(
            inflater, R.layout.fragment_exercise_type_picker, container, false
        )
        binding.lifecycleOwner = this

        binding.exerciseTypePickerList.setSelectedItemListener(object : ISelectableListener {
            override fun onItemSelected(selected: Long?) {
                Timber.d("1waybind onItemSelected in fragment $selected")
                workoutSetViewModel.selectedExerciseTypeId.postValue(selected)
            }
        })

        // TODO hopefully data bind the list straight to the recycler view and remove this boilerplate
        workoutSetViewModel.allExerciseTypes.observe(viewLifecycleOwner, Observer {
            Timber.d("1waybind observed changes to allExerciseTypes $it")
            it?.let {
                exerciseTypePickerList.submitList(it, workoutSetViewModel.selectedExerciseTypeId.value)
            }
        })

        // TODO maybe get rid of this method entirely
        workoutSetViewModel.selectedExerciseTypeId.observe(viewLifecycleOwner, Observer {
            Timber.d("2waybind Observed changed to exercise type id: $it")
            exerciseTypePickerList.notifyItemSelected(it)
        })

        binding.createNewExerciseTypeFAB.setOnClickListener {
            findNavController().navigate(R.id.action_exerciseTypePickerFragment_to_exerciseTypeCreator)
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        activity!!.mainActivityFAB.setImageResource(R.drawable.icon_arrow_right)
        activity!!.mainActivityFAB.show()
        activity?.mainActivityFAB?.setOnClickListener {

            // TODO handle animation of button
        }

        hideKeyboard(activity!!)
    }
}
