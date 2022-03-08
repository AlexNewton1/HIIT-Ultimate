package com.softwareoverflow.hiit_trainer.ui.workout_creator.workout_set_creator


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.snackbar.Snackbar
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.databinding.FragmentWorkoutSetCreatorStep2Binding
import com.softwareoverflow.hiit_trainer.ui.MainActivity
import com.softwareoverflow.hiit_trainer.ui.workout_creator.WorkoutCreatorViewModel
import kotlinx.android.synthetic.main.fragment_workout_set_creator_step_2.*
import timber.log.Timber

/**
 * Allows user to input values for the [com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO] (e.g. work time, rest time etc)
 */
class WorkoutSetCreatorStep2Fragment : Fragment() {

    // These do not take the corresponding factory, as the view model *SHOULD* always be created by this point
    private val workoutCreatorViewModel: WorkoutCreatorViewModel by navGraphViewModels(R.id.nav_workout_creator)
    private val workoutSetViewModel: WorkoutSetCreatorViewModel by navGraphViewModels(R.id.nav_workout_set_creator)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentWorkoutSetCreatorStep2Binding>(
            inflater, R.layout.fragment_workout_set_creator_step_2, container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = workoutSetViewModel

        if (!workoutCreatorViewModel.isNewWorkoutSet) {
            (requireActivity() as MainActivity).supportActionBar?.title =
                getString(R.string.nav_set_editor_step_1)
        }

        binding.createWorkoutSetButton.setOnClickListener {

            if (workTime.text.isNullOrBlank() || restTime.text.isNullOrEmpty() || numReps.text.isNullOrEmpty() || recoverTime.text.isNullOrEmpty()) {
                Snackbar.make(
                    requireView(),
                    getString(R.string.missing_values),
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (workTime.text.toString().toInt() == 0  || numReps.text.toString().toInt() == 0 ||
                (numReps.text.toString().toInt() > 1 && restTime.text.toString().toInt() == 0)
            ) {
                Snackbar.make(
                    requireView(),
                    getString(R.string.workout_set_values_zero_warning),
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // At the point we try and add this workout set to the workout, it should not be null. Throw NPE if it is ever null
            workoutCreatorViewModel.addOrUpdateWorkoutSet(workoutSetViewModel.workoutSet.value!!)
            findNavController().popBackStack(R.id.workoutCreatorFragment, false)
        }

        binding.numReps.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                try {
                    val restTimeOccurs = p0.toString().toInt() > 1

                    if(restTimeOccurs)
                        restTimeIcon.setImageResource(R.drawable.icon_rest)
                    else
                        restTimeIcon.setImageResource(R.drawable.icon_rest_grey)

                    restTime.isEnabled = restTimeOccurs
                } catch (ex: NumberFormatException) {
                    // Do nothing
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()
        requireActivity().window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Timber.d("SetCreator onCreateOptionsMenu $menu")
        inflater.inflate(R.menu.action_bar_help, menu)

        menu[0].setOnMenuItemClickListener {
            findNavController().navigate(R.id.action_workoutSetCreator_to_workoutSetCreatorHelpFragment)
            true
        }
    }
}
 