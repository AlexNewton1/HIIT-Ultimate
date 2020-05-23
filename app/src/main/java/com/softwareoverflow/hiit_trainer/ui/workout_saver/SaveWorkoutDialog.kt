package com.softwareoverflow.hiit_trainer.ui.workout_saver

import android.graphics.Color
import android.graphics.Point
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.snackbar.Snackbar
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.ui.dpToPx
import com.softwareoverflow.hiit_trainer.ui.workout_creator.WorkoutCreatorViewModel


abstract class SaveWorkoutDialog : DialogFragment() {

    internal val workoutViewModel: WorkoutCreatorViewModel by navGraphViewModels(R.id.nav_workout_creator)
    internal val viewModel by lazy { getWorkoutSaverViewModel() }

    internal lateinit var binding: ViewDataBinding

    private val fadeColor = Color.parseColor("#008577")
    private lateinit var emptyNameWarning: Snackbar


    override fun onResume() {
        super.onResume()

        val displaySize = Point()
        requireActivity().windowManager.defaultDisplay.getRealSize(displaySize)

        val window = requireDialog().window
        window?.setLayout(displaySize.x - 32.dpToPx, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        parentFragment?.view?.alpha = 0.5f

        binding.root.background.colorFilter =
            PorterDuffColorFilter(fadeColor, PorterDuff.Mode.SRC_IN)
        binding.root.background.alpha = 100

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

    override fun onStop() {
        super.onStop()

        parentFragment?.view?.alpha = 1f
    }
}