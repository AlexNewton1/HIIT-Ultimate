package com.softwareoverflow.hiitultimate.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.softwareoverflow.hiitultimate.database.dto.Workout;
import com.softwareoverflow.hiitultimate.database.entity.WorkoutEntity;
import com.softwareoverflow.hiitultimate.database.entity.WorkoutSetEntity;

import java.util.List;

@Dao
public abstract class WorkoutDao implements BaseDao<WorkoutEntity> {

    // TODO - think about best methods for this given it won't have all it's data inside of it (necessarily)

    @Transaction
    @Query("Select * FROM workout")
    public abstract LiveData<List<Workout>> loadAllWorkouts();

    @Transaction
    @Query("SELECT * FROM workout WHERE workoutId = :workoutId")
    public abstract LiveData<Workout> loadWorkoutById(long workoutId);

    @Transaction
    public void createOrUpdate(WorkoutEntity workout, List<WorkoutSetEntity> sets,
                               WorkoutSetDao workoutSetDao) {
        long workoutId = createOrUpdate(workout);
        workout.setWorkoutId(workoutId);

        // New workout -> configure the WorkoutSetEntity foreign keys
        for (WorkoutSetEntity workoutSet : sets)
            workoutSet.setWorkoutId(workoutId);
        workoutSetDao.createOrUpdate(sets);
    }
}
