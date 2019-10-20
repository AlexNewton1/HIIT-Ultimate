package com.softwareoverflow.hiitultimate.database.entity;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.softwareoverflow.hiitultimate.database.dto.Workout;
import com.softwareoverflow.hiitultimate.database.dto.WorkoutSet;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4ClassRunner.class)
public class WorkoutDaoTest extends DatabaseInMemoryTest {

    @Test
    public void testInsertWorkout() {
        WorkoutEntity workoutEntity = new WorkoutEntity("Test name", "");

        long workoutId = testDb.workoutDao().createOrUpdate(workoutEntity);
        List<Workout> workouts =
                LiveDataTestUtil.getValue(testDb.workoutDao().loadAllWorkouts());

        assertEquals(1, workoutId);
        assertEquals(1, workouts.size());
        assertEquals(workouts.get(0).getWorkout().getName(), "Test name");


        long exerciseTypeId = Helper.insertExerciseTypeIntoDatabase(testDb);
        WorkoutSetEntity setEntity = Helper.getWorkoutSetEntity();
        setEntity.setWorkoutId(workoutId);
        setEntity.setExerciseTypeId(exerciseTypeId);
        testDb.workoutSetDao().createOrUpdate(setEntity);

        WorkoutSetEntity setEntity2 = new WorkoutSetEntity(1, 2, 3, 4);
        setEntity2.setWorkoutId(workoutId);
        setEntity2.setExerciseTypeId(exerciseTypeId);
        testDb.workoutSetDao().createOrUpdate(setEntity2);

        // Check it also returns the linked workout sets
        workouts = LiveDataTestUtil.getValue(testDb.workoutDao().loadAllWorkouts());
        assertEquals(2, workouts.get(0).getSets().size());
    }

    @Test
    public void testUpdateValidWorkout() {
        long id = Helper.insertWorkoutIntoDatabase(testDb);
        WorkoutEntity entity = Helper.getWorkoutEntity();
        entity.setWorkoutId(id);
        entity.setDescription("Description");
        entity.setName("Updated");

        int rowsAffected = testDb.workoutDao().update(entity);

        List<Workout> workouts =
                LiveDataTestUtil.getValue(testDb.workoutDao().loadAllWorkouts());

        assertEquals(1, rowsAffected);
        assertEquals("Updated", workouts.get(0).getWorkout().getName());
        assertEquals("Description", workouts.get(0).getWorkout().getDescription());
    }

    @Test
    public void testUpdateInvalidWorkout() {
        WorkoutEntity entity = Helper.getWorkoutEntity();
        int rowsAffected = testDb.workoutDao().update(entity);

        assertEquals(0, rowsAffected);
    }

    @Test
    public void testDeleteWorkout() {
        WorkoutEntity workoutEntity = new WorkoutEntity("Test name", "");

        long workoutId = testDb.workoutDao().createOrUpdate(workoutEntity);
        workoutEntity.setWorkoutId(workoutId);

        long exerciseTypeId = Helper.insertExerciseTypeIntoDatabase(testDb);
        WorkoutSetEntity setEntity = Helper.getWorkoutSetEntity();
        setEntity.setWorkoutId(workoutId);
        setEntity.setExerciseTypeId(exerciseTypeId);
        testDb.workoutSetDao().createOrUpdate(setEntity);

        WorkoutSetEntity setEntity2 = new WorkoutSetEntity(1, 2, 3, 4);
        setEntity2.setWorkoutId(workoutId);
        setEntity2.setExerciseTypeId(exerciseTypeId);
        testDb.workoutSetDao().createOrUpdate(setEntity2);

        // Check it we have both correctly added
        List<Workout> workouts = LiveDataTestUtil.getValue(testDb.workoutDao().loadAllWorkouts());
        assertEquals(2, workouts.get(0).getSets().size());

        testDb.workoutDao().delete(workoutEntity);
        workouts = LiveDataTestUtil.getValue(testDb.workoutDao().loadAllWorkouts());
        assertEquals(0, workouts.size());

        //Test deleting workouts cascade deletes the sets
        List<WorkoutSet> sets =
                LiveDataTestUtil.getValue(testDb.workoutSetDao().loadSetsForWorkout(workoutId));
        assertEquals(0, sets.size());

        // Test deleting workouts does NOT delete the exercise type
        List<ExerciseTypeEntity> exerciseTypeEntities =
                LiveDataTestUtil.getValue(testDb.exerciseTypeDao().loadAllExerciseTypes());
        assertEquals(1, exerciseTypeEntities.size());
    }
}
