package com.softwareoverflow.hiit_trainer.ui.injection

import android.app.Application
import android.content.Context
import com.softwareoverflow.hiit_trainer.repository.IWorkoutRepository
import com.softwareoverflow.hiit_trainer.repository.WorkoutRepositoryFactory
import com.softwareoverflow.hiit_trainer.ui.upgrade.BillingViewModel
import com.softwareoverflow.hiit_trainer.ui.upgrade.MobileAdsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MainModuleInjection {

    @Provides
    fun providesBillingViewModel(application: Application): BillingViewModel {
        return BillingViewModel(application)
    }

    @Provides
    fun providesMobileAdsManager(@ApplicationContext context: Context): MobileAdsManager {
        return MobileAdsManager(context)
    }

    @Provides
    fun providesWorkoutRepo(@ApplicationContext context: Context) : IWorkoutRepository {
        return WorkoutRepositoryFactory.getInstance(context)
    }
}