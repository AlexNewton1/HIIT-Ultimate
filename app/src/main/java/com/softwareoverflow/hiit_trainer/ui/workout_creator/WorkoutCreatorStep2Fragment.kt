package com.softwareoverflow.hiit_trainer.ui.workout_creator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.google.android.material.snackbar.Snackbar
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.databinding.FragmentWorkoutCreatorStep2Binding
import kotlinx.android.synthetic.main.fragment_workout_creator_step_2.*

/**
 * A simple [Fragment] subclass.
 */
class WorkoutCreatorStep2Fragment : Fragment() {

    private val viewModel: WorkoutCreatorViewModel by navGraphViewModels(R.id.nav_workout_creator)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentWorkoutCreatorStep2Binding>(
            inflater,
            R.layout.fragment_workout_creator_step_2,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // TODO - add 2 way text binding for the name edit text
        binding.saveWorkoutButton.setOnClickListener {
            val name = workoutNameET.text.toString()
            if(name.isBlank()) {
                Snackbar.make(view!!, "Please enter a workout name before saving", Snackbar.LENGTH_SHORT).show()
            } else {
                viewModel.setWorkoutName(name)
                viewModel.createOrUpdateWorkout {
                    Snackbar.make(view!!, "Workout '$name' Saved!", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }

}
