package com.softwareoverflow.hiitultimate.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class WorkoutViewModelFactory implements ViewModelProvider.Factory {

    private Application application;
    private Integer workoutId;

    public WorkoutViewModelFactory(@NonNull Application application, Integer workoutId) {
        this.application = application;
        this.workoutId = workoutId;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new WorkoutViewModel(application, workoutId);
    }
}
