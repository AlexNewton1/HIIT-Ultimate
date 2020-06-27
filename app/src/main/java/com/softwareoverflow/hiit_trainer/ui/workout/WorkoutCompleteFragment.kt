package com.softwareoverflow.hiit_trainer.ui.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.databinding.FragmentWorkoutCompleteBinding
import com.softwareoverflow.hiit_trainer.ui.upgrade.AdsManager
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter
import kotlinx.android.synthetic.main.fragment_workout_complete.*

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
            val action =
                WorkoutCompleteFragmentDirections.actionWorkoutCompleteFragmentToUpgradeDialog()

            // If the snackbar for no remaining save slots is showing, clicks are registered on the screen.
            // This causes IllegalArgumentException as the save workout dialog is still on screen. Catch these exceptions and do nothing.
            try {
                findNavController().navigate(action)
            } catch (ex: IllegalArgumentException) {
                // Do nothing
            }
        }

        binding.goHome.setOnClickListener {
            val action =
                WorkoutCompleteFragmentDirections.actionWorkoutCompleteFragmentToHomeScreenFragment()
            findNavController().navigate(action)
        }

        binding.saveSpeedDial.setMenuListener(object : SimpleMenuListenerAdapter() {
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.title == requireContext().getString(R.string.save_as)) {
                    val action =
                        WorkoutCompleteFragmentDirections.actionWorkoutCompleteFragmentToSaveNewWorkoutDialog(
                            workoutDto = viewModel.getOriginalWorkout()
                        )
                    findNavController().navigate(action)

                    return true
                } else if (menuItem.title == resources.getString(R.string.overwrite_existing)) {
                    val action =
                        WorkoutCompleteFragmentDirections.actionWorkoutCompleteFragmentToOverwriteExistingWorkoutDialog(
                            workoutDto = viewModel.getOriginalWorkout()
                        )
                    findNavController().navigate(action)

                    return true
                }

                return false
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        if(AdsManager.hasUserUpgraded)
            upgradeToProButton.visibility = View.GONE
    }
}