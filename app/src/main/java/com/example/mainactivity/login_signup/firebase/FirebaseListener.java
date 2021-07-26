package com.example.mainactivity.login_signup.firebase;

public interface FirebaseListener {

    void onSuccess(String indicatorAuth);

    void onFailure(String indicatorAuth);
}
