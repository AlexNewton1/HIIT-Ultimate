package com.softwareoverflow.hiitultimate.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.softwareoverflow.hiitultimate.database.dto.WorkoutSet;
import com.softwareoverflow.hiitultimate.database.entity.ExerciseTypeEntity;
import com.softwareoverflow.hiitultimate.database.entity.WorkoutSetEntity;

import java.util.List;

@Dao
public abstract class WorkoutSetDao implements BaseDao<WorkoutSetEntity> {

    @Query("Select * FROM workoutSet join exerciseType on exerciseTypeId = fkExerciseTypeId WHERE fkWorkoutId = :workoutId")
    public abstract LiveData<List<WorkoutSet>> loadSetsForWorkout(long workoutId);

    @Transaction
    @Insert
    public abstract void insert(WorkoutSetEntity setEntity, ExerciseTypeEntity typeEntity);
}
