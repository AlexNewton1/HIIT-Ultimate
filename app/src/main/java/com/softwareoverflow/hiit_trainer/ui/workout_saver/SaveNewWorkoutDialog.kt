package com.softwareoverflow.hiit_trainer.ui.workout_saver

import android.graphics.Color
import android.graphics.Point
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.snackbar.Snackbar
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.databinding.DialogSaveNewBinding
import com.softwareoverflow.hiit_trainer.ui.dpToPx
import com.softwareoverflow.hiit_trainer.ui.workout_creator.WorkoutCreatorViewModel
import kotlinx.android.synthetic.main.dialog_save_new.*


class SaveNewWorkoutDialog : DialogFragment() {

    private val workoutViewModel: WorkoutCreatorViewModel by navGraphViewModels(R.id.nav_workout_creator)

    private val viewModel: WorkoutSaverViewModel by navGraphViewModels(R.id.saveNewWorkoutDialog) {
        WorkoutSaverViewModelFactory(requireContext(), workoutViewModel.workout.value!!, true)
    }

    private lateinit var emptyNameWarning: Snackbar
    private var viewGroup: ViewGroup? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewGroup = container

        val binding = DataBindingUtil.inflate<DialogSaveNewBinding>(
            inflater,
            R.layout.dialog_save_new,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

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
                    "Workout '${viewModel.workoutName.value}' saved.",
                    Snackbar.LENGTH_SHORT
                ).show()
                findNavController().navigateUp()
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val displaySize = Point()
        requireActivity().windowManager.defaultDisplay.getRealSize(displaySize)

        val window = requireDialog().window
        window?.setLayout(displaySize.x - 32.dpToPx, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        parentFragment?.view?.alpha = 0.5f

        val fadeColor = circularIconImageView.getColor()
        requireView().background.colorFilter =
            PorterDuffColorFilter(fadeColor, PorterDuff.Mode.SRC_IN)
        requireView().background.alpha = 75

        // TODO string resource
        emptyNameWarning = Snackbar.make(
            parentFragment?.view ?: requireView(),
            "Please enter a workout name",
            Snackbar.LENGTH_SHORT
        )
    }

    override fun onStop() {
        super.onStop()

        parentFragment?.view?.alpha = 1f
    }
}