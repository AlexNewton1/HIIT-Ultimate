package com.softwareoverflow.hiit_trainer.ui.workout_creator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.databinding.FragmentExerciseTypeCreatorBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_exercise_type_creator.*

// TODO - move this out of workout_creator package. Not really directly related to creating a workout
class ExerciseTypeCreatorFragment : Fragment() {

    private lateinit var viewModel: ExerciseTypeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // TODO - probably change this to binding layout so it can auto-select the correct items and populate the correct name
        val binding: FragmentExerciseTypeCreatorBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_exercise_type_creator,
            container,
            false
        )

        // TODO - pass in the correct Id
        val viewModelFactory = ExerciseTypeViewModelFactory(activity!!, null)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ExerciseTypeViewModel::class.java)

        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        activity!!.mainActivityFAB.setImageResource(R.drawable.icon_tick)
        activity!!.mainActivityFAB.show()
        activity?.mainActivityFAB?.setOnClickListener {
            if (etExerciseTypeName.text.toString().isBlank()) {
                Snackbar.make(
                    etExerciseTypeName,
                    R.string.error_exercise_type_name_required,
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                viewModel.createOrUpdateExerciseType(
                    activity!!,
                    etExerciseTypeName.text.toString(),
                    etViewPagerPicker.getIconId(),
                    etViewPagerPicker.getColorId()
                )

                findNavController().navigateUp()
                // TODO handle animation of button and pass the Id of the created ID back up
            }
        }
    }
}
