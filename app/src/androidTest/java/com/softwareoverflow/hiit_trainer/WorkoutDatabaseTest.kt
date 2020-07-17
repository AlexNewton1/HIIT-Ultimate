package com.softwareoverflow.hiit_trainer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.softwareoverflow.hiit_trainer.data.WorkoutDatabase
import com.softwareoverflow.hiit_trainer.data.dao.ExerciseTypeDao
import com.softwareoverflow.hiit_trainer.data.dao.WorkoutDao
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class WorkoutDatabaseTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var workoutDao: WorkoutDao
    private lateinit var exerciseTypeDao: ExerciseTypeDao
    private lateinit var db: WorkoutDatabase

    private val TEST_DB = "migration-test"

    // Why can't this be imported? Might need to go without testing (no users will be affected as they won't have to migrate!)
   /* @Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        WorkoutDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )
*/
    @Before
    fun createDB() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        db = Room.inMemoryDatabaseBuilder(context, WorkoutDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        workoutDao = db.workoutDao
        exerciseTypeDao = db.exerciseTypeDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

   /* @Test
    @Throws(IOException::class)
    fun migrate1To2() {
        var db = helper.createDatabase(TEST_DB, 1).apply {
            // db has schema version 1. insert some data using SQL queries.
            // You cannot use DAO classes because they expect the latest schema.
            execSQL(...)

            // Prepare for the next version.
            close()
        }

        // Re-open the database with version 2 and provide
        // MIGRATION_1_2 as the migration process.
        db = helper.runMigrationsAndValidate(TEST_DB, 2, true, WorkoutDatabaseMigrator.MIGRATION_1_2)

        // MigrationTestHelper automatically verifies the schema changes,
        // but you need to validate that the data was migrated properly.
    }*/

    /*
    @Test
    fun insertAndGetWorkout() {
        val workout = WorkoutEntity(null, "MyWorkout")

        val exerciseType = ExerciseTypeEntity(0, "Test Type", "icon_name", "#666666")
        val exerciseTypeId = exerciseTypeDao.createOrUpdate(exerciseType)

        val exerciseTypes = getValue(exerciseTypeDao.getAllExerciseTypes())
        assertEquals(exerciseTypes.size, 1)

        val workoutId = workoutDao.createOrUpdate(workout)

        val workoutSet = WorkoutSetEntity(null, workoutId, exerciseTypeId, 5, 5, 6, 120)
        workoutSetDao.createOrUpdate(workoutSet)

        val workouts = getValue(workoutDao.getAllWorkouts())
        assertEquals(workouts.size, 1)

        val workoutSets = getValue(workoutSetDao.getAllWorkoutSets())
        assertEquals(workoutSets.size, 1)
    }
    */
}