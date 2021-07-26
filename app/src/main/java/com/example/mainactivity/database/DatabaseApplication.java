package com.example.mainactivity.database;

import android.app.Application;

import androidx.room.Room;

public class DatabaseApplication extends Application {

    public static DatabaseApplication instance;

    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database").build();
    }

    public static DatabaseApplication getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
