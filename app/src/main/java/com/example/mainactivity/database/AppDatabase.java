package com.example.mainactivity.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {PostDatabase.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PostDatabaseDao postDatabaseDao();
}
