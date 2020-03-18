package com.softwareoverflow.hiit_trainer.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.softwareoverflow.hiit_trainer.R
import kotlinx.android.synthetic.main.fragment_home_screen.*


class HomeScreenFragment : Fragment() {

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createNewWorkoutButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_homeScreenFragment_to_workoutCreatorHomeFragment))
    }
}
