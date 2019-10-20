package com.softwareoverflow.hiitultimate.database;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.softwareoverflow.hiitultimate.R;
import com.softwareoverflow.hiitultimate.database.dao.ExerciseTypeDao;
import com.softwareoverflow.hiitultimate.database.dao.WorkoutDao;
import com.softwareoverflow.hiitultimate.database.dao.WorkoutSetDao;
import com.softwareoverflow.hiitultimate.database.entity.ExerciseTypeEntity;
import com.softwareoverflow.hiitultimate.database.entity.WorkoutEntity;
import com.softwareoverflow.hiitultimate.database.entity.WorkoutSetEntity;

import java.util.Arrays;
import java.util.List;

@Database(
        entities = {WorkoutEntity.class, ExerciseTypeEntity.class, WorkoutSetEntity.class},
        version = 1
)
public abstract class AppDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "hiitUltimate";
    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                Log.d("debugger2", "Creating new database instance");
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .addCallback(new RoomDatabase.Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                                new Thread(() -> {
                                    instance.exerciseTypeDao().createOrUpdate(createExerciseTypeData(context));
                                    Log.d("debugger2", "inserted data!");
                                }).start();
                            }
                        })
                        .build();

                Log.d("debugger2", "instance done! " + instance);

                // run a read/write operation on creation to trigger the DB onCreate callback
                new Thread(() -> {
                    instance.runInTransaction(() -> {});
                    //instance.beginTransaction();
                    //instance.endTransaction();
                    // or query a dummy select statement
                    //instance.query("select 1", null);
                }).start();
            }
        }
        Log.d("debugger2", "Getting the database instance");

        return instance;
    }

    public abstract ExerciseTypeDao exerciseTypeDao();

    public abstract WorkoutSetDao workoutSetDao();

    public abstract WorkoutDao workoutDao();

    private static List<ExerciseTypeEntity> createExerciseTypeData(Context context) {
        String[] colors = context.getResources().getStringArray(R.array.exercise_type_colors);

        TypedArray ta = context.getResources().obtainTypedArray(R.array.exercise_type_drawables);
        int[] icons = new int[ta.length()];
        for(int i=0; i<ta.length(); i++){
            icons[i] = ta.getResourceId(i, 0);
        }
        ta.recycle();

        ExerciseTypeEntity e1 = new ExerciseTypeEntity("Push Ups", icons[0], colors[0]);
        ExerciseTypeEntity e2 = new ExerciseTypeEntity("Pull Ups", icons[1], colors[3]);
        ExerciseTypeEntity e3 = new ExerciseTypeEntity("This really obnoxiously long name is 50 character", icons[2], colors[1]);
        ExerciseTypeEntity e4 = new ExerciseTypeEntity("Jumping Jacks", icons[3], colors[3]);
        ExerciseTypeEntity e5 = new ExerciseTypeEntity("Squat", icons[4], colors[2]);
        ExerciseTypeEntity e6 = new ExerciseTypeEntity("Lunge", icons[0], colors[3]);

        return Arrays.asList(e1, e2, e3, e4, e5, e6);
    }
}
