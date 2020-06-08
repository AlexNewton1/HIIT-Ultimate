package com.softwareoverflow.hiit_trainer.ui.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.databinding.FragmentWorkoutCompleteBinding

class WorkoutCompleteFragment : Fragment() {

    private val viewModel: WorkoutViewModel by navGraphViewModels(R.id.workoutFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentWorkoutCompleteBinding>(
            inflater,
            R.layout.fragment_workout_complete,
            container,
            false
        )
        binding.lifecycleOwner = this

        binding.upgradeToProButton.setOnClickListener {
            val action = WorkoutCompleteFragmentDirections.actionWorkoutCompleteFragmentToUpgradeDialog()
            findNavController().navigate(action)
        }

        binding.goHome.setOnClickListener {
            val action = WorkoutCompleteFragmentDirections.actionWorkoutCompleteFragmentToHomeScreenFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }
}