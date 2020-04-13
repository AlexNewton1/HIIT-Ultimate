package com.softwareoverflow.hiit_trainer.ui.workout_creator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.softwareoverflow.hiit_trainer.R
import kotlinx.android.synthetic.main.activity_main.*

class WorkoutCreatorHomeFragment : Fragment() {

    // TODO probably need to use this viewmodel so that it is created in time for the other fragments which (might) use it. Can acheive by using data bidning and assigning bidning.viewModel = viewModel
    private val viewModel: WorkoutCreatorViewModel by navGraphViewModels(R.id.nav_workout_creator) {
        WorkoutCreatorViewModelFactory(
            activity!!,
            null
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_workout_creator_home, container, false)
    }

    override fun onStart() {
        super.onStart()

        activity?.mainActivityFAB?.show()
        activity?.mainActivityFAB?.setImageResource(R.drawable.icon_plus)
        activity?.mainActivityFAB?.setOnClickListener {
            findNavController().navigate(R.id.action_workoutCreatorHomeFragment_to_exerciseTypePickerFragment)
        }
    }

    override fun onStop() {
        super.onStop()

        // TODO work out if this is needed, as it seems to happen after the listener gets set in the next fragment :o
        //activity?.mainActivityFAB?.setOnClickListener(null)
    }
}
