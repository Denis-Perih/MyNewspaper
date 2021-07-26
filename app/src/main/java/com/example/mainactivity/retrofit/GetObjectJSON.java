package com.example.mainactivity.retrofit;

import androidx.annotation.NonNull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class GetObjectJSON {

    public void objectJSON(AnswerJSON answerJSON) {

        String apiKey = "pub_5528f2ad4b11d726b66fe821466ba214497";
        String lang = "en";
        NetworkService.getInstance()
                .getJSONApi()
                .getNews(apiKey, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<News>() {
                    @Override
                    public void onSuccess(@NonNull News news) {
                        answerJSON.onSuccessJSON(news.getResults());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        answerJSON.onFailureJSON();
                    }
                });
    }
}