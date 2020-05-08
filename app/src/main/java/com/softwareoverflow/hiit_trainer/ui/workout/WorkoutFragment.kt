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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.databinding.FragmentWorkoutBinding
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutDTO
import com.softwareoverflow.hiit_trainer.repository.dto.WorkoutSetDTO
import com.softwareoverflow.hiit_trainer.ui.view.animation.MoveAndScaleAnimationFactory
import kotlinx.android.synthetic.main.fragment_workout.*
import timber.log.Timber

class WorkoutFragment : Fragment(), IWorkoutObserver {

    private val args: WorkoutFragmentArgs by navArgs()

    private val viewModel: WorkoutViewModel by viewModels() {
        WorkoutViewModelFactory(requireActivity().application, requireContext(), args.workoutId)
    }

    private var timer: WorkoutTimer? = null

    private lateinit var currentSectionLabelAnimation: ObjectAnimator
    private lateinit var scaleAndMoveAnimation: ValueAnimator

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
                    timer = WorkoutTimer(it, this@WorkoutFragment)

                    // We only want to observe when the workout is first initialised as not null. Remove the observer.
                    viewModel.workout.removeObserver(this)
                }
            }
        })

        viewModel.skipSection.observe(viewLifecycleOwner, Observer {
            if (it) {
                Timber.d("Timer: Trying to skip")
                timer?.skip()
                Timber.d("Timer: Skipped")
                viewModel.onSectionSkipped() // Notify the viewModel the section has been skipped
            }
        })

        viewModel.soundOn.observe(viewLifecycleOwner, Observer {
            // TODO handle sound playing, it will probably be through the Timer delegating to a different class?
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

        // TODO come up with a nicer way of handling all the animations and prevent the flashing.
        // TODO also need a way of passing the anim time in for cases when recover time is less than 3s (otherwise animation jumps part way through to end)
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
            }.create(null)
        }

        return binding.root
    }

    override fun onTimerTick(workoutRemaining: Int, workoutSectionRemaining: Int) {
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
        throw NotImplementedError()
    }

    // TODO - Check this does handle cancelling the timer if the user navigates away from the page
    override fun onDestroy() {
        timer?.cancel()
        super.onDestroy()
    }

}
