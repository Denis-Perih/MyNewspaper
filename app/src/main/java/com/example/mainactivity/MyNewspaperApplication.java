package com.example.mainactivity;

import android.app.Application;

import androidx.room.Room;

import com.example.mainactivity.database.AppDatabase;

public class MyNewspaperApplication extends Application {

    private static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database").build();
    }

    public static AppDatabase getDatabase() {
        return database;
    }
}
