package com.softwareoverflow.hiit_trainer.ui.workout_creator


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.softwareoverflow.hiit_trainer.R
import kotlinx.android.synthetic.main.activity_main.*

// TODO - move this out of workout_creator package. Not really directly related to creating a workout
class ExerciseTypeCreatorFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercise_type_creator, container, false)
    }

    override fun onStart() {
        super.onStart()
        activity!!.mainActivityFAB.setImageResource(R.drawable.icon_tick)
        activity!!.mainActivityFAB.show()
        activity?.mainActivityFAB?.setOnClickListener {
            findNavController().navigateUp()
            // TODO handle animation of button
        }
    }
}
