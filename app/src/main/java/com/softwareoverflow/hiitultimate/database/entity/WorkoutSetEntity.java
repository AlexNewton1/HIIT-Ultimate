package com.softwareoverflow.hiitultimate.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "workoutSet",
        foreignKeys = {
                @ForeignKey(entity = WorkoutEntity.class, // Where the fk maps to
                        parentColumns = {"workoutId"}, // Column name in the entity mapped to
                        childColumns = {"fkWorkoutId"}, // Column name to map to the entity
                        onUpdate = ForeignKey.CASCADE,
                        onDelete = ForeignKey.CASCADE //Delete this when the WorkoutEntity is deleted
                ),
                @ForeignKey(entity = ExerciseTypeEntity.class, // Where the fk maps to
                        parentColumns = {"exerciseTypeId"}, // Column name in the entity mapped to
                        childColumns = {"fkExerciseTypeId"}, // Column name to map to the entity
                        onUpdate = ForeignKey.CASCADE // Updates to the exercise type are reflected in here
                )
        })
public class WorkoutSetEntity {

    @PrimaryKey(autoGenerate = true)
    private long workoutSetId;

    @ColumnInfo(name = "fkWorkoutId", index = true)
    private long workoutId;

    @ColumnInfo(name = "fkExerciseTypeId", index = true)
    private long exerciseTypeId;
    private int workTime, restTime, numReps, recoveryTime;

    public WorkoutSetEntity(int workTime, int restTime, int numReps, int recoveryTime) {
        this.workTime = workTime;
        this.restTime = restTime;
        this.numReps = numReps;
        this.recoveryTime = recoveryTime;
    }

    //region GETTER methods
    public long getWorkoutSetId() {
        return workoutSetId;
    }

    public long getWorkoutId() {
        return workoutId;
    }

    public long getExerciseTypeId() {
        return exerciseTypeId;
    }

    public int getWorkTime() {
        return workTime;
    }

    public int getRestTime() {
        return restTime;
    }

    public int getNumReps() {
        return numReps;
    }

    public int getRecoveryTime() {
        return recoveryTime;
    }
    //endregion

    //region SETTER methods
    public void setWorkoutSetId(long workoutSetId) {
        this.workoutSetId = workoutSetId;
    }

    public void setWorkoutId(long workoutId) {
        this.workoutId = workoutId;
    }

    public void setExerciseTypeId(long exerciseTypeId) {
        this.exerciseTypeId = exerciseTypeId;
    }

    void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    void setRestTime(int restTime) {
        this.restTime = restTime;
    }

    void setNumReps(int numReps) {
        this.numReps = numReps;
    }

    void setRecoveryTime(int recoveryTime) {
        this.recoveryTime = recoveryTime;
    }
    //endregion
}
