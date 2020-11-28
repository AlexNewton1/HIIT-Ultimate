package com.softwareoverflow.hiit_trainer.ui.workout_saver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.google.android.material.snackbar.Snackbar
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.databinding.DialogSaveNewWorkoutBinding

class SaveNewWorkoutDialog : SaveWorkoutDialog() {

    private val args: SaveNewWorkoutDialogArgs by navArgs()

    private val saverViewModel by navGraphViewModels<WorkoutSaverViewModel>(R.id.saveNewWorkoutDialog) {
        WorkoutSaverViewModelFactory(requireActivity().application, requireContext(), args.dto, true)
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

        return binding.root
    }

    override fun getWorkoutSaverViewModel() = saverViewModel
}