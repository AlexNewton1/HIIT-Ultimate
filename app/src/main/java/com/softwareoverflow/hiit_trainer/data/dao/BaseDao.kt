package com.softwareoverflow.hiit_trainer.data.dao

import androidx.room.*

@Dao
interface BaseDao<T> {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun createOrUpdate(obj: T) : Long

    @Update
    fun update(obj: T)

    @Delete
    fun delete(obj: T)

}