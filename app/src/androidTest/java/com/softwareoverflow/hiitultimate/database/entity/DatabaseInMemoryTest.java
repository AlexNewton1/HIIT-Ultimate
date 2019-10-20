package com.softwareoverflow.hiitultimate.database.entity;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.softwareoverflow.hiitultimate.database.AppDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import java.io.IOException;

public class DatabaseInMemoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    AppDatabase testDb;

    @Before
    public void createInMemDb() {
        Context context = ApplicationProvider.getApplicationContext();
        testDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).allowMainThreadQueries().build();
    }

    @After
    public void cloeDb() throws IOException {
        testDb.close();
    }
}
