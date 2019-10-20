package com.softwareoverflow.hiitultimate.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.softwareoverflow.hiitultimate.database.entity.ExerciseTypeEntity;
import com.softwareoverflow.hiitultimate.repository.exerciseType.ExerciseTypeRepository;

/**
 * AndroidViewModel for creating and editing
 * {@link com.softwareoverflow.hiitultimate.database.entity.ExerciseTypeEntity} objects
 */
public class CreateOrEditExerciseTypeViewModel extends AndroidViewModel {

    private ExerciseTypeRepository repository;
    private MutableLiveData<ExerciseTypeEntity> exerciseType;

    /**
     * @param application - the Application object
     * @param id - the id of the object to edit. Null if creating new one
     */
    public CreateOrEditExerciseTypeViewModel(@NonNull Application application, Integer id) {
        super(application);

        repository = ExerciseTypeRepository.getInstance(application);

//        if (id != null)// load the exerciseType
//            exerciseType = repository.(id);
//        else
//            exerciseType = repository.createNewExerciseType();
    }
}
