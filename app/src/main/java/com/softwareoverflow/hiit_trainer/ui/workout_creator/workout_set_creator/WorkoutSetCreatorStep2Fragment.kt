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
import com.softwareoverflow.hiit_trainer.databinding.FragmentWorkoutSetCreatorStep2Binding
import com.softwareoverflow.hiit_trainer.ui.workout_creator.WorkoutCreatorViewModel
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

/**
 * Allows user to input values for the [com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO] (e.g. work time, rest time etc)
 */
class WorkoutSetCreatorStep2Fragment : Fragment() {

    // These do not take the corresponding factory, as the view model *SHOULD* always be created by this point
    private val workoutCreatorViewModel: WorkoutCreatorViewModel by navGraphViewModels(R.id.nav_workout_creator)
    private val workoutSetViewModel: WorkoutSetCreatorViewModel by navGraphViewModels(R.id.nav_workout_set_creator)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentWorkoutSetCreatorStep2Binding>(
            inflater, R.layout.fragment_workout_set_creator_step_2, container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = workoutSetViewModel

        workoutSetViewModel.selectedExerciseTypeId.observe(viewLifecycleOwner, Observer {
            Timber.d("SelectedExerciseTypeId changed to $it")
        })

        return binding.root
    }


    override fun onStart() {
        super.onStart()

        requireActivity().mainActivityFAB.show()
        activity?.mainActivityFAB?.setImageResource(R.drawable.icon_tick)
        requireActivity().mainActivityFAB.setOnClickListener {
            // At the point we try and add this workout set to the workout, it should not be null. Throw NPE if it is ever null
            workoutCreatorViewModel.addOrUpdateWorkoutSet(workoutSetViewModel.workoutSet.value!!)
            findNavController().popBackStack(R.id.workoutCreatorHomeFragment, false)
        }
    }


}
 