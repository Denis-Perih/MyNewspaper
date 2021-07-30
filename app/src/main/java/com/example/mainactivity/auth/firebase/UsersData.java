package com.example.mainactivity.auth.firebase;

import android.net.Uri;

public class UsersData {

    private String name, email, password;

    private Uri photoUrl;

    public UsersData() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Uri getPhotoUri() {
        return photoUrl;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUrl = photoUri;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
