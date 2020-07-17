package com.softwareoverflow.hiit_trainer.ui.workout_creator.workout_set_creator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.databinding.DialogHelpWorkoutSetCreatorBinding
import com.softwareoverflow.hiit_trainer.ui.FadedDialogBase

class WorkoutSetCreatorHelpFragment : FadedDialogBase() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<DialogHelpWorkoutSetCreatorBinding>(
            inflater,
            R.layout.dialog_help_workout_set_creator,
            container,
            false
        )
        binding.lifecycleOwner = this

        binding.closeButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}