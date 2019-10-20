package com.softwareoverflow.hiitultimate.database.entity;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.softwareoverflow.hiitultimate.database.dto.WorkoutSet;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static com.softwareoverflow.hiitultimate.database.entity.Helper.insertExerciseTypeIntoDatabase;
import static com.softwareoverflow.hiitultimate.database.entity.Helper.insertWorkoutIntoDatabase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4ClassRunner.class)
public class WorkoutSetDaoTest extends DatabaseInMemoryTest {

    @Test
    public void testGetWorkoutSetsForWorkout() {
        int exerciseTypeId = (int) insertExerciseTypeIntoDatabase(testDb);
        int workoutId = (int) insertWorkoutIntoDatabase(testDb);

        WorkoutSetEntity workoutSetEntity = Helper.getWorkoutSetEntity();
        workoutSetEntity.setExerciseTypeId(exerciseTypeId);
        workoutSetEntity.setWorkoutId(workoutId);

        WorkoutSetEntity entity2 = new WorkoutSetEntity(10, 5, 3, 1);
        entity2.setExerciseTypeId(exerciseTypeId);
        entity2.setWorkoutId(workoutId);

        long id1 = testDb.workoutSetDao().createOrUpdate(workoutSetEntity);
        long id2 = testDb.workoutSetDao().createOrUpdate(entity2);

        List<WorkoutSet> sets =
                LiveDataTestUtil.getValue(testDb.workoutSetDao().loadSetsForWorkout(workoutId));
        assertEquals(2, sets.size());
        assertEquals(id1, sets.get(0).getWorkoutSet().getWorkoutSetId());
        assertEquals(id2, sets.get(1).getWorkoutSet().getWorkoutSetId());
    }

    @Test
    public void testInsertWorkoutSet() {
        int exerciseTypeId = (int) insertExerciseTypeIntoDatabase(testDb);
        int workoutId = (int) insertWorkoutIntoDatabase(testDb);

        WorkoutSetEntity workoutSetEntity = Helper.getWorkoutSetEntity();
        workoutSetEntity.setExerciseTypeId(exerciseTypeId);
        workoutSetEntity.setWorkoutId(workoutId);

        int id = (int) testDb.workoutSetDao().createOrUpdate(workoutSetEntity);
        List<WorkoutSet> sets =
                LiveDataTestUtil.getValue(testDb.workoutSetDao().loadSetsForWorkout(workoutId));

        assertEquals(1, id);
        assertNotNull(sets);

        WorkoutSet workoutSet = sets.get(0);
        assertEquals(5, workoutSet.getWorkoutSet().getWorkTime());
        assertEquals(5, workoutSet.getWorkoutSet().getRestTime());
        assertEquals(6, workoutSet.getWorkoutSet().getNumReps());
        assertEquals(120, workoutSet.getWorkoutSet().getRecoveryTime());

        ExerciseTypeEntity exerciseTypeEntity = Helper.getExerciseTypeEntity();
        exerciseTypeEntity.setExerciseTypeId(exerciseTypeId);

        ExerciseTypeEntity savedEntity = workoutSet.getExerciseTypeEntity();
        assertEquals(savedEntity.getExerciseTypeId(), exerciseTypeEntity.getExerciseTypeId());
        assertEquals(savedEntity.getName(), exerciseTypeEntity.getName());
        assertEquals(savedEntity.getIconId(), exerciseTypeEntity.getIconId());
        assertEquals(savedEntity.getColorIndex(), exerciseTypeEntity.getColorIndex());
    }

    @Test
    public void testUpdateValidWorkoutSet(){
        // Create the entity to be modified
        long exerciseTypeId = Helper.insertExerciseTypeIntoDatabase(testDb);
        long workoutId = Helper.insertWorkoutIntoDatabase(testDb);
        WorkoutSetEntity entity = Helper.getWorkoutSetEntity();
        entity.setWorkoutId(workoutId);
        entity.setExerciseTypeId(exerciseTypeId);

        long id = testDb.workoutSetDao().createOrUpdate(entity);

        //Modify the entity
        entity.setWorkoutSetId(id);
        entity.setWorkTime(100);

        int rowsAffected = testDb.workoutSetDao().update(entity);
        List<WorkoutSet> sets =
                LiveDataTestUtil.getValue(testDb.workoutSetDao().loadSetsForWorkout(workoutId));

        assertEquals(1, rowsAffected);
        assertEquals(100, sets.get(0).getWorkoutSet().getWorkTime());
    }

    @Test
    public void testUpdateInvalidWorkoutSet() {
        WorkoutSetEntity entity = Helper.getWorkoutSetEntity();
        int rowsAffected = testDb.workoutSetDao().update(entity);

        assertEquals(0, rowsAffected);
    }

    @Test
    public void testDeleteWorkoutSet() {
        long exerciseTypeId = Helper.insertExerciseTypeIntoDatabase(testDb);
        long workoutId = Helper.insertWorkoutIntoDatabase(testDb);
        WorkoutSetEntity entity = Helper.getWorkoutSetEntity();
        entity.setWorkoutId(workoutId);
        entity.setExerciseTypeId(exerciseTypeId);

        long id = testDb.workoutSetDao().createOrUpdate(entity);
        entity.setWorkoutSetId(id);

        int rowsAffected = testDb.workoutSetDao().delete(entity);
        List<WorkoutSet> sets =
                LiveDataTestUtil.getValue(testDb.workoutSetDao().loadSetsForWorkout(workoutId));

        assertEquals(1, rowsAffected);
        assertEquals(0, sets.size());
    }
}
