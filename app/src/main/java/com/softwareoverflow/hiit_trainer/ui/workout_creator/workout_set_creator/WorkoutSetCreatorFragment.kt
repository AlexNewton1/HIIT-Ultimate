package com.softwareoverflow.hiit_trainer.ui.workout_creator


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.softwareoverflow.hiit_trainer.R
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class WorkoutSetCreatorFragment : Fragment() {

    private lateinit var viewModel: WorkoutCreatorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_workout_set_creator, container, false)

        // TODO - update this to actually get and send the correct ID
        val viewModelFactory = WorkoutCreatorViewModelFactory(activity!!, null)
        // Ensure to use the activity as the lifecycle owner, not the fragment as this view model
        // is shared across multiple fragments
        viewModel =
            ViewModelProvider(activity!!, viewModelFactory).get(WorkoutCreatorViewModel::class.java)

        Timber.d(viewModel.hashCode().toString())

        return view
    }

    override fun onStart() {
        super.onStart()

        activity!!.mainActivityFAB.show()
        activity!!.mainActivityFAB.setOnClickListener { v: View ->
            findNavController().navigate(R.id.action_workoutSetCreator_to_workoutCreatorHomeFragment)
            // TODO - start FAB animation back to add for the previous screen!
        }
    }


}
 