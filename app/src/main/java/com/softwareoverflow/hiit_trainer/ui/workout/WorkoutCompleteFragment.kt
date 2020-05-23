package com.softwareoverflow.hiit_trainer.ui.workout

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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

        // TODO consider making upgrade button it's own custom view so this code doesn't need repeating
        binding.root.background.colorFilter =
            PorterDuffColorFilter(binding.upgradeToProIcon.getColor(), PorterDuff.Mode.SRC_IN)
        binding.root.background.alpha = 75

        return binding.root
    }
}