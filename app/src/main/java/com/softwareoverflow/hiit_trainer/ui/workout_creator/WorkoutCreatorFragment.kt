package com.softwareoverflow.hiit_trainer.ui.workout_creator

import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.databinding.FragmentWorkoutCreatorBinding
import com.softwareoverflow.hiit_trainer.ui.MainActivity
import com.softwareoverflow.hiit_trainer.ui.getFormattedDuration
import com.softwareoverflow.hiit_trainer.ui.upgrade.AdsManager
import com.softwareoverflow.hiit_trainer.ui.utils.hideKeyboard
import com.softwareoverflow.hiit_trainer.ui.utils.safeNavigate
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.IEditableOrderedListEventListener
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.SpacedListDecoration
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.workout.WorkoutSetListAdapter
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter
import kotlinx.android.synthetic.main.fragment_workout_creator.*
import kotlinx.android.synthetic.main.fragment_workout_creator.view.*


class WorkoutCreatorFragment : Fragment() {

    private val args: WorkoutCreatorFragmentArgs by navArgs()

    private val viewModel: WorkoutCreatorViewModel by navGraphViewModels(R.id.nav_workout_creator) {
        WorkoutCreatorViewModelFactory(
            requireActivity(),
            args.workoutCreatorWorkoutId
        )
    }

    private lateinit var noSetsSnackbar: Snackbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentWorkoutCreatorBinding>(
            inflater,
            R.layout.fragment_workout_creator,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        setupUnsavedChangesDialog()

        binding.workoutRepeaterFAB.setOnClickListener {
            findNavController().safeNavigate(R.id.action_workoutCreatorFragment_to_repeatWorkoutDialog)
        }

        binding.listWorkoutSets.adapter = WorkoutSetListAdapter()
        binding.listWorkoutSets.addItemDecoration(
            SpacedListDecoration(
                requireContext()
            )
        )

        (binding.listWorkoutSets.adapter as WorkoutSetListAdapter).setEventListener(object :
            IEditableOrderedListEventListener {

            /** The item has been clicked. Take the user straight to the edit page **/
            override fun onItemSelected(id: Long?) {
                triggerItemEdit(id!!)
            }

            /** (Effectively) swaps the WorkoutSet at position [fromPosition] and [toPosition] */
            override fun triggerItemChangePosition(fromPosition: Int, toPosition: Int) {
                viewModel.changeWorkoutSetOrder(fromPosition, toPosition)
            }

            /** Triggers deletion of the item at position [id] */
            override fun triggerItemDeletion(id: Long) {
                viewModel.removeWorkoutSetFromWorkout(id.toInt())
            }

            /** Triggers the edit of the item at the position [id] */
            override fun triggerItemEdit(id: Long) {
                viewModel.setWorkoutSetToEdit(id.toInt())
                findNavController().safeNavigate(R.id.action_workoutCreatorHomeFragment_to_exerciseTypePickerFragment)
            }
        })

        viewModel.workout.observe(viewLifecycleOwner, Observer {
            it?.let {
                (listWorkoutSets.adapter as WorkoutSetListAdapter).submitList(it.workoutSets.toMutableList()) // Call toMutableList as otherwise the list does not update when only the order of elements has changed

                if (it.workoutSets.isEmpty())
                    noSetsTextHint.visibility = View.VISIBLE
                else
                    noSetsTextHint.visibility = View.GONE

                durationText.text = getString(R.string.duration_text, it.getFormattedDuration())

                val formattedDuration =
                    if (it.numReps > 1) {
                        requireContext().getString(
                            R.string.workout_recovery_time_full,
                            it.numReps,
                            it.recoveryTime
                        )
                    } else {
                        "1x"
                    }
                workoutRepeatText.text = formattedDuration


                (requireActivity() as MainActivity).supportActionBar?.title =
                    if (it.name.isBlank())
                        getString(R.string.create_your_workout)
                    else
                        getString(R.string.edit_your_workout, it.name)
            }
        })

        binding.createNewWorkoutSetButton.setOnClickListener {
            viewModel.setWorkoutSetToEdit(null)
            findNavController().safeNavigate(R.id.action_workoutCreatorHomeFragment_to_exerciseTypePickerFragment)
        }

        binding.startWorkoutButton.setOnClickListener {
            if (viewModel.workout.value!!.workoutSets.isEmpty())
                noSetsSnackbar.show()
            else {
                val action =
                    WorkoutCreatorFragmentDirections.actionWorkoutCreatorFragmentToWorkoutFragment(
                        dto = viewModel.workout.value
                    )

                // Show an advert with callback to navigate to the workout
                AdsManager.showAdBeforeWorkout {
                    findNavController().safeNavigate(action)
                }
            }
        }

        setupSaveSpeedDial(binding.root)

        return binding.root
    }

    private fun setupSaveSpeedDial(view: View) {
        view.saveSpeedDial.y =
            view.saveSpeedDial.y - resources.getDimension(R.dimen.fab_size_padded)

        view.saveSpeedDial.setMenuListener(object : SimpleMenuListenerAdapter() {
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (viewModel.workout.value!!.workoutSets.isEmpty()) {
                    noSetsSnackbar.show()
                    return false
                }

                if (menuItem.title == requireContext().getString(R.string.save_as)) {
                    val action =
                        WorkoutCreatorFragmentDirections.actionWorkoutCreatorFragmentToSaveNewWorkoutDialog(
                            dto = viewModel.workout.value!!
                        )
                    findNavController().safeNavigate(action)
                } else if (menuItem.title == resources.getString(R.string.overwrite_existing)) {
                    if (viewModel.getNumSavedWorkouts() == 0) {
                        Snackbar.make(
                            requireView(),
                            getString(R.string.no_saved_workouts),
                            Snackbar.LENGTH_SHORT
                        ).show()
                        return false
                    }

                    val action =
                        WorkoutCreatorFragmentDirections.actionWorkoutCreatorFragmentToOverwriteExistingWorkoutDialog(
                            dto = viewModel.workout.value!!
                        )
                    findNavController().safeNavigate(action)
                }

                return false
            }
        })
    }

    override fun onResume() {
        super.onResume()
        hideKeyboard(requireActivity())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_bar_save, menu)

        menu[0].setOnMenuItemClickListener {
            if (!saveSpeedDial.isMenuOpen)
                saveSpeedDial.openMenu()
            else
                saveSpeedDial.closeMenu()

            true
        }
    }

    private fun setupUnsavedChangesDialog() {
        val showUnsavedChangesWarning =
            PreferenceManager.getDefaultSharedPreferences(requireContext().applicationContext)
                .getBoolean(getString(R.string.pref_unsaved_changes_warning), true)
        if (!showUnsavedChangesWarning)
            viewModel.setUnsavedChangesWarningAccepted()

        viewModel.forceNavigateUp.observe(viewLifecycleOwner, Observer {
            if (it)
                findNavController().navigateUp()
        })

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, true) {
            if (viewModel.showUnsavedChangesWarning)
                findNavController().safeNavigate(R.id.action_workoutCreatorFragment_to_unsavedChangesWarningDialog)
            else
                findNavController().navigateUp()
        }
    }

    override fun onStart() {
        super.onStart()

        noSetsSnackbar = Snackbar.make(
            requireView(),
            getString(R.string.error_no_workout_sets),
            Snackbar.LENGTH_SHORT
        )
    }


}
