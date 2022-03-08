package com.softwareoverflow.hiit_trainer.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class WorkoutDatabaseMigrator {

    companion object {

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Workout ADD COLUMN numReps INTEGER NOT NULL DEFAULT 1")
                database.execSQL("ALTER TABLE Workout ADD COLUMN recoveryTime INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}