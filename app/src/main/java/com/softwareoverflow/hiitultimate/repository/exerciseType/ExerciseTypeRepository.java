package com.softwareoverflow.hiitultimate.repository.exerciseType;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.softwareoverflow.hiitultimate.database.AppDatabase;
import com.softwareoverflow.hiitultimate.database.dao.ExerciseTypeDao;
import com.softwareoverflow.hiitultimate.database.entity.ExerciseTypeEntity;

import java.util.List;

public class ExerciseTypeRepository implements IExerciseTypeRepository {

    private static ExerciseTypeRepository instance;
    private ExerciseTypeDao dao;

    private LiveData<List<ExerciseTypeEntity>> exerciseTypes;

    public static ExerciseTypeRepository getInstance(@NonNull Application application) {
        if(instance == null)
            instance = new ExerciseTypeRepository(application);

        return instance;
    }

    private ExerciseTypeRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application.getApplicationContext());
        dao = db.exerciseTypeDao();
    }

    @Override
    public LiveData<List<ExerciseTypeEntity>> loadAllExerciseTypes() {
        exerciseTypes = dao.loadAllExerciseTypes();
        return exerciseTypes;
    }

    @Override
    public LiveData<ExerciseTypeEntity> loadExerciseTypeById(int id) {
        return dao.loadById(id);
    }

    @Override
    public void createOrUpdateExerciseType(ExerciseTypeEntity entity) {
        dao.createOrUpdate(entity);
    }

    @Override
    public void deleteExerciseType(ExerciseTypeEntity entity) {
        dao.delete(entity);
    }
}
