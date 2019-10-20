package com.softwareoverflow.hiitultimate.repository.workout;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.softwareoverflow.hiitultimate.database.AppDatabase;
import com.softwareoverflow.hiitultimate.database.dao.WorkoutDao;
import com.softwareoverflow.hiitultimate.database.dao.WorkoutSetDao;
import com.softwareoverflow.hiitultimate.database.dto.Workout;
import com.softwareoverflow.hiitultimate.database.dto.WorkoutSet;
import com.softwareoverflow.hiitultimate.database.entity.WorkoutEntity;
import com.softwareoverflow.hiitultimate.database.entity.WorkoutSetEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * An instance of IWorkoutRepository for creating a Workout in memory and then persisting to
 * storage when completed.
 */
public class WorkoutRepository implements IWorkoutRepository {

    private static WorkoutRepository instance;

    private MutableLiveData<Workout> mutableWorkout;

    private WorkoutDao workoutDao;
    private WorkoutSetDao workoutSetDao;

    public static WorkoutRepository getInstance(@NonNull Application application) {
        if(instance == null)
            instance = new WorkoutRepository(application);

        return instance;
    }

    private WorkoutRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application.getApplicationContext());
        workoutDao = db.workoutDao();
        workoutSetDao = db.workoutSetDao();
    }

    //region Workout specific methods
    @Override
    public MutableLiveData<Workout> createNewWorkout() {
        if(mutableWorkout == null)
        {
            Workout workout = new Workout();
            workout.setWorkout(new WorkoutEntity());
            workout.setWorkoutSets(new ArrayList<>());

            mutableWorkout = new MutableLiveData<>();
            mutableWorkout.setValue(workout);
        }

        return mutableWorkout;
    }

    @Override
    public MutableLiveData<Workout> loadWorkoutById(int workoutId) {
        Workout workout = workoutDao.loadWorkoutById(workoutId).getValue();
        mutableWorkout.setValue(workout);
        return mutableWorkout;
    }

    @Override
    public void deleteWorkout(Workout workout) {
        workoutDao.delete(workout.getWorkout());
    }
    //endregion methods

    //region WorkoutSet specific methods
    @Override
    public void addWorkoutSet(WorkoutSet workoutSet) {
        Workout workout = mutableWorkout.getValue();
        workout.getSets().add(workoutSet.getWorkoutSet());
    }

    @Override
    public void updateWorkoutSet(WorkoutSet newWorkoutSet, int index) {
        List<WorkoutSetEntity> workoutSets = mutableWorkout.getValue().getSets();
        workoutSets.remove(index);
        workoutSets.add(index, newWorkoutSet.getWorkoutSet());
    }

    @Override
    public void deleteWorkoutSet(WorkoutSet workoutSet) {
         mutableWorkout.getValue().getSets().remove(workoutSet.getWorkoutSet());
         mutableWorkout.setValue(mutableWorkout.getValue());
    }

    @Override
    public void deleteWorkoutSet(int index) {
        mutableWorkout.getValue().getSets().remove(index);
        mutableWorkout.setValue(mutableWorkout.getValue());
    }
    //endregion

    @Override
    public void saveChanges(Workout workout) {
        long workoutId = workoutDao.createOrUpdate(workout.getWorkout());

        //Ensure the FKs are set up correctly
        for(WorkoutSetEntity set : workout.getSets())
            set.setWorkoutId(workoutId);
        workoutSetDao.createOrUpdate(workout.getSets());
    }
}