package com.softwareoverflow.hiitultimate.database.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.softwareoverflow.hiitultimate.database.dto.WorkoutSet;

/**
 * A WorkoutEntity is a group of {@link WorkoutSet}s completed in order, usually with some form of
 * 'recovery' time between them.
 */
@Entity(tableName = "workout")
public class WorkoutEntity {

    @PrimaryKey(autoGenerate = true)
    private long workoutId;
    private String name, description;

    public WorkoutEntity() {}

    @Ignore // Force Room to use the default constructor to suppress warning
    public WorkoutEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // region GETTER methods
    public long getWorkoutId() {
        return workoutId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    //endregion

    //region SETTER methods
    public void setWorkoutId(long workoutId) {
        this.workoutId = workoutId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    //endregion
}
