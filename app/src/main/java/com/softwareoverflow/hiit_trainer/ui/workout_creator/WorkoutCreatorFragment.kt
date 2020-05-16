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

                // TODO string resource these
                (requireActivity() as MainActivity).supportActionBar?.title =
                    if (it.name.isBlank())
                        "Create Your Workout"
                    else
                        "Edit Workout '${it.name}'"
            }
        })

        view.createNewWorkoutSetButton.setOnClickListener {
            viewModel.setWorkoutSetToEdit(null)
            // TODO fix this breaking if going back to home fragment and then trying to create workout again
            findNavController().navigate(R.id.action_workoutCreatorHomeFragment_to_exerciseTypePickerFragment)
        }

        view.startWorkoutButton.setOnClickListener {
            if (viewModel.workout.value!!.workoutSets.isEmpty())
                noSetsSnackbar.show()
            // TODO - pass the workout to the workout
            /*else
                findNavController().navigate(R.id.action_workout)*/
        }

        view.saveSpeedDial.setMenuListener(object: SimpleMenuListenerAdapter() {
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if(menuItem.title == requireContext().getString(R.string.save_as)) {
                    if(viewModel.workout.value!!.workoutSets.isEmpty())
                        noSetsSnackbar.show()
                    else
                        findNavController().navigate(R.id.action_workoutCreatorFragment_to_saveNewWorkoutDialog)
                }
                /*else if (menuItem.itemId == R.id.menu_save_overwrite)
                    findNavController().navigate()*/

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
            // TODO string resource
            "Please add at least one Workout Set to your Workout",
            Snackbar.LENGTH_SHORT
        )
    }
}
