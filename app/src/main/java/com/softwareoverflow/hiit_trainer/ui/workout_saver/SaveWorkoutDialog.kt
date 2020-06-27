package com.softwareoverflow.hiit_trainer.ui.workout_saver

import android.view.WindowManager
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.ui.FadedDialogBase

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

        val noWorkoutSlots = Snackbar.make(
            parentFragment?.view ?: requireView(),
            getString(R.string.no_free_workout_slots_warning),
            Snackbar.LENGTH_LONG
        ).setAction(R.string.upgrade) {
            viewModel.upgrade(requireActivity())
        }.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)

                viewModel.noWorkoutSlotsWarningShown()
            }
        })

        viewModel.noWorkoutSlotsRemainingWarning.observe(viewLifecycleOwner, Observer {
            if (it) {
                noWorkoutSlots.show()
            }

            it?.let {
                val window = requireDialog().window
                if (it) window?.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
                else window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
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


