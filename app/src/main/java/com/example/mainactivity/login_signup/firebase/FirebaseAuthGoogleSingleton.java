package com.example.mainactivity.login_signup.firebase;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class FirebaseAuthGoogleSingleton {

    private static final String AUTH_GOOGLE = "auth_google";

    private static FirebaseAuthGoogleSingleton instance;

    public static final int RC_SIGN_IN = 123;

    public final String WEB_CLIENT_ID = "337548636915-qch1nbefitcohpsk5tfo8nua7b1tmigj.apps.googleusercontent.com";

    private FirebaseAuth mAuth;

    public GoogleSignInClient mGoogleSignInClient;

    private Activity activity;

    FirebaseListener fblGoogle;

    private FirebaseAuthGoogleSingleton() {

    }

    public void init(Activity activity, FirebaseListener fblGoogle) {
        this.activity = activity;
        this.fblGoogle = fblGoogle;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(WEB_CLIENT_ID)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);

        mAuth = FirebaseAuth.getInstance();
    }

    public static FirebaseAuthGoogleSingleton getInstance() {
        if (instance == null) {
            instance = new FirebaseAuthGoogleSingleton();
        }
        return instance;
    }

    public void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        fblGoogle.onSuccess(AUTH_GOOGLE);
                    } else {
                        fblGoogle.onFailure(AUTH_GOOGLE);
                    }
                }).addOnFailureListener(e -> fblGoogle.onFailure(AUTH_GOOGLE));
    }

    public void signIn() {
        if (mAuth.getCurrentUser() != null){
            fblGoogle.onSuccess(AUTH_GOOGLE);
        } else {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            activity.startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }
}