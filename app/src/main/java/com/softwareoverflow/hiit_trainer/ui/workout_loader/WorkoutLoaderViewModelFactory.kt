package com.softwareoverflow.hiit_trainer.ui.workout_loader

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.softwareoverflow.hiit_trainer.repository.WorkoutRepositoryFactory
import com.softwareoverflow.hiit_trainer.repository.billing.BillingRepository

class WorkoutLoaderViewModelFactory(private val app: Application, private val context: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutLoaderViewModel::class.java)) {
            val billingRepo = BillingRepository.getInstance(app)
            val workoutRepo = WorkoutRepositoryFactory.getInstance(context)
            return WorkoutLoaderViewModel(billingRepo, context, workoutRepo) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}