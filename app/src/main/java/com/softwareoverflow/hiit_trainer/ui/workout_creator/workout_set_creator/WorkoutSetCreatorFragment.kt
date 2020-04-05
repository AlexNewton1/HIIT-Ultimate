package com.softwareoverflow.hiit_trainer.ui.workout_creator.workout_set_creator


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.databinding.FragmentWorkoutSetCreatorBinding
import com.softwareoverflow.hiit_trainer.ui.getColorId
import com.softwareoverflow.hiit_trainer.ui.getDrawableId
import com.softwareoverflow.hiit_trainer.ui.workout_creator.WorkoutCreatorViewModel
import com.softwareoverflow.hiit_trainer.ui.workout_creator.WorkoutCreatorViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_workout_set_creator.*

class WorkoutSetCreatorFragment : Fragment() {

    private val workoutCreatorViewModel: WorkoutCreatorViewModel by navGraphViewModels(R.id.nav_workout_creator) {
        // TODO - update this to actually get and send the correct ID
        WorkoutCreatorViewModelFactory(
            activity!!,
            null
        )
    }

    // This does not take the corresponding factory, as the view model *SHOULD* always be created by this point
    private val workoutSetViewModel: WorkoutSetCreatorViewModel by navGraphViewModels(R.id.nav_workout_set_creator)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentWorkoutSetCreatorBinding>(
            inflater, R.layout.fragment_workout_set_creator, container, false
        )

        binding.viewModel = workoutSetViewModel

        workoutSetViewModel.exerciseType.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.exerciseTypeIcon.setBackground(it.iconName?.getDrawableId(context!!))
                binding.exerciseTypeIcon.setColor(it.colorHex?.getColorId())
            }
        })

        return binding.root
    }


    override fun onStart() {
        super.onStart()

        activity!!.mainActivityFAB.show()
        activity?.mainActivityFAB?.setImageResource(R.drawable.icon_plus)
        /*activity!!.mainActivityFAB.setOnClickListener { v: View ->
            findNavController().navigate(R.id.action_workoutSetCreator_to_workoutCreatorHomeFragment)
            // TODO - start FAB animation back to add for the previous screen!
        }*/

        // TODO change this to data binding - will require working out handling of the viewModel for a given workoutSet
        exerciseTypeIcon.setColor(Color.BLUE)
        exerciseTypeIcon.setImageResource(R.drawable.icon_fire)
    }


}
 