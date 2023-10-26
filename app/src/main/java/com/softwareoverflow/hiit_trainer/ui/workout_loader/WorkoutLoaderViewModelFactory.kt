package com.softwareoverflow.hiit_trainer.ui.workout_loader

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.softwareoverflow.hiit_trainer.repository.WorkoutRepositoryFactory
import com.softwareoverflow.hiit_trainer.repository.billing.BillingRepository
import com.softwareoverflow.hiit_trainer.ui.view.list_adapter.workout.WorkoutLoaderDomainObject

class WorkoutLoaderViewModelFactory(private val app: Application, private val context: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutLoaderViewModel::class.java)) {
            val billingRepo = BillingRepository.getInstance(app)
            val workoutRepo = WorkoutRepositoryFactory.getInstance(context)

            val placeholderUnlocked = WorkoutLoaderDomainObject.getPlaceholderUnlocked(context.applicationContext)
            val placeholderLocked = WorkoutLoaderDomainObject.getPlaceholderLocked(context.applicationContext)

            return WorkoutLoaderViewModel(billingRepo, workoutRepo, placeholderUnlocked, placeholderLocked) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
