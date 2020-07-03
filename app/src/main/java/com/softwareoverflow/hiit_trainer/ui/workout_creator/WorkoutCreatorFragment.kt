package com.softwareoverflow.hiit_trainer.ui.workout_creator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.google.android.material.snackbar.Snackbar
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.ui.MainActivity
import com.softwareoverflow.hiit_trainer.ui.upgrade.AdsManager
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

        val view = inflater.inflate(R.layout.fragment_workout_creator, container, false)

        view.listWorkoutSets.adapter =
            WorkoutSetListAdapter()
        view.listWorkoutSets.addItemDecoration(
            SpacedListDecoration(
                requireContext()
            )
        )

        (view.listWorkoutSets.adapter as WorkoutSetListAdapter).setEventListener(object :
            IEditableOrderedListEventListener {

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
                findNavController().navigate(R.id.action_workoutCreatorHomeFragment_to_exerciseTypePickerFragment)

            }
        })

        viewModel.workout.observe(viewLifecycleOwner, Observer {
            it?.let {
                (listWorkoutSets.adapter as WorkoutSetListAdapter).submitList(it.workoutSets.toMutableList()) // Call toMutableList as otherwise the list does not update when only the order of elements has changed

                if (it.workoutSets.isEmpty())
                    noSetsTextHint.visibility = View.VISIBLE
                else
                    noSetsTextHint.visibility = View.GONE

                (requireActivity() as MainActivity).supportActionBar?.title =
                    if (it.name.isBlank())
                        getString(R.string.create_your_workout)
                    else
                        getString(R.string.edit_your_workout, it.name)
            }
        })

        view.createNewWorkoutSetButton.setOnClickListener {
            viewModel.setWorkoutSetToEdit(null)

            // If the snackbar for no remaining save slots is showing, clicks are registered on the screen.
            // This causes IllegalArgumentException as the save workout dialog is still on screen. Catch these exceptions and do nothing.
            try {
                findNavController().navigate(R.id.action_workoutCreatorHomeFragment_to_exerciseTypePickerFragment)
            } catch (ex: IllegalArgumentException) {
                // Do nothing
            }
        }

        view.startWorkoutButton.setOnClickListener {
            if (viewModel.workout.value!!.workoutSets.isEmpty())
                noSetsSnackbar.show()
            else {
                val action =
                    WorkoutCreatorFragmentDirections.actionWorkoutCreatorFragmentToWorkoutFragment(
                        workoutDto = viewModel.workout.value
                    )

                // Show an advert and then naviage to the workout
                AdsManager.showAdBeforeWorkout {
                    findNavController().navigate(action)
                }
            }
        }

        view.saveSpeedDial.setMenuListener(object : SimpleMenuListenerAdapter() {
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (viewModel.workout.value!!.workoutSets.isEmpty()) {
                    noSetsSnackbar.show()
                    return false
                }

                if (menuItem.title == requireContext().getString(R.string.save_as)) {
                    val action =
                        WorkoutCreatorFragmentDirections.actionWorkoutCreatorFragmentToSaveNewWorkoutDialog(
                            workoutDto = viewModel.workout.value!!
                        )
                    findNavController().navigate(action)
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
                            workoutDto = viewModel.workout.value!!
                        )
                    findNavController().navigate(action)
                }

                return false
            }
        }
        )

        return view
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
