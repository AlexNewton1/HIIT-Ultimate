package com.softwareoverflow.hiit_trainer.ui.workout_creator.workout_set_creator


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.databinding.FragmentWorkoutSetCreatorStep1Binding
import com.softwareoverflow.hiit_trainer.ui.hideKeyboard
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.ISelectableEditableListEventListener
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.SpacedListDecoration
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.exercise_type.ExerciseTypePickerListAdapter
import com.softwareoverflow.hiit_trainer.ui.workout_creator.WorkoutCreatorViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_workout_set_creator_step_1.*

/**
 * Allows the user to select from the list of saved [com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO] objects.
 */
class WorkoutSetCreatorStep1Fragment : Fragment() {

    // TODO Not sure this is actually needed? Might be best to use nav arguments to pass data back and forth between these fragments...
    private val workoutViewModel: WorkoutCreatorViewModel by navGraphViewModels(R.id.nav_workout_creator)

    private val workoutSetViewModel: WorkoutSetCreatorViewModel by navGraphViewModels(R.id.nav_workout_set_creator)
    {
        // TODO pass in the correct ID - this should probably come through the args?
        WorkoutSetCreatorViewModelFactory(workoutViewModel.workoutSet, requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentWorkoutSetCreatorStep1Binding>(
            inflater, R.layout.fragment_workout_set_creator_step_1, container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = workoutSetViewModel

        binding.exerciseTypePickerList.apply {
            adapter = ExerciseTypePickerListAdapter(object:
                ISelectableEditableListEventListener {
                override fun onItemSelected(selected: Long?) {
                    workoutSetViewModel.selectedExerciseTypeId.value  = selected
                }

                override fun triggerItemDeletion(id: Long) {
                    workoutSetViewModel.selectedExerciseTypeId.value = null
                    workoutSetViewModel.deleteExerciseTypeById(id)
                }

                override fun triggerItemEdit(id: Long) {
                    workoutSetViewModel.selectedExerciseTypeId.value = id
                    createOrEditExerciseType()
                }
            })

            addItemDecoration(
                SpacedListDecoration(
                    context,
                    (this.layoutManager as GridLayoutManager).spanCount
                )
            )
        }

        workoutSetViewModel.selectedExerciseTypeId.observe(viewLifecycleOwner, Observer {
            it?.let{
                (exerciseTypePickerList.adapter as ExerciseTypePickerListAdapter).notifyItemSelected(it)
            }
        })

        workoutSetViewModel.allExerciseTypes.observe(viewLifecycleOwner, Observer {
            it?.let{
                val adapter = (exerciseTypePickerList.adapter as ExerciseTypePickerListAdapter)
                adapter.submitDTOs(it){
                    workoutSetViewModel.selectedExerciseTypeId.value?.let {
                        adapter.notifyItemSelected(it)
                    }
                }
            }
        })


        binding.createNewExerciseTypeFAB.setOnClickListener {
            workoutSetViewModel.selectedExerciseTypeId.value = null
            createOrEditExerciseType()
        }

        return binding.root
    }

    fun createOrEditExerciseType(){
        findNavController().navigate(R.id.action_exerciseTypePickerFragment_to_exerciseTypeCreator)
    }

    override fun onStart() {
        super.onStart()
        requireActivity().mainActivityFAB.setImageResource(R.drawable.icon_arrow_right)
        requireActivity().mainActivityFAB.show()
        activity?.mainActivityFAB?.setOnClickListener {

            workoutSetViewModel.selectedExerciseTypeId.value?.let {
                workoutSetViewModel.setChosenExerciseTypeId(it)
                findNavController().navigate(R.id.action_exerciseTypePickerFragment_to_workoutSetCreator)
                return@setOnClickListener
            }

            Snackbar.make(requireView(), R.string.select_exercise_type, Snackbar.LENGTH_SHORT).show()

            // TODO handle animation of button
        }

        hideKeyboard(requireActivity())
    }
}
