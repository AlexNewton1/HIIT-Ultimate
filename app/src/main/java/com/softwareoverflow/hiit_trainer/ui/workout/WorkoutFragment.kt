package com.softwareoverflow.hiit_trainer.ui.workout

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.doOnLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.databinding.FragmentWorkoutBinding
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO
import com.softwareoverflow.hiit_trainer.ui.getDuration
import com.softwareoverflow.hiit_trainer.ui.getFullWorkoutSets
import com.softwareoverflow.hiit_trainer.ui.getWorkoutPrepSet
import com.softwareoverflow.hiit_trainer.ui.upgrade.AdsManager
import com.softwareoverflow.hiit_trainer.ui.view.animation.MoveAndScaleAnimationFactory
import kotlinx.android.synthetic.main.fragment_workout.*
import timber.log.Timber

class WorkoutFragment : Fragment(), IWorkoutObserver {

    private val args: WorkoutFragmentArgs by navArgs()

    private val viewModel: WorkoutViewModel by navGraphViewModels(R.id.workoutFragment) {
        WorkoutViewModelFactory(
            requireActivity().application,
            requireContext(),
            args.workoutId,
            args.dto
        )
    }

    private var timer: WorkoutTimer? = null

    private lateinit var currentSectionLabelAnimation: ObjectAnimator
    private lateinit var scaleAndMoveAnimation: ValueAnimator

    private var isWorkoutFinished = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentWorkoutBinding>(
            inflater,
            R.layout.fragment_workout,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.workout.observe(viewLifecycleOwner, object : Observer<WorkoutDTO> {
            override fun onChanged(dto: WorkoutDTO?) {
                dto?.let {
                    var duration = it.getDuration()
                    getWorkoutPrepSet(requireContext())?.let { prepSet ->
                        duration += prepSet.workTime!!
                    }

                    timer = WorkoutTimer(
                        requireContext(),
                        duration, it.getFullWorkoutSets(requireContext()), this@WorkoutFragment
                    )
                    timer!!.start()

                    // We only want to observe when the workout is first initialised as not null. Remove the observer.
                    viewModel.workout.removeObserver(this)
                }
            }
        })

        viewModel.skipSection.observe(viewLifecycleOwner, Observer {
            if (it) {
                timer?.skip()
                viewModel.onSectionSkipped() // Notify the viewModel the section has been skipped
            }
        })

        viewModel.soundOn.observe(viewLifecycleOwner, Observer {
            timer?.toggleSound(it)
        })

        viewModel.isPaused.observe(viewLifecycleOwner, Observer {
            timer?.togglePause(it)
        })

        currentSectionLabelAnimation =
            ObjectAnimator.ofFloat(binding.workoutSectionLabelText, "alpha", 1f, 0f).apply {
                duration = 1250
                startDelay = 750
                interpolator = AccelerateDecelerateInterpolator()
            }

        viewModel.animateUpNextExerciseType.observe(viewLifecycleOwner, Observer {
            if (it) {
                scaleAndMoveAnimation.start()
            }
        })

        // TODO FUTURE VERSION Maybe a way of passing the anim time in for cases when recover time is less than 3s (otherwise animation jumps part way through to end). This use case of recovery < 3s seems unlikely so not a priority
        binding.root.doOnLayout {
            scaleAndMoveAnimation = MoveAndScaleAnimationFactory().apply {
                setDuration(3000L)
                setTextViewToScale(binding.upNextExerciseTypeName)
                setViewToScale(binding.upNextExerciseTypeView)
                setMoveY(binding.upNextExerciseTypeView.y, binding.currentExerciseTypeIcon.y)
                setScaleHeight(
                    binding.upNextExerciseTypeView.height,
                    binding.currentExerciseTypeIcon.height
                )
                setScaleText(
                    binding.upNextExerciseTypeName.textSize,
                    binding.currentExerciseTypeName.textSize
                )
                setAlphaAnimation(binding.currentExerciseTypeView, 1f, 0f)
            }.create() {
            }
        }

        return binding.root
    }

    override fun onTimerTick(workoutRemaining: Int, workoutSectionRemaining: Int) {
        Timber.d("Timer: WorkoutFragment: $workoutRemaining")
        viewModel.updateTimers(workoutRemaining, workoutSectionRemaining)
    }

    override fun onWorkoutSectionChange(
        section: WorkoutSection,
        currentSet: WorkoutSetDTO,
        currentRep: Int
    ) {
        viewModel.updateValues(section, currentSet, currentRep)

        workoutSectionLabelText?.alpha = 1f
        currentSectionLabelAnimation.start()
    }

    override fun onFinish() {
        if (!isWorkoutFinished) {
            isWorkoutFinished = true

            timer?.cancel()

            val action =
                WorkoutFragmentDirections.actionWorkoutFragmentToWorkoutCompleteFragment(viewModel.workout.value!!, args.workoutId)
            AdsManager.showAdAfterWorkout() {
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroy() {
        timer?.cancel()
        super.onDestroy()
    }
}
