package com.softwareoverflow.hiitultimate.database.entity;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.softwareoverflow.hiitultimate.database.dto.WorkoutSet;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static com.softwareoverflow.hiitultimate.database.entity.Helper.insertExerciseTypeIntoDatabase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4ClassRunner.class)
public class ExerciseTypeDaoTest extends DatabaseInMemoryTest {

    @Test
    public void loadExerciseTypeDbEmpty() {
        List<ExerciseTypeEntity> entities =
                LiveDataTestUtil.getValue(testDb.exerciseTypeDao().loadAllExerciseTypes());
        assertEquals(0, entities.size());
    }

    @Test
    public void insertAndGetExerciseType() {
        ExerciseTypeEntity entity = Helper.getExerciseTypeEntity();

        long id = testDb.exerciseTypeDao().createOrUpdate(entity);
        List<ExerciseTypeEntity> entities =
                LiveDataTestUtil.getValue(testDb.exerciseTypeDao().loadAllExerciseTypes());

        assertEquals(1, id);

        assertNotNull(entities);
        assertEquals(1, entities.size());

        ExerciseTypeEntity result = entities.get(0);
        assertEquals(entity.getName(), result.getName());
        assertEquals(entity.getIconId(), result.getIconId());
        assertEquals(entity.getColorIndex(), result.getColorIndex());
    }

    @Test
    public void updateValidExerciseType() {
        long id = insertExerciseTypeIntoDatabase(testDb);

        long workoutId = Helper.insertWorkoutIntoDatabase(testDb);
        WorkoutSetEntity set = Helper.getWorkoutSetEntity();
        set.setExerciseTypeId(id);
        set.setWorkoutId(workoutId);
        testDb.workoutSetDao().createOrUpdate(set);

        ExerciseTypeEntity entity = Helper.getExerciseTypeEntity();
        entity.setExerciseTypeId((int) id);
        entity.setName("New Name");
        entity.setIconId(4);
        int rowsAffected = testDb.exerciseTypeDao().update(entity);


        List<ExerciseTypeEntity> entities =
                LiveDataTestUtil.getValue(testDb.exerciseTypeDao().loadAllExerciseTypes());

        assertEquals(1, rowsAffected);
        assertNotNull(entities);
        assertEquals(1, entities.size());

        ExerciseTypeEntity result = entities.get(0);
        assertEquals(entity.getName(), result.getName());
        assertEquals(entity.getIconId(), result.getIconId());
        assertEquals(entity.getColorIndex(), result.getColorIndex());

        // Ensure the WorkoutSet gets the updates
        List<WorkoutSet> sets =
                LiveDataTestUtil.getValue(testDb.workoutSetDao().loadSetsForWorkout(workoutId));
        assertEquals(1, sets.size());
        assertEquals(entity.getName(), sets.get(0).getExerciseTypeEntity().getName());
    }

    @Test
    public void updateInvalidExerciseType() {
        ExerciseTypeEntity entity = Helper.getExerciseTypeEntity();
        entity.setName("New Name");
        entity.setIconId(4);

        int rowsAffected = testDb.exerciseTypeDao().update(entity);
        List<ExerciseTypeEntity> entities =
                LiveDataTestUtil.getValue(testDb.exerciseTypeDao().loadAllExerciseTypes());

        assertEquals(0, rowsAffected);
        assertEquals(0, entities.size());
    }

    @Test
    public void deleteValidExerciseType() {
        long id = insertExerciseTypeIntoDatabase(testDb);
        ExerciseTypeEntity entity = Helper.getExerciseTypeEntity();
        entity.setExerciseTypeId((int) id);

        List<ExerciseTypeEntity> entities =
                LiveDataTestUtil.getValue(testDb.exerciseTypeDao().loadAllExerciseTypes());
        Assert.assertEquals(1, entities.size());

        int rowsAffected = testDb.exerciseTypeDao().delete(entity);

        assertEquals(1, rowsAffected);
        entities =
                LiveDataTestUtil.getValue(testDb.exerciseTypeDao().loadAllExerciseTypes());
        assertEquals(0, entities.size());
    }

    @Test
    public void deleteInvalidExerciseType() {
        ExerciseTypeEntity entity = Helper.getExerciseTypeEntity();
        entity.setExerciseTypeId(9999);

        int rowsAffected = testDb.exerciseTypeDao().delete(entity);
        assertEquals(0, rowsAffected);
    }
}