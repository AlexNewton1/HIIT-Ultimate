package com.softwareoverflow.hiit_trainer.repository

import android.content.Context

/**
 * Simple factory to create [IWorkoutRepository] instances
 */
class WorkoutRepositoryFactory {
    companion object {
        fun getInstance(context: Context) = WorkoutRepositoryRoomDb(context)
    }
}