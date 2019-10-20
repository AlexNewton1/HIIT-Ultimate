package com.softwareoverflow.hiitultimate.database.entity;

import com.softwareoverflow.hiitultimate.R;
import com.softwareoverflow.hiitultimate.database.AppDatabase;

public class Helper {

    private static ExerciseTypeEntity exerciseTypeEntity = new ExerciseTypeEntity("Test",
            R.drawable.ic_launcher_foreground,
            2);

    private static WorkoutEntity workoutEntity = new WorkoutEntity(
            "Test Name", "Desc");

    public static ExerciseTypeEntity getExerciseTypeEntity() {
        return exerciseTypeEntity;
    }

    public static WorkoutSetEntity getWorkoutSetEntity(){
        return new WorkoutSetEntity(
                5, 5, 6, 120);
    }

    public static WorkoutEntity getWorkoutEntity(){
        return workoutEntity;
    }

    public static long insertExerciseTypeIntoDatabase(AppDatabase testDb) {
        return testDb.exerciseTypeDao().createOrUpdate(exerciseTypeEntity);
    }

    public static long insertWorkoutIntoDatabase(AppDatabase testDb){
        return testDb.workoutDao().createOrUpdate(workoutEntity);
    }

    public static long insertWorkoutSetIntoDatabase(AppDatabase testDb) {
        return testDb.workoutSetDao().createOrUpdate(getWorkoutSetEntity());
    }
}
