package com.softwareoverflow.hiit_trainer.ui.workout_saver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.databinding.DialogSaveNewWorkoutBinding

class SaveNewWorkoutDialog : SaveWorkoutDialog() {

    private val saverViewModel by navGraphViewModels<WorkoutSaverViewModel>(R.id.saveNewWorkoutDialog) {
        WorkoutSaverViewModelFactory(requireContext(), workoutViewModel.workout.value!!, true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<DialogSaveNewWorkoutBinding>(
            inflater,
            R.layout.dialog_save_new_workout,
            container,
            false
        ) .apply {
            lifecycleOwner = this@SaveNewWorkoutDialog
            viewModel = viewModel

            cancelButton.setOnClickListener {
                findNavController().navigateUp()
            }
        }

        return binding.root
    }

    override fun getWorkoutSaverViewModel() = saverViewModel
}