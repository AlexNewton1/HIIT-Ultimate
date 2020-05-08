package com.softwareoverflow.hiit_trainer.ui.workout_loader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.ISelectableEditableListEventListener
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.SpacedListDecoration
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.workout.SavedWorkoutListAdapter
import kotlinx.android.synthetic.main.fragment_workout_loader.*
import kotlinx.android.synthetic.main.fragment_workout_loader.view.*

class LoadSavedWorkoutFragment : Fragment() {

    val viewModel: WorkoutLoaderViewModel by navGraphViewModels(R.id.loadSavedWorkoutFragment) {
        WorkoutLoaderViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_workout_loader, container, false)

        view.listSavedWorkouts.adapter = SavedWorkoutListAdapter(
            requireContext()
        ).apply {
            setEventListener(object:
                ISelectableEditableListEventListener {
                override fun onItemSelected(selected: Long?) {
                    val action =
                        LoadSavedWorkoutFragmentDirections.actionLoadSavedWorkoutFragmentToWorkoutFragment(selected!!)
                    findNavController().navigate(action)
                }

                override fun triggerItemDeletion(id: Long) {
                    // TODO probably add are you sure dialog
                    viewModel.deleteWorkout(id)
                }

                override fun triggerItemEdit(id: Long) {
                    val action =
                        LoadSavedWorkoutFragmentDirections.actionLoadSavedWorkoutFragmentToNavWorkoutCreator(id)
                    findNavController().navigate(action)
                }
            })
        }

        view.listSavedWorkouts.addItemDecoration(
            SpacedListDecoration(
                requireContext()
            )
        )

        viewModel.workouts.observe(viewLifecycleOwner, Observer {
            // TODO fix having to cast this - it's not very nice!
            it?.let{
                (listSavedWorkouts.adapter as SavedWorkoutListAdapter).submitList(it.toMutableList())
            }
        })
        return view
    }
}