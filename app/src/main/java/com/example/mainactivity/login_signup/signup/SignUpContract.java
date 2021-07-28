package com.example.mainactivity.login_signup.signup;

import android.content.Intent;

import androidx.fragment.app.Fragment;

public interface SignUpContract {

    interface View {

        void signInWithEmail(String name, String email, String password);

        void showSnackBar(String text);

        void startSignUpGoogle();

        void startSignUpFacebook();

        void startFirebaseAuthWithGoogle(String idToken);

        void startActivityResult(int requestCode, int resultCode, Intent data);

        void successSignUp();

        void failureSignUp(String text);
    }

    interface Presenter {

        void onSignUpClicked(String name, String email, String password);

        void onSignUpClickedGoogle();

        void onSignUpClickedFacebook(Fragment fragment);

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void onSuccessSignUp(String indicatorAuth);

        void onFailureSignUp(String indicatorAuth);
    }
}
