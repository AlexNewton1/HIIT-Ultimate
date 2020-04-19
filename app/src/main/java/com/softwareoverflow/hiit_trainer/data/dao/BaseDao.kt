package com.softwareoverflow.hiit_trainer.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface BaseDao<T> {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun createOrUpdate(obj: T) : Long

    fun delete(obj: T)

}