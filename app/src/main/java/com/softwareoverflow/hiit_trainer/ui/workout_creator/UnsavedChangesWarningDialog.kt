package com.softwareoverflow.hiit_trainer.ui.workout_creator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.preference.PreferenceManager
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.databinding.DialogUnsavedChangesWarningBinding
import com.softwareoverflow.hiit_trainer.ui.view.FadedDialogBase
import kotlinx.android.synthetic.main.dialog_unsaved_changes_warning.*

class UnsavedChangesWarningDialog : FadedDialogBase() {

    private val viewModel: WorkoutCreatorViewModel by navGraphViewModels(R.id.nav_workout_creator)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<DialogUnsavedChangesWarningBinding>(
            inflater,
            R.layout.dialog_unsaved_changes_warning,
            container,
            false
        )
        binding.lifecycleOwner = this
        isCancelable = false

        requireDialog().setCanceledOnTouchOutside(false)
        requireDialog().setCancelable(false)

        binding.continueButton.setOnClickListener {
            PreferenceManager.getDefaultSharedPreferences(requireContext().applicationContext)
                .edit()
                .putBoolean(getString(R.string.pref_unsaved_changes_warning), !checkbox.isChecked)
                .apply()

            findNavController().navigateUp()

            viewModel.setUnsavedChangesWarningAccepted()
            viewModel.forceNavigateUp()
        }

        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

}