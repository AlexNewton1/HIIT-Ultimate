package com.softwareoverflow.hiit_trainer.ui.workout_complete

import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.preference.PreferenceManager
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.databinding.FragmentWorkoutCompleteBinding
import com.softwareoverflow.hiit_trainer.ui.upgrade.BillingViewModel
import com.softwareoverflow.hiit_trainer.ui.utils.InAppReviewManager
import com.softwareoverflow.hiit_trainer.ui.utils.safeNavigate
import com.softwareoverflow.hiit_trainer.ui.workout.WorkoutFragmentArgs
import com.softwareoverflow.hiit_trainer.ui.workout.WorkoutViewModel
import com.softwareoverflow.hiit_trainer.ui.workout.WorkoutViewModelFactory
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter
import kotlinx.android.synthetic.main.fragment_workout_creator.*

class WorkoutCompleteFragment : Fragment() {

    private val args: WorkoutFragmentArgs by navArgs()

    private val viewModel: WorkoutViewModel by navGraphViewModels(R.id.workoutFragment) {
        WorkoutViewModelFactory(
            requireActivity().application,
            requireContext(),
            args.workoutId,
            args.dto
        )
    }

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

        val billingClient = ViewModelProvider(requireActivity()).get(BillingViewModel::class.java)
        binding.billing = billingClient

        binding.upgradeToProButton.setOnClickListener {
            val action =
                WorkoutCompleteFragmentDirections.actionWorkoutCompleteFragmentToUpgradeDialog()
            findNavController().safeNavigate(action)
        }

        binding.goHome.setOnClickListener {
            if (viewModel.showUnsavedChangesWarning)
                findNavController().safeNavigate(R.id.action_workoutCompleteFragment_to_unsavedWorkoutWarningDialog)
            else
                findNavController().safeNavigate(R.id.action_workoutCompleteFragment_to_homeScreenFragment)
        }

        binding.saveSpeedDial.y = binding.saveSpeedDial.y - resources.getDimension(R.dimen.fab_size_padded)
        binding.saveSpeedDial.setMenuListener(object : SimpleMenuListenerAdapter() {
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.title == requireContext().getString(R.string.save_as)) {
                    val action =
                        WorkoutCompleteFragmentDirections.actionWorkoutCompleteFragmentToSaveNewWorkoutDialog(
                            dto = viewModel.getOriginalWorkout()
                        )
                    findNavController().safeNavigate(action)

                    return true
                } else if (menuItem.title == resources.getString(R.string.overwrite_existing)) {
                    val action =
                        WorkoutCompleteFragmentDirections.actionWorkoutCompleteFragmentToOverwriteExistingWorkoutDialog(
                            dto = viewModel.getOriginalWorkout()
                        )
                    findNavController().safeNavigate(action)

                    return true
                }

                return false
            }
        })

        setupUnsavedChangesDialog()

        InAppReviewManager.askForReview(requireContext(), requireActivity())

        return binding.root
    }

    private fun setupUnsavedChangesDialog() {
        val showUnsavedChangesWarning =
            PreferenceManager.getDefaultSharedPreferences(requireContext().applicationContext)
                .getBoolean(getString(R.string.pref_unsaved_workout_warning), true)
        if (!showUnsavedChangesWarning)
            viewModel.setUnsavedChangesWarningAccepted()

        viewModel.forceNavigateUp.observe(viewLifecycleOwner, Observer {
            if (it)
                findNavController().safeNavigate(R.id.action_workoutCompleteFragment_to_homeScreenFragment)
        })

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, true) {
            if (viewModel.showUnsavedChangesWarning)
                findNavController().safeNavigate(R.id.action_workoutCompleteFragment_to_unsavedWorkoutWarningDialog)
            else
                findNavController().safeNavigate(R.id.action_workoutCompleteFragment_to_homeScreenFragment)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_bar_save, menu)

        menu[0].setOnMenuItemClickListener {
            if(!saveSpeedDial.isMenuOpen)
                saveSpeedDial.openMenu()
            else
                saveSpeedDial.closeMenu()

            true
        }
    }
}