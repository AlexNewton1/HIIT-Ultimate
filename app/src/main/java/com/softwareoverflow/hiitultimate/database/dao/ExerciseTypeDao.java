package com.softwareoverflow.hiitultimate.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.softwareoverflow.hiitultimate.database.entity.ExerciseTypeEntity;

import java.util.List;

@Dao
public abstract class ExerciseTypeDao implements BaseDao<ExerciseTypeEntity> {

    @Query("SELECT * FROM exerciseType")
    public abstract LiveData<List<ExerciseTypeEntity>> loadAllExerciseTypes();

    @Query("SELECT * FROM exerciseType where exerciseTypeId = :id")
    public abstract LiveData<ExerciseTypeEntity> loadById(long id);

    @Query("SELECT COUNT(*) FROM workoutSet WHERE fkExerciseTypeId = :id")
    public abstract boolean isSafeDelete(long id);
}
