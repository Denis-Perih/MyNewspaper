package com.example.mainactivity.login_signup.login;

import android.content.Intent;

import androidx.fragment.app.Fragment;

public interface LoginContract {

    interface View {

        void signInWithEmail(String email, String password);

        void showSnackBar(String text);

        void startSignInGoogle();

        void startSignInFacebook();

        void startFirebaseAuthWithGoogle(String idToken);

        void startActivityResult(int requestCode, int resultCode, Intent data);

        void successLogin();

        void failureLogin(String text);
    }

    interface Presenter {

        void onLoginClicked(String email, String password);

        void onLoginClickedGoogle();

        void onLoginClickedFacebook(Fragment fragment);

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void onSuccessLogin(String indicatorAuth);

        void onFailureLogin(String indicatorAuth);
    }
}
