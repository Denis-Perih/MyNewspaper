package com.example.mainactivity.auth.firebase;

public interface FirebaseListener {

    void onSuccess(String indicatorAuth);

    void onFailure(String indicatorAuth);
}
