package com.softwareoverflow.hiit_trainer.ui.injection

import com.softwareoverflow.hiit_trainer.data.history.WorkoutHistoryRoomDb
import com.softwareoverflow.hiit_trainer.ui.history.write.HistorySaverLocal
import com.softwareoverflow.hiit_trainer.ui.history.write.HistoryWriterLocal
import com.softwareoverflow.hiit_trainer.ui.history.write.IHistorySaver
import com.softwareoverflow.hiit_trainer.ui.history.write.IHistoryWriter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelInjection {
    @Provides
    @ViewModelScoped
    fun providesHistoryWriter(db: WorkoutHistoryRoomDb) : IHistoryWriter {
        return HistoryWriterLocal(db)
    }

    @Provides
    @ViewModelScoped
    fun providesHistorySaver(writer: IHistoryWriter) : IHistorySaver {
        return HistorySaverLocal(writer)
    }
}