package com.softwareoverflow.hiit_trainer.ui.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.snackbar.Snackbar
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.databinding.DialogRepeatWorkoutBinding
import com.softwareoverflow.hiit_trainer.ui.view.FadedDialogBase
import com.softwareoverflow.hiit_trainer.ui.workout_creator.WorkoutCreatorViewModel
import kotlinx.android.synthetic.main.dialog_repeat_workout.*

class RepeatWorkoutDialog : FadedDialogBase() {

    private val viewModel: WorkoutCreatorViewModel by navGraphViewModels(R.id.nav_workout_creator)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<DialogRepeatWorkoutBinding>(
            inflater,
            R.layout.dialog_repeat_workout,
            container,
            false
        )

        binding.workoutNumRepsPicker.minValue = 1
        binding.workoutNumRepsPicker.maxValue = 10

        binding.workoutNumRepsPicker.value = viewModel.workout.value!!.numReps
        binding.workoutRecoveryTime.setText(viewModel.workout.value!!.recoveryTime.toString())

        binding.saveButton.setOnClickListener {
            val repeatCount = workoutNumRepsPicker.value
            val recovery = workoutRecoveryTime.text.toString()

            if (recovery.isBlank()) {
                Snackbar.make(
                    requireParentFragment().requireView(),
                    getString(R.string.missing_values),
                    Snackbar.LENGTH_SHORT
                ).show()

            } else {
                viewModel.setRepeatCount(repeatCount, recovery.toInt())
                findNavController().navigateUp()
            }
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}