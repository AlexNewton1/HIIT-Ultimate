package com.softwareoverflow.hiitultimate.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exerciseType")
public class ExerciseTypeEntity {

    @PrimaryKey(autoGenerate = true)
    private long exerciseTypeId;

    private String name;
    private int iconId; // references a R.drawable value
    private String colorHex; // the hex code for the color

    public ExerciseTypeEntity(String name, int iconId, String colorHex) {
        this.name = name;
        this.iconId = iconId;
        this.colorHex = colorHex;
    }

    //region GETTER methods
    public long getExerciseTypeId(){
        return exerciseTypeId;
    }

    public String getName() {
        return name;
    }

    public int getIconId() {
        return iconId;
    }

    public String getColorHex() {
        return colorHex;
    }
    //endregion

    //region SETTER methods
    public void setExerciseTypeId(long exerciseTypeId){
        this.exerciseTypeId = exerciseTypeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }
    //endregion
}
