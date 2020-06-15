package com.softwareoverflow.hiit_trainer.ui.workout_saver

import android.app.Activity
import android.content.Context
import android.view.WindowManager
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.snackbar.Snackbar
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.ui.FadedDialogBase
import com.softwareoverflow.hiit_trainer.ui.workout_creator.WorkoutCreatorViewModel
import timber.log.Timber
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

        val noWorkoutSlots = Snackbar.make(
            parentFragment?.view ?: requireView(),
            "No free workout slots remaining. Overwrite an existing workout or upgrade to PRO.",
            Snackbar.LENGTH_LONG
        ).setAction("Upgrade") {
            Timber.d("UPGRADE: its clicked")
            viewModel.upgrade(requireActivity())
        }.addCallback(object: Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)

                viewModel.noWorkoutSlotsWarningShown()
            }
        })
        viewModel.noWorkoutSlotsRemainingWarning.observe(viewLifecycleOwner, Observer {
            if (it) {
                noWorkoutSlots.show()
            }

            it?.let{
                // Not cancellable while showing upgrade snackbar
                //requireDialog().setCanceledOnTouchOutside(!it)

                val window = requireDialog().window
                if(it) window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                else window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
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

    override fun isCancelable(): Boolean{
        return viewModel.noWorkoutSlotsRemainingWarning.value == false
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }


}


