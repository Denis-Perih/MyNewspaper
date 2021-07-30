package com.example.mainactivity.auth.firebase;

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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class FirebaseLoginSingleton {

    private static final String AUTH_EMAIL_PASSWORD = "auth_email_password";
    private static final String AUTH_GOOGLE = "auth_google";
    private static final String AUTH_FACEBOOK = "auth_facebook";
    public static final int RC_SIGN_IN = 123;

    public final String WEB_CLIENT_ID = "337548636915-qch1nbefitcohpsk5tfo8nua7b1tmigj.apps.googleusercontent.com";

    public GoogleSignInClient mGoogleSignInClient;

    private CallbackManager mCallbackManager;

    private Activity activity;

    private static FirebaseLoginSingleton instance;

    private FirebaseAuth auth;

    FirebaseListener fbListener;

    private FirebaseLoginSingleton() {
    }

    public void init(Activity activity, FirebaseListener fbListener) {
        this.activity = activity;
        this.fbListener = fbListener;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(WEB_CLIENT_ID)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);

        FacebookSdk.sdkInitialize(this.activity);
        mCallbackManager = CallbackManager.Factory.create();

        auth = FirebaseAuth.getInstance();
    }

    public static FirebaseLoginSingleton getInstance() {
        if (instance == null) {
            instance = new FirebaseLoginSingleton();
        }
        return instance;
    }

// ------------------------------------------- Login with email and password

    public void executeSignUp(FirebaseListener fbListener, String name, String email, String password) {

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    UsersData uData = new UsersData();
                    uData.setName(name);
                    uData.setEmail(email);
                    uData.setPassword(password);
                    fbListener.onSuccess(AUTH_EMAIL_PASSWORD);
                }).addOnFailureListener(e -> fbListener.onFailure(AUTH_EMAIL_PASSWORD));
    }

    public void executeLogin(FirebaseListener fbListener, String email, String password) {

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        fbListener.onSuccess(AUTH_EMAIL_PASSWORD);
                    } else {
                        fbListener.onFailure(AUTH_EMAIL_PASSWORD);
                    }
                }).addOnFailureListener(e -> fbListener.onFailure(AUTH_EMAIL_PASSWORD));
    }

// ------------------------------------------- Login with google

    public void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        fbListener.onSuccess(AUTH_GOOGLE);
                    } else {
                        fbListener.onFailure(AUTH_GOOGLE);
                    }
                }).addOnFailureListener(e -> fbListener.onFailure(AUTH_GOOGLE));
    }

    public void signIn() {
        if (auth.getCurrentUser() != null){
            fbListener.onSuccess(AUTH_GOOGLE);
        } else {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            activity.startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

// ------------------------------------------- Login with facebook

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void loginFacebook() {
        if (auth.getCurrentUser() != null){
            fbListener.onSuccess(AUTH_FACEBOOK);
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
                    fbListener.onFailure(AUTH_FACEBOOK);
                }
            });
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        fbListener.onSuccess(AUTH_FACEBOOK);
                    } else {
                        fbListener.onFailure(AUTH_FACEBOOK);
                    }
                }).addOnFailureListener(e -> fbListener.onFailure(AUTH_FACEBOOK));
    }
}
