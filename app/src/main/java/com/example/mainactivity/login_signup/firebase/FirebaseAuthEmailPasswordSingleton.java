package com.example.mainactivity.login_signup.firebase;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthEmailPasswordSingleton {

    private static final String AUTH_EMAIL_PASSWORD = "auth_email_password";

    private static FirebaseAuthEmailPasswordSingleton instance;

    private FirebaseAuth auth;

    private FirebaseAuthEmailPasswordSingleton() {
    }

    public void init() {
        auth = FirebaseAuth.getInstance();
    }

    public static FirebaseAuthEmailPasswordSingleton getInstance() {
        if (instance == null) {
            instance = new FirebaseAuthEmailPasswordSingleton();
        }
        return instance;
    }

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
}