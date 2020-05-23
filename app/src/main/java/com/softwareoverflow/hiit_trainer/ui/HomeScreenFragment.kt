package com.softwareoverflow.hiit_trainer.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.softwareoverflow.hiit_trainer.R
import com.softwareoverflow.hiit_trainer.databinding.FragmentHomeScreenBinding
import kotlinx.android.synthetic.main.fragment_home_screen.*


class HomeScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentHomeScreenBinding>(inflater, R.layout.fragment_home_screen, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createNewWorkoutButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_homeScreenFragment_to_workoutCreatorHomeFragment
            )
        )

        loadSavedWorkoutsButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_homeScreenFragment_to_loadSavedWorkoutFragment
            )
        )
    }
}
