package com.softwareoverflow.hiit_trainer.ui.workout_creator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.snackbar.Snackbar
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.ui.view.IEditableOrderedListEventListener
import com.softwareoverflow.hiit_trainer.ui.view.exercise_type_picker.GridListDecoration
import com.softwareoverflow.hiit_trainer.ui.view.workout_set.WorkoutSetListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_workout_creator_step_1.*
import kotlinx.android.synthetic.main.fragment_workout_creator_step_1.view.*
import timber.log.Timber

class WorkoutCreatorStep1Fragment : Fragment() {

    private val viewModel: WorkoutCreatorViewModel by navGraphViewModels(R.id.nav_workout_creator) {
        Timber.d("Workout creating WorkoutCreatorViewModel")
        WorkoutCreatorViewModelFactory(
            requireActivity(),
            1
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_workout_creator_step_1, container, false)

        view.listWorkoutSets.adapter = WorkoutSetListAdapter()
        view.listWorkoutSets.addItemDecoration(GridListDecoration(requireContext(), 1))

        (view.listWorkoutSets.adapter as WorkoutSetListAdapter).setEventListener(object: IEditableOrderedListEventListener{

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
                (listWorkoutSets.adapter as WorkoutSetListAdapter).submitList(it.workoutSets)
            }
        })

        view.createNewWorkoutSetButton.setOnClickListener {
            // TODO fix this breaking if going back to home fragment and then trying to create workout again
            findNavController().navigate(R.id.action_workoutCreatorHomeFragment_to_exerciseTypePickerFragment)
        }

        return view
    }

    override fun onStart() {
        super.onStart()

        activity?.mainActivityFAB?.show()
        activity?.mainActivityFAB?.setImageResource(R.drawable.icon_arrow_right)
        activity?.mainActivityFAB?.setOnClickListener {
            if(viewModel.workout.value!!.workoutSets.isEmpty())
                Snackbar.make(requireView(), "Please add at least one Workout Set to your Workout", Snackbar.LENGTH_SHORT).show()
            else
                findNavController().navigate(R.id.action_workoutCreatorHomeFragment_to_workoutCreatorStep2Fragment)
        }
    }

    override fun onStop() {
        super.onStop()

        // TODO work out if this is needed, as it seems to happen after the listener gets set in the next fragment :o
        //activity?.mainActivityFAB?.setOnClickListener(null)
    }
}
