package com.example.mainactivity.retrofit;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JSONNewsApi {

    @GET("news")
    Single<News> getNews(@Query("apikey") String apiKey, @Query("language") String lang);
}
