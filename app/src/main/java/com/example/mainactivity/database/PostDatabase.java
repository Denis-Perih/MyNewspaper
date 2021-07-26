package com.example.mainactivity.database;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mainactivity.retrofit.Post;

@Entity
public class PostDatabase {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @Embedded
    public Post post;

    public PostDatabase(Post post) {
        this.post = post;
    }
}
