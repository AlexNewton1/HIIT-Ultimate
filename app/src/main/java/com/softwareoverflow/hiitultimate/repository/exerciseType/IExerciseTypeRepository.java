package com.softwareoverflow.hiitultimate.repository.exerciseType;

import androidx.lifecycle.LiveData;

import com.softwareoverflow.hiitultimate.database.entity.ExerciseTypeEntity;

import java.util.List;

public interface IExerciseTypeRepository {

    LiveData<List<ExerciseTypeEntity>> loadAllExerciseTypes();

    LiveData<ExerciseTypeEntity> loadExerciseTypeById(int id);

    void createOrUpdateExerciseType(ExerciseTypeEntity entity);

    void deleteExerciseType(ExerciseTypeEntity entity);
}
