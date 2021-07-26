package com.example.mainactivity.login_signup.firebase;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthFacebookSingleton {

    private static final String AUTH_FACEBOOK = "auth_facebook";

    private static FirebaseAuthFacebookSingleton instance;

    private FirebaseAuth mAuth;

    private CallbackManager mCallbackManager;

    FirebaseListener fblFacebook;

    private FirebaseAuthFacebookSingleton() {

    }

    public void init(Activity activity, FirebaseListener fblFacebook) {
        FacebookSdk.sdkInitialize(activity);
        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        this.fblFacebook = fblFacebook;
    }

    public static FirebaseAuthFacebookSingleton getInstance() {
        if (instance == null) {
            instance = new FirebaseAuthFacebookSingleton();
        }
        return instance;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void loginFacebook() {
        if (mAuth.getCurrentUser() != null){
            fblFacebook.onSuccess(AUTH_FACEBOOK);
        } else {
            LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                }

                @Override
                public void onError(FacebookException error) {
                    fblFacebook.onFailure(AUTH_FACEBOOK);
                }
            });
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        fblFacebook.onSuccess(AUTH_FACEBOOK);
                    } else {
                        fblFacebook.onFailure(AUTH_FACEBOOK);
                    }
                }).addOnFailureListener(e -> fblFacebook.onFailure(AUTH_FACEBOOK));
    }
}