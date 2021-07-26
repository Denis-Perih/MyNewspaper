package com.example.mainactivity.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface PostDatabaseDao {

    @Query("SELECT * FROM PostDatabase")
    Single<List<PostDatabase>> getPosts();

    @Insert
    Completable insert(List<PostDatabase> postDatabase);
}
