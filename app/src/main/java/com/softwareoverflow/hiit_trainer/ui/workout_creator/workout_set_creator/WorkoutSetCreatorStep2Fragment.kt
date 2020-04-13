package com.softwareoverflow.hiit_trainer.ui.workout_creator.workout_set_creator


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.databinding.FragmentWorkoutSetCreatorStep2Binding
import com.softwareoverflow.hiit_trainer.ui.workout_creator.WorkoutCreatorViewModel
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

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


        return binding.root
    }


    override fun onStart() {
        super.onStart()

        activity!!.mainActivityFAB.show()
        activity?.mainActivityFAB?.setImageResource(R.drawable.icon_tick)
        activity!!.mainActivityFAB.setOnClickListener {
            Timber.d("Clicked save on workoutSet ${workoutSetViewModel.workoutSet.value}")
            //findNavController().navigate(R.id.action_workoutSetCreator_to_workoutCreatorHomeFragment)
            // TODO - start FAB animation back to add for the previous screen!
        }
    }


}
 