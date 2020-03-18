package com.softwareoverflow.hiit_trainer.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.softwareoverflow.hiit_trainer.R

class WorkoutCreatorHomeFragment : Fragment() {

    companion object {
        fun newInstance() = WorkoutCreatorHomeFragment()
    }

    private lateinit var viewModel: WorkoutCreatorHomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_workout_creator_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WorkoutCreatorHomeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
