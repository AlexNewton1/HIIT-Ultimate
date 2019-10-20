package com.softwareoverflow.hiitultimate.database.dto;

import androidx.room.Embedded;

import com.softwareoverflow.hiitultimate.database.entity.ExerciseTypeEntity;
import com.softwareoverflow.hiitultimate.database.entity.WorkoutSetEntity;

/**
 *  A WorkoutSetEntity consists of a 'work' period followed by a 'rest' period (AKA a 'rep').
 *  The 'rep' is then  repeated a set number of times before being followed by
 */
public class WorkoutSet{

    @Embedded
    private WorkoutSetEntity workoutSet;

    @Embedded
    private ExerciseTypeEntity exerciseTypeEntity;

    public WorkoutSetEntity getWorkoutSet() {
        return workoutSet;
    }

    public ExerciseTypeEntity getExerciseTypeEntity() {
        return exerciseTypeEntity;
    }

    public void setWorkoutSet(WorkoutSetEntity workoutSet) {
        this.workoutSet = workoutSet;
    }

    public void setExerciseTypeEntity(ExerciseTypeEntity exerciseTypeEntity) {
        this.exerciseTypeEntity = exerciseTypeEntity;
    }
}
