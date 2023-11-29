package com.softwareoverflow.hiit_trainer.ui.injection

import android.content.Context
import com.softwareoverflow.hiit_trainer.repository.IWorkoutRepository
import com.softwareoverflow.hiit_trainer.repository.WorkoutRepositoryFactory
import com.softwareoverflow.hiit_trainer.repository.billing.BillingRepository
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
    fun providesMobileAdsManager(@ApplicationContext context: Context): MobileAdsManager {
        return MobileAdsManager(context)
    }

    @Provides
    fun providesWorkoutRepo(@ApplicationContext context: Context): IWorkoutRepository {
        return WorkoutRepositoryFactory.getInstance(context)
    }

    @Provides
    fun providesBillingRepo(@ApplicationContext context: Context): BillingRepository {
        return BillingRepository(context)
    }

    @Provides
    fun providesBillingViewModel(repository: BillingRepository) :BillingViewModel {
        return BillingViewModel(repository)
    }
}