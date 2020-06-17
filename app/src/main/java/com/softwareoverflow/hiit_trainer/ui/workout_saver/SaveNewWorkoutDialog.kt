package com.softwareoverflow.hiit_trainer.ui.workout_saver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.databinding.DialogSaveNewWorkoutBinding

class SaveNewWorkoutDialog : SaveWorkoutDialog() {

    private val args: SaveNewWorkoutDialogArgs by navArgs()

    private val saverViewModel by navGraphViewModels<WorkoutSaverViewModel>(R.id.saveNewWorkoutDialog) {
        WorkoutSaverViewModelFactory(requireActivity().application, requireContext(), args.workoutDto, true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<DialogSaveNewWorkoutBinding>(
            inflater,
            R.layout.dialog_save_new_workout,
            container,
            false
        ) .apply {
            cancelButton.setOnClickListener {
                findNavController().navigateUp()
            }

            saveButton.setOnClickListener {
                saverViewModel.saveWorkout()
            }
        }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun getWorkoutSaverViewModel() = saverViewModel
}