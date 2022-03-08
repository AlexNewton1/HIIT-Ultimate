package com.softwareoverflow.hiit_trainer.ui.workout_saver

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.ui.view.FadedDialogBase

abstract class SaveWorkoutDialog : FadedDialogBase() {

    internal val viewModel by lazy { getWorkoutSaverViewModel() }
    internal lateinit var binding: ViewDataBinding

    private lateinit var emptyNameWarning: Snackbar

    override fun onResume() {
        super.onResume()

        emptyNameWarning = Snackbar.make(
            parentFragment?.view ?: requireView(),
            R.string.error_name_required,
            Snackbar.LENGTH_SHORT
        )

        viewModel.emptyNameWarning.observe(viewLifecycleOwner, Observer {
            if (it) {
                emptyNameWarning.show()
                viewModel.emptyNameWarningShown()
            }
        })

        viewModel.nameTooLongWarning.observe(viewLifecycleOwner, Observer {
            if (it) {
               Snackbar.make(
                    parentFragment?.view ?: requireView(),
                    getString(R.string.name_too_long_warning),
                    Snackbar.LENGTH_SHORT
                ).show()

                viewModel.nameTooLongWarningShown()
            }
        })

        viewModel.workoutSaved.observe(viewLifecycleOwner, Observer {
            if (it) {
                Snackbar.make(
                    requireParentFragment().requireView(),
                    requireActivity().applicationContext.getString(
                        R.string.workout_saved,
                        viewModel.newWorkoutName.value
                    ),
                    Snackbar.LENGTH_SHORT
                ).show()
                findNavController().navigateUp()
            }
        })
    }

    abstract fun getWorkoutSaverViewModel(): WorkoutSaverViewModel

    override fun isCancelable(): Boolean {
        return viewModel.noWorkoutSlotsRemainingWarning.value == false
    }
}


