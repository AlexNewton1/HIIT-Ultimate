package com.softwareoverflow.hiit_trainer.ui.workout_creator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doBeforeTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.snackbar.Snackbar
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.databinding.FragmentExerciseTypeCreatorBinding
import com.softwareoverflow.hiit_trainer.ui.workout_creator.workout_set_creator.WorkoutSetCreatorViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_exercise_type_creator.*

// TODO - move this out of workout_creator package. Not really directly related to creating a workout
class ExerciseTypeCreatorFragment : Fragment() {

    private lateinit var viewModel: ExerciseTypeViewModel

    private var snackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // TODO - probably change this to binding layout so it can auto-select the correct items and populate the correct name
        val binding: FragmentExerciseTypeCreatorBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_exercise_type_creator,
            container,
            false
        )

        // TODO - pass in the correct Id
        // This does not take the corresponding factory, as the view model *SHOULD* always be created by this point
        val workoutSetViewModel: WorkoutSetCreatorViewModel by navGraphViewModels(R.id.nav_workout_set_creator)
        val viewModelFactory = ExerciseTypeViewModelFactory(
            requireActivity(),
            workoutSetViewModel.selectedExerciseTypeId.value,
            workoutSetViewModel
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(ExerciseTypeViewModel::class.java)
        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        // TODO - fix this issue
        val etNameMaxLength = requireActivity().resources.getInteger(R.integer.et_name_length_max)
        binding.etExerciseTypeName.apply {
            doBeforeTextChanged { text, start, count, after ->
                if (etExerciseTypeName.length() >= etNameMaxLength && snackbar?.isShown != true)
                    snackbar?.show()
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        if (snackbar == null) {
            snackbar = Snackbar.make(
                etExerciseTypeName,
                requireActivity().applicationContext.getString(
                    R.string.char_limit_exceeded,
                    context?.resources?.getInteger(R.integer.et_name_length_max)
                ),
                Snackbar.LENGTH_SHORT
            ).setAction(R.string.dismiss) {
                snackbar?.dismiss()
            }
        }

        requireActivity().mainActivityFAB.setImageResource(R.drawable.icon_tick)
        requireActivity().mainActivityFAB.show()
        activity?.mainActivityFAB?.setOnClickListener {
            if (etExerciseTypeName.text.toString().isBlank()) {
                Snackbar.make(
                    etExerciseTypeName,
                    R.string.error_exercise_type_name_required,
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                viewModel.createOrUpdateExerciseType(
                    requireActivity(),
                    etExerciseTypeName.text.toString(),
                    etViewPagerPicker.getIconId(),
                    etViewPagerPicker.getColorId()
                )

                findNavController().navigateUp()
                // TODO handle animation of button and pass the Id of the created ID back up
            }
        }
    }
}
