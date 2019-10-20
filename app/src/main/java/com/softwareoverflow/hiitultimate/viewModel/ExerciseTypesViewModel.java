package com.softwareoverflow.hiitultimate.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.softwareoverflow.hiitultimate.database.entity.ExerciseTypeEntity;
import com.softwareoverflow.hiitultimate.repository.exerciseType.ExerciseTypeRepository;

import java.util.List;

public class ExerciseTypesViewModel extends AndroidViewModel {

    private ExerciseTypeRepository repository;
    private LiveData<List<ExerciseTypeEntity>> entities;


    public ExerciseTypesViewModel(@NonNull Application application){
        super(application);

        repository = ExerciseTypeRepository.getInstance(application);

    }

    /**
     * @return an immutable list of the available ExerciseType objects from the persistent storage
     */
    public LiveData<List<ExerciseTypeEntity>> loadAllExerciseTypes() {
        entities = repository.loadAllExerciseTypes();
        return entities;
    }
}
