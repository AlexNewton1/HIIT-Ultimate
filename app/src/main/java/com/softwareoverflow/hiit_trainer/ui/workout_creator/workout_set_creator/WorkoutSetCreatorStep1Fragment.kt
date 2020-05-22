package com.softwareoverflow.hiit_trainer.ui.workout_creator.workout_set_creator


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
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
import kotlinx.android.synthetic.main.fragment_workout_set_creator_step_1.*


/**
 * Allows the user to select from the list of saved [com.softwareoverflow.hiit_trainer.repository.dto.ExerciseTypeDTO] objects.
 * Also supports edit / deletion of Exercise Type objects
 */
class WorkoutSetCreatorStep1Fragment : Fragment() {

    private val workoutViewModel: WorkoutCreatorViewModel by navGraphViewModels(R.id.nav_workout_creator)
    private val workoutSetViewModel: WorkoutSetCreatorViewModel by navGraphViewModels(R.id.nav_workout_set_creator)
    {
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

        workoutSetViewModel.unableToDeleteExerciseType.observe(viewLifecycleOwner, Observer {
            if(!it.isNullOrBlank()) {
                val snackbar = Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG)
                (snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)).maxLines = 3
                snackbar.show()

                workoutSetViewModel.unableToDeleteExerciseTypeWarningShown()
            }
        })

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

        binding.workoutSetCreatorGoToStep2Button.setOnClickListener {
            workoutSetViewModel.selectedExerciseTypeId.value?.let {
                workoutSetViewModel.setChosenExerciseTypeId(it)
                findNavController().navigate(R.id.action_exerciseTypePickerFragment_to_workoutSetCreator)
                return@setOnClickListener
            }

            Snackbar.make(requireView(), R.string.select_exercise_type, Snackbar.LENGTH_SHORT).show()
        }

        binding.sortButton.setOnClickListener {
            workoutSetViewModel.changeSortOrder()
        }

        binding.nameSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    workoutSetViewModel.setFilterText(it)
                }
                return true
            }
        })

        return binding.root
    }

    fun createOrEditExerciseType(){
        findNavController().navigate(R.id.action_exerciseTypePickerFragment_to_exerciseTypeCreator)
    }

    override fun onStart() {
        super.onStart()

        hideKeyboard(requireActivity())
    }

    override fun onResume() {
        super.onResume()

        // TODO investigate some from of animation of the items
/*        val lac = LayoutAnimationController(
            AnimationUtils.loadAnimation(
                activity,
                R.anim.up_from_bottom
            ), 0.5f
        ) //0.5f == time between appearance of listview items.

        exerciseTypePickerList.layoutAnimation = lac

        exerciseTypePickerList.startLayoutAnimation()*/

        /*var clickSpacing = 50L
        exerciseTypePickerList.itemAnimator?.let {
            clickSpacing = it.changeDuration
            Timber.d("anim: setting clickSpacing to be $clickSpacing")
        }

        // Cause some animation when loading the page
        lifecycleScope.launch {
            sortButton.callOnClick()
            delay(clickSpacing)
            sortButton.callOnClick()
        }*/
    }
}
