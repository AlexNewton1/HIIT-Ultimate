package com.softwareoverflow.hiitultimate.database.dto;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

import com.softwareoverflow.hiitultimate.database.entity.WorkoutEntity;
import com.softwareoverflow.hiitultimate.database.entity.WorkoutSetEntity;

import java.util.ArrayList;
import java.util.List;

public class Workout {

    @Embedded
    private WorkoutEntity workout;

    @NonNull
    @Relation(parentColumn = "workoutId", entityColumn = "fkWorkoutId")
    private List<WorkoutSetEntity> sets = new ArrayList<>();

    public WorkoutEntity getWorkout() {
        return workout;
    }

    public List<WorkoutSetEntity> getSets() {
        return sets;
    }

    public void setWorkout(WorkoutEntity workout) {
        this.workout = workout;
    }

    public void setSets(List<WorkoutSetEntity> sets) {
        this.sets = sets;
    }

    public void setWorkoutSets(List<WorkoutSet> workoutSets) {
        List<WorkoutSetEntity> entities = new ArrayList<>();

        for(WorkoutSet workoutSet : workoutSets)
            entities.add(workoutSet.getWorkoutSet());

        setSets(entities);
    }

    public void addWorkoutSet(WorkoutSet workoutSet) {
        getSets().add(workoutSet.getWorkoutSet());
    }
}
