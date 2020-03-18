package com.softwareoverflow.hiit_trainer.ui.workout_creator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.softwareoverflow.hiit_trainer.R
import kotlinx.android.synthetic.main.activity_main.*

class WorkoutCreatorHomeFragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_workout_creator_home, container, false)

        viewModel.workout.observe(viewLifecycleOwner, Observer {
            val abc = it.name
        })

        return view
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
