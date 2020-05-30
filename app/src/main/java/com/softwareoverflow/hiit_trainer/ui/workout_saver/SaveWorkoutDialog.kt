package com.softwareoverflow.hiit_trainer.ui.workout_saver

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.snackbar.Snackbar
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.ui.FadedDialogBase
import com.softwareoverflow.hiit_trainer.ui.workout_creator.WorkoutCreatorViewModel


abstract class SaveWorkoutDialog : FadedDialogBase() {

    internal val workoutViewModel: WorkoutCreatorViewModel by navGraphViewModels(R.id.nav_workout_creator)
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
}