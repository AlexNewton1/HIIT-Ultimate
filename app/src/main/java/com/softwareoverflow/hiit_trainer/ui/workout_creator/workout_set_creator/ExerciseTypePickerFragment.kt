package com.softwareoverflow.hiit_trainer.ui.workout_creator.workout_set_creator


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO
import com.softwareoverflow.hiit_trainer.ui.view.GridListDecoration
import com.softwareoverflow.hiit_trainer.ui.workout_creator.WorkoutCreatorViewModel
import com.softwareoverflow.hiit_trainer.ui.workout_creator.WorkoutCreatorViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_exercise_type_picker.view.*

class ExerciseTypePickerFragment : Fragment() {

    // TODO Not sure this is actually needed? Might be best to use nav arguments to pass data back and forth between fragments...
    private val workoutViewModel: WorkoutCreatorViewModel by navGraphViewModels(R.id.nav_workout_creator) {
        // TODO - update this to actually get and send the correct ID
        WorkoutCreatorViewModelFactory(
            activity!!,
            null
        )
    }

    private val workoutSetViewModel: WorkoutSetCreatorViewModel by navGraphViewModels(R.id.nav_workout_set_creator)
    {
        // TODO this should probably come through the args?
        var workoutSet = workoutViewModel.getWorkoutSetToEdit()
        if (workoutSet == null) {
            workoutSet = WorkoutSetDTO(null, null, 15, 5, 3, 120)
        }

        WorkoutSetCreatorViewModelFactory(workoutSet, context!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_exercise_type_picker, container, false)

        val adapter =
            ExerciseTypePickerListAdapter()
        val list = view.exerciseTypePickerList
        list.adapter = adapter
        list.addItemDecoration(GridListDecoration(view.context))

        workoutSetViewModel.allExerciseTypes.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.submitList(it)
            }
        })

        workoutSetViewModel.exerciseType.observe(viewLifecycleOwner, Observer {
            adapter.notifyItemSelected(it?.id)
        })


        // TODO - bind the viewModel to the UI & / or observe changes in livedata and set the focus on the correct item in the grid

        return view
    }

    override fun onStart() {
        super.onStart()
        activity!!.mainActivityFAB.setImageResource(R.drawable.icon_plus)
        activity!!.mainActivityFAB.show()
        activity?.mainActivityFAB?.setOnClickListener {
            findNavController().navigate(R.id.action_exerciseTypePickerFragment_to_exerciseTypeCreator)
            // TODO handle animation of button
        }
    }
}
