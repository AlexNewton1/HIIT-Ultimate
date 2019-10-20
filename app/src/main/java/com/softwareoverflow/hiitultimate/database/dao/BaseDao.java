package com.softwareoverflow.hiitultimate.database.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import java.util.List;


interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createOrUpdate(T toCreate);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createOrUpdate(List<T> toCreate);

    @Delete
    int delete(T toDelete);
}
