package com.softwareoverflow.hiit_trainer.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
internal interface BaseDao<T> {

    @Insert
    fun insert(obj: T) : Long

    @Update
    fun update(obj: T)

    @Delete
    fun delete(obj: T)

}