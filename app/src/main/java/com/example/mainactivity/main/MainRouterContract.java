package com.example.mainactivity.main;

import android.os.Bundle;

import com.example.mainactivity.retrofit.Post;

public interface MainRouterContract {

    void openHomeFragment();

    void openMoreAboutFragment(Post post);

    void openSplashFragment();

    void openSplashScreenFragment();

    void openLoginFragment();

    void openSignUpFragment();
}
